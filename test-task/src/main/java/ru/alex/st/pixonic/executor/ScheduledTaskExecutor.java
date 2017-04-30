package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScheduledTaskExecutor<T> {
	
//	private static final Logger LOGGER = LogManager.getLogger(ScheduledTaskExecutorTest.class);

	private ScheduledExecutorService scheduledExecutor;
	
	private Queue<ScheduledFuture<T>> outQueue;
	
	private ScheduledTaskExecutor(ScheduledExecutorService scheduledExecutor, Queue<ScheduledFuture<T>> outQueue) {
		this.scheduledExecutor = scheduledExecutor;
		this.outQueue = outQueue;
	}
	
	public void addTask(LocalDateTime dateTime, Callable<T> callable) {
		long delay = LocalDateTime.now().until(dateTime, ChronoUnit.MILLIS);
		outQueue.offer(scheduledExecutor.schedule(callable, delay, TimeUnit.MILLISECONDS));
	}
	
	public void stop() {
		scheduledExecutor.shutdown();
	}

	public static <E> ScheduledTaskExecutor<E> startNewScheduledTaskExecutor(Queue<ScheduledFuture<E>> outQueue) {
		int poolSize = Runtime.getRuntime().availableProcessors() * 3;
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(poolSize); 
		ScheduledTaskExecutor<E> scheduledExecutor = new ScheduledTaskExecutor<E>(executor, outQueue);
		return scheduledExecutor;
	}

}
