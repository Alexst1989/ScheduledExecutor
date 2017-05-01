package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * From documentation about ScheduledThreadPoolExecutor
 * 
 * Delayed tasks execute no sooner than they are enabled, but without any
 * real-time guarantees about when, after they are enabled, they will commence.
 * Tasks scheduled for exactly the same execution time are enabled in
 * first-in-first-out (FIFO) order of submission.
 * 
 * @author alexp
 *
 * @param <T>
 */

public class ScheduledTaskExecutor<T> {

	private ScheduledExecutorService scheduledExecutor;

	private Queue<ScheduledFuture<T>> outQueue;

	private ScheduledTaskExecutor(ScheduledExecutorService scheduledExecutor, Queue<ScheduledFuture<T>> outQueue) {
		this.scheduledExecutor = scheduledExecutor;
		this.outQueue = outQueue;
	}

	public void addTask(LocalDateTime dateTime, Callable<T> callable) {
		long delay = LocalDateTime.now().until(dateTime, ChronoUnit.NANOS);
		outQueue.offer(scheduledExecutor.schedule(callable, delay, TimeUnit.NANOSECONDS));
	}

	public void stop() {
		scheduledExecutor.shutdown();
	}

	public static <E> ScheduledTaskExecutor<E> startNewScheduledTaskExecutor(Queue<ScheduledFuture<E>> outQueue) {
		int poolSize = Runtime.getRuntime().availableProcessors() * 3;
		ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(poolSize);
		executor.prestartAllCoreThreads();
		ScheduledTaskExecutor<E> scheduledExecutor = new ScheduledTaskExecutor<E>(executor, outQueue);
		return scheduledExecutor;
	}

}
