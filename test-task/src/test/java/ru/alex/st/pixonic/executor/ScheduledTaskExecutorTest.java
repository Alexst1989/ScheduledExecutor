package ru.alex.st.pixonic.executor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ScheduledTaskExecutorTest {
		
	private LinkedList<ScheduledFuture<String>> outQueue = new LinkedList<>();
	
	private ScheduledTaskExecutor<String> scheduledTaskExecutor;
	
	@BeforeClass
	private void beforeClass() {
		scheduledTaskExecutor = ScheduledTaskExecutor.startNewScheduledTaskExecutor(outQueue);
	}
	
	@AfterClass
	private void afterClass() {
		scheduledTaskExecutor.stop();
	}
	
	@Test(dataProvider = "taskDataProvider", dataProviderClass = TaskDataProvider.class)
	public void testAddTask(List<Task<String>> taskList) throws InterruptedException, ExecutionException {
		for (Task<String> task : taskList) {
			scheduledTaskExecutor.addTask2(task.getDateTime(), task.getCallable());
		}
		for (ScheduledFuture<String> future : outQueue) {
			System.out.println(future.get());
		}
	}
	
}
