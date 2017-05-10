package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.DelayQueue;

import org.testng.annotations.Test;

import ru.alex.st.pixonic.executor.helpers.CallableResult;

public class DelayQueueTest {

    private DelayQueue<DelayedWithCreationTime> queue = new DelayQueue<>();

    @Test(dataProvider = "taskDataProviderEqualTime100", dataProviderClass = TaskDataProvider.class, threadPoolSize = 10)
    public void testOrder(List<TaskData<CallableResult>> taskList) throws InterruptedException {
        for (TaskData<CallableResult> taskData : taskList) {
            queue.offer(new Task<CallableResult>(taskData.getLocalDateTime(), LocalDateTime.now(), taskData.getCallable()));
            Thread.sleep(20);
        }
        for (TaskData<CallableResult> taskData : taskList) {
            while(queue.size() > 0 ) {
                if (queue.poll() != null) {
                    System.out.println(queue.poll());
                }
            }
        }
        
        
    }

}
