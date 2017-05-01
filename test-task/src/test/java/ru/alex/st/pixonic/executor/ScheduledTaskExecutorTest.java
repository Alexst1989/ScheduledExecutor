package ru.alex.st.pixonic.executor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledFuture;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;
import ru.alex.st.pixonic.executor.helpers.CallableResult;
import ru.alex.st.pixonic.executor.helpers.SimpleCallable;
import ru.alex.st.pixonic.executor.helpers.TaskData;

public class ScheduledTaskExecutorTest {

	private PriorityBlockingQueue<ScheduledFuture<CallableResult>> outQueue = new PriorityBlockingQueue<>(); // Не
	                                                                                                         // нужна
	                                                                                                         // такая
	                                                                                                         // очередь

	private ScheduledTaskExecutor<CallableResult> scheduledTaskExecutor;

	@BeforeClass
	private void beforeClass() {
		scheduledTaskExecutor = ScheduledTaskExecutor.startNewScheduledTaskExecutor(outQueue);
	}

	@AfterClass
	private void afterClass() {
		scheduledTaskExecutor.stop();
	}

	// @Test(dataProvider = "taskDataProvider1000", dataProviderClass =
	// TaskDataProvider.class)
	public void testAddTask(List<TaskData<CallableResult>> taskList) throws InterruptedException, ExecutionException {
		for (TaskData<CallableResult> task : taskList) {
			scheduledTaskExecutor.addTask(task.getLocalDateTime(), task.getCallable());
		}
		LinkedList<CallableResult> resultList = new LinkedList<>();
		for (ScheduledFuture<CallableResult> future : outQueue) {
			resultList.add(future.get());
		}
		for (CallableResult result : resultList) {
			Assert.assertEquals(true, result.getScheduleTime().isBefore(result.getStartTime())
			                || result.getScheduleTime().isEqual(result.getStartTime()));
		}
	}

//	@Test(dataProvider = "taskDataProvider100", dataProviderClass = TaskDataProvider.class, threadPoolSize = 10)
	public void testConcurrentAdding(List<TaskData<CallableResult>> taskList)
	                throws InterruptedException, ExecutionException {
		for (TaskData<CallableResult> task : taskList) {
			scheduledTaskExecutor.addTask(task.getLocalDateTime(), task.getCallable());
		}
		LinkedList<CallableResult> resultList = new LinkedList<>();
		for (ScheduledFuture<CallableResult> future : outQueue) {
			resultList.add(future.get());
		}
		for (CallableResult result : resultList) {
			Assert.assertEquals(true, result.getScheduleTime().isBefore(result.getStartTime())
			                || result.getScheduleTime().isEqual(result.getStartTime()));
		}
	}

	@Test(dataProvider = "taskDataProviderEqualTime1000", dataProviderClass = TaskDataProvider.class)
	public void testExecutionOrder(List<TaskData<CallableResult>> taskList)
	                throws InterruptedException, ExecutionException {
		
		checkData(taskList);
		
		for (TaskData<CallableResult> task : taskList) {
			scheduledTaskExecutor.addTask(task.getLocalDateTime(), task.getCallable());
		}
		LinkedList<CallableResult> resultList = new LinkedList<>();
		for (ScheduledFuture<CallableResult> future : outQueue) {
			resultList.add(future.get());
		}
		CallableResult previous = null;
		resultList.sort((o1, o2) -> {
			//return o1.getStartTime().compareTo(o2.getStartTime());
			return o1.getTaskId().compareTo(o2.getTaskId());
		});
		for (CallableResult result : resultList) {
			System.out.println(String.format("taskId:%s	startTime:%s	scheduled:%s", result.getTaskId(),
			                result.getStartTime(), result.getScheduleTime()));
			if (previous != null)
				Assert.assertEquals(true, result.getStartTime().isEqual(previous.getStartTime()) 
								|| result.getStartTime().isAfter(previous.getStartTime()));
			previous = result;
		}
	}

	private void checkData(List<TaskData<CallableResult>> taskList) {
		SimpleCallable previous = null;
		for (TaskData<CallableResult> task : taskList) {
			if (previous != null)
				Assert.assertEquals(true, ((SimpleCallable) task.getCallable()).getTaskId() > previous.getTaskId());
			previous = (SimpleCallable) task.getCallable();
		}
	}

}
