package ru.alex.st.pixonic.executor.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.testng.annotations.Test;

public class DelayedWorkQueueTest extends ScheduledThreadPoolExecutor {
	
	public DelayedWorkQueueTest(int corePoolSize) {
		super(corePoolSize);
	}
	
	public DelayedWorkQueueTest() {
		super(10);
	}

	@Test
	public void testQueue() {
		BlockingQueue<Runnable> queue = getQueue();
//		RunnableScheduledFuture future;
//		queue.offer(e)
//		
//		queue.poll()
		
		
	}

}
