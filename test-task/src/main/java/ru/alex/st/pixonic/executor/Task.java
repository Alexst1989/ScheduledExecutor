package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class Task<T> implements Comparable<Task<T>> {
	
	private LocalDateTime dateTime;

	private Callable<T> callable;
	
	public Task(LocalDateTime dateTime, Callable<T> callable) {
		this.dateTime = dateTime;
		this.callable = callable;
	}
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public Callable<T> getCallable() {
		return callable;
	}

	@Override
	public int compareTo(Task<T> o) {
		if (o == null) {
			return -1000;
		} else if (this.equals(o)) {
			return 0;
		} else if (this.dateTime.isAfter(o.dateTime)) {
			return 1;
		} else {
			return -1;
		}
	}
	
	@Override
	public String toString() {
		return String.format("[Execution date = %s, time = %s, task = %s]", dateTime.toLocalDate(), dateTime.toLocalTime(), callable);
	}

}
