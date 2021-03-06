package ru.alex.st.pixonic.executor;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.alex.st.pixonic.executor.helpers.CallableResult;

public class ScheduledTaskExecutorTest {

    private ScheduledTaskExecutor<CallableResult> scheduledTaskExecutor;

    @BeforeClass
    private void beforeClass() {
        scheduledTaskExecutor = ScheduledTaskExecutor.startNewExecutor();
    }

    @AfterClass
    private void afterClass() throws InterruptedException {
        scheduledTaskExecutor.stop();
    }

    @Test(dataProvider = "taskDataProvider100", dataProviderClass = TaskDataProvider.class, threadPoolSize = 10)
    public void testConcurrentAdding(List<TaskData<CallableResult>> taskList)
                    throws InterruptedException, ExecutionException {
        final int taskNumber = taskList.size();
        for (TaskData<CallableResult> task : taskList) {
            scheduledTaskExecutor.addTask(task.getLocalDateTime(), task.getCallable());
        }
        LinkedList<CallableResult> resultList = new LinkedList<>();
        BlockingQueue<CallableResult> outQueue = scheduledTaskExecutor.getOutQueue();
        int completedTasks = 0;
        while (completedTasks < taskNumber) {
            resultList.add(outQueue.take());
            completedTasks++;
        }
        for (CallableResult result : resultList) {
            System.out.println(String.format("taskId:%s startTime:%s creationTime=%s scheduled:%s", result.getTaskId(),
                            result.getStartTime(), result.getCreationTime(), result.getScheduleTime()));
            Assert.assertEquals(true, result.getScheduleTime().isBefore(result.getStartTime())
                            || result.getScheduleTime().isEqual(result.getStartTime()));
        }
    }

    @Test(dataProvider = "taskDataProviderEqualTime100", dataProviderClass = TaskDataProvider.class)
    public void testExecutionOrder(List<TaskData<CallableResult>> taskList)
                    throws InterruptedException, ExecutionException {
        final int taskNumber = taskList.size();
        for (TaskData<CallableResult> task : taskList) {
            scheduledTaskExecutor.addTask(task.getLocalDateTime(), task.getCallable());
        }
        LinkedList<CallableResult> resultList = new LinkedList<>();
        BlockingQueue<CallableResult> outQueue = scheduledTaskExecutor.getOutQueue();

        int completedTasks = 0;
        while (completedTasks < taskNumber) {
            resultList.add(outQueue.take());
            completedTasks++;
        }
        CallableResult previous = null;
        resultList.sort((o1, o2) -> {
            return o1.getCreationTime().compareTo(o2.getCreationTime());
        });
        for (CallableResult result : resultList) {
            System.out.println(String.format("taskId:%s startTime:%s creationTime=%s scheduled:%s", result.getTaskId(),
                            result.getStartTime(), result.getCreationTime(), result.getScheduleTime()));
            if (previous != null) {
                Assert.assertEquals(true, result.getStartTime().isEqual(previous.getStartTime())
                                || result.getStartTime().isAfter(previous.getStartTime()));
                Assert.assertEquals(true, result.getCreationTime().isEqual(previous.getCreationTime())
                                || result.getCreationTime().isAfter(previous.getCreationTime()));
            }
            Assert.assertEquals(true, result.getScheduleTime().isBefore(result.getStartTime())
                            || result.getScheduleTime().isEqual(result.getStartTime()));
            previous = result;
        }
    }

}
