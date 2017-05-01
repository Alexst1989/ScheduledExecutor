package ru.alex.st.pixonic.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

	public CustomScheduledThreadPoolExecutor(int corePoolSize) {
		super(corePoolSize);
	}
	
	@Override
	protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
		// TODO Auto-generated method stub
		return super.decorateTask(callable, task);
	}
	
	@Override
	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		// TODO Auto-generated method stub
		return super.newTaskFor(callable);
	}
	
	@Override
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		// TODO Auto-generated method stub
		return super.schedule(callable, delay, unit);
	}
	
	
	
	
}
