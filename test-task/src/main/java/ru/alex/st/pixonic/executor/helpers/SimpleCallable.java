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
	
	private LocalDateTime creationTime;

	public SimpleCallable(LocalDateTime scheduled) {
		this.taskId = counter.incrementAndGet();
		this.scheduledTime = scheduled;
	}

	@Override
	public CallableResult call() throws Exception {
		LocalDateTime start = LocalDateTime.now();
		LOGGER.debug(String.format("[taskId:%s StartTime: %s, creationTime:%s, Scheduled:%s, currentAfterScheduled=%s]",
		                taskId, start, creationTime, scheduledTime, start.isAfter(scheduledTime)));
		try {
			Thread.sleep((int) (Math.random() * 500));
		} catch (InterruptedException ex) {
			LOGGER.error(ex);
		}
		CallableResult result = new CallableResult(String.format("Callable with id %s finished", taskId), taskId, scheduledTime, start, LocalDateTime.now(), creationTime);
		return result;
	}
	
	
	

	public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public int getTaskId() {
		return taskId;
	}

}
