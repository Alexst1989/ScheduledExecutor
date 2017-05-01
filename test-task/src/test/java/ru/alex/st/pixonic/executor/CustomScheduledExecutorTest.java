package ru.alex.st.pixonic.executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;
import ru.alex.st.pixonic.executor.helpers.CallableResult;
import ru.alex.st.pixonic.executor.helpers.TaskData;

public class CustomScheduledExecutorTest {
				
	private CustomScheduledExecutor2<CallableResult> scheduledTaskExecutor;
	
	@BeforeClass
	private void beforeClass() {
		scheduledTaskExecutor = CustomScheduledExecutor2.getCustomScheduledExecutor();
	}
	
	@AfterClass
	private void afterClass() {
		scheduledTaskExecutor.stop();
	}
	
	@Test(dataProvider = "taskDataProvider100", dataProviderClass = TaskDataProvider.class, threadPoolSize = 10)
	public void testAddTask(List<TaskData<CallableResult>> taskList) throws InterruptedException, ExecutionException {
		for (TaskData<CallableResult> task : taskList) {
			scheduledTaskExecutor.addTask(task.getLocalDateTime(), task.getCallable());
		}
		Queue<Future<CallableResult>> resulQueue = scheduledTaskExecutor.getOutQueue();
		LinkedList<CallableResult> resultList = new LinkedList<>();
		for (Future<CallableResult> future : resulQueue) {
			resultList.add(future.get());
		}
		for (CallableResult result : resultList) {
			Assert.assertEquals(true, result.getScheduleTime().isBefore(result.getStartTime()) 
							|| result.getScheduleTime().isEqual(result.getStartTime()));
		}
	}	
	
}
