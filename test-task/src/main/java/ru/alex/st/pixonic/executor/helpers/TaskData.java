package ru.alex.st.pixonic.executor.helpers;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class TaskData<T> {
	
	private Callable<T> callable;
	
	private LocalDateTime localDateTime;
	
	public TaskData(LocalDateTime localDateTime, Callable<T> callable) {
		this.callable = callable;
		this.localDateTime = localDateTime;
	}

	public Callable<T> getCallable() {
		return callable;
	}

	public void setCallable(Callable<T> callable) {
		this.callable = callable;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}
	
	

}
