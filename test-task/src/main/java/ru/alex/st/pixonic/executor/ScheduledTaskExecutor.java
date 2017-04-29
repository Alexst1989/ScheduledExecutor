package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ScheduledTaskExecutor<T> implements Runnable {
	
//	private static final Logger LOGGER = LogManager.getLogger(ScheduledTaskExecutorTest.class);

	private PriorityBlockingQueue<Task<T>> queue = new PriorityBlockingQueue<>();

//	private ExecutorService executor;

	private ScheduledExecutorService scheduledExecutor;
	
	private boolean run = true; //TODO Make accurate shutdown
	
	private Lock lock = new ReentrantLock();
	
	private Queue<ScheduledFuture<T>> outQueue;

//	private ScheduledTaskExecutor(ExecutorService executor, Queue<ScheduledFuture<T>> outQueue) {
//		this.executor = executor;
//		this.outQueue = outQueue;
//	}
	
	private ScheduledTaskExecutor(ScheduledExecutorService scheduledExecutor, Queue<ScheduledFuture<T>> outQueue) {
		this.scheduledExecutor = scheduledExecutor;
		this.outQueue = outQueue;
	}
	

	public void addTask(LocalDateTime dateTime, Callable<T> callable) {
		queue.offer(new Task<T>(dateTime, callable));
		lock.notify();
	}
	
	public void addTask2(LocalDateTime dateTime, Callable<T> callable) {
		outQueue.offer(scheduledExecutor.schedule(callable, dateTime.until(LocalDateTime.now(), ChronoUnit.MILLIS), TimeUnit.MILLISECONDS));
	}
	
	public void stop() {
		this.run = false;
		scheduledExecutor.shutdown();
	}

//	@Override
//	public void run() {
//		while (run) {
//			try { //TODO handle in more convenient way
//				if (queue.isEmpty()) {
//					lock.wait();
//				} else {
//					Task<T> task = queue.peek();
//					if (task.getDateTime().equals(LocalDateTime.now()) || task.getDateTime().isAfter(LocalDateTime.now())) {
//						Future<T> result = executor.submit(queue.poll().getCallable());
//						outQueue.offer(result);
//					} else {
//						lock.wait(getWaitingTime(task));
//					}
//				}
//			} catch (InterruptedException ex) {
//				LOGGER.error(ex);
//			}
//		}
//	}
	
	@Override
	public void run() {
		while (run) {
			
		}
	}
	
//	private long getWaitingTime(Task<T> task) {
//		return LocalDateTime.now().until(task.getDateTime(), ChronoUnit.MILLIS);
//	}
	
	
//	public static <E> ScheduledTaskExecutor<E> startNewScheduledTaskExecutor(Queue<Future<E>> outQueue, ExecutorService executor) {
//		ScheduledTaskExecutor<E> scheduledExecutor = new ScheduledTaskExecutor<E>(executor, outQueue);
//		Thread thread = new Thread(scheduledExecutor);
//		thread.start();
//		return scheduledExecutor;
//	}
	
	public static <E> ScheduledTaskExecutor<E> startNewScheduledTaskExecutor(Queue<ScheduledFuture<E>> outQueue) {
		//TODO number threads
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(10); 
		ScheduledTaskExecutor<E> scheduledExecutor = new ScheduledTaskExecutor<E>(executor, outQueue);
//		Thread thread = new Thread(scheduledExecutor);
//		thread.start();
		return scheduledExecutor;
	}

}
