package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomScheduledExecutor2<T> implements Runnable {

	private static final Logger LOGGER = LogManager.getLogger(CustomScheduledExecutor2.class);

	private PriorityBlockingQueue<Task<T>> inQueue = new PriorityBlockingQueue<>();

	private ExecutorService executor;

	private boolean run = true; // TODO Make accurate shutdown

	private Lock lock = new ReentrantLock();

	private Condition condition = lock.newCondition();

	private Queue<Future<T>> outQueue; // Очередь не нужна

	private Thread mainThread;

	private CustomScheduledExecutor2(ExecutorService executor, Queue<Future<T>> outQueue) {
		this.executor = executor;
		this.outQueue = outQueue;
		mainThread = new Thread(this);
		mainThread.start();
	}

	public void addTask(LocalDateTime dateTime, Callable<T> callable) {
		inQueue.offer(new Task<T>(dateTime, LocalDateTime.now(), callable));
		lock.lock();
		try {
			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	public void stop() {
		this.run = false;
		lock.lock();
		try {
			condition.signal();
		} finally {
			lock.unlock();
		}
		executor.shutdown();
	}

	@Override
	public void run() {
		while (run) {
			lock.lock();
			try { // TODO handle in more convenient way
				if (inQueue.isEmpty()) {
					condition.await();
				} else {
					Task<T> task = inQueue.peek();
					if (task.getScheduledTime().equals(LocalDateTime.now())
					                || task.getScheduledTime().isAfter(LocalDateTime.now())) {
						Future<T> result = executor.submit(inQueue.poll().getCallable());
						outQueue.offer(result);
					} else {
						condition.await(getWaitingTime(task), TimeUnit.MILLISECONDS);
					}
				}
			} catch (InterruptedException ex) {
				LOGGER.error(ex);
			} finally {
				lock.unlock();
			}
		}
	}

	private long getWaitingTime(Task<T> task) {
		return LocalDateTime.now().until(task.getScheduledTime(), ChronoUnit.MILLIS);
	}

	public Queue<Future<T>> getOutQueue() {
		return outQueue;
	}

	public static <E> CustomScheduledExecutor2<E> getCustomScheduledExecutor() {
		Queue<Future<E>> outQueue = new LinkedList<>();
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 3);
		CustomScheduledExecutor2<E> executor = new CustomScheduledExecutor2<>(executorService, outQueue);
		return executor;
	}

}
