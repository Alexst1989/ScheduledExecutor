package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleCallable implements Callable<CallableResult> {

	private static final Logger LOGGER = LogManager.getLogger(SimpleCallable.class);

	private static AtomicInteger counter = new AtomicInteger(0);
	
	private int taskId;

	private LocalDateTime taskTime;

	public SimpleCallable(LocalDateTime dateTime) {
		this.taskId = counter.incrementAndGet();
		this.taskTime = dateTime;
	}

	@Override
	public CallableResult call() throws Exception {
		LocalDateTime now = LocalDateTime.now();
		LOGGER.debug(String.format("[Thread id: %s, taskId:%s current time: %s, task time:%s]",
		                Thread.currentThread().getId(), taskId, now, taskTime));
		try {
			Thread.sleep((int) Math.random() * 5000);
		} catch (InterruptedException ex) {
			LOGGER.error(ex);
		}
		LOGGER.debug(String.format("Thread with id = %s has finished", Thread.currentThread().getId()));
		CallableResult result = new CallableResult(String.format("Callable with id %s finished", taskId), taskTime, now, LocalDateTime.now());
		return result;
	}

}
