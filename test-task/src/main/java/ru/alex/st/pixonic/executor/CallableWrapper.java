package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class CallableWrapper<T> implements Callable<T>, Comparable<CallableWrapper<T>> {
	
	private Callable<T> callable;
	
	private LocalDateTime addTime;
	
	public CallableWrapper(Callable<T> callable) {
		this.callable = callable;
	}

	@Override
	public T call() throws Exception {
		return callable.call();
	}

	@Override
	public int compareTo(CallableWrapper<T> o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	 

}
