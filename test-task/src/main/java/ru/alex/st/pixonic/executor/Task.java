package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class Task<T> implements Comparable<Task<T>> {
	
	private LocalDateTime scheduledTime;
	
	private LocalDateTime receivedTime;

	private Callable<T> callable;
	
	public Task(LocalDateTime scheduledDateTime, LocalDateTime addTime, Callable<T> callable) {
		this.scheduledTime = scheduledDateTime;
		this.callable = callable;
	}
	
	public LocalDateTime getScheduledTime() {
		return scheduledTime;
	}

	public Callable<T> getCallable() {
		return callable;
	}

	@Override
	public int compareTo(Task<T> o) {
		if (o == null) {
			return -1000;
		} else if (this.equals(o)) {
			if (this.receivedTime.isBefore(o.receivedTime)) {
				return 1;
			} else if (this.receivedTime.isBefore(o.receivedTime)){
				return -1;
			} else {
				return 0;
			}
		} else if (this.scheduledTime.isAfter(o.scheduledTime)) {
			return 1;
		} else {
			return -1;
		}
	}
	
	@Override
	public String toString() {
		return String.format("[Execution date = %s, time = %s, task = %s]", scheduledTime.toLocalDate(), scheduledTime.toLocalTime(), callable);
	}

	public LocalDateTime getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(LocalDateTime receivedTime) {
		this.receivedTime = receivedTime;
	}

}
