package ru.alex.st.pixonic.executor.helpers;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleCallable implements Callable<CallableResult> {

	private static final Logger LOGGER = LogManager.getLogger(SimpleCallable.class);

	private static AtomicInteger counter = new AtomicInteger(0);
	
	private int taskId;

	private LocalDateTime scheduledTime;

	public SimpleCallable(LocalDateTime dateTime) {
		this.taskId = counter.incrementAndGet();
		this.scheduledTime = dateTime;
	}

	@Override
	public CallableResult call() throws Exception {
		LocalDateTime start = LocalDateTime.now();
		LOGGER.debug(String.format("[taskId:%s StartTime: %s, Scheduled:%s, currentAfterScheduled=%b]",
		                taskId, start, scheduledTime, start.isAfter(scheduledTime)));
		try {
			Thread.sleep((int) (Math.random() * 1000));
		} catch (InterruptedException ex) {
			LOGGER.error(ex);
		}
		LOGGER.trace(String.format("Thread with id = %s has finished", Thread.currentThread().getId()));
		CallableResult result = new CallableResult(String.format("Callable with id %s finished", taskId), taskId, scheduledTime, start, LocalDateTime.now());
		return result;
	}

}
