package ru.alex.st.pixonic.executor;

import java.util.List;
import java.util.PriorityQueue;

import org.testng.annotations.Test;

import junit.framework.Assert;

public class TaskTest {
	
	@Test(dataProvider="taskDataProvider", dataProviderClass=TaskDataProvider.class)
	public void testNaturalOrder(List<Task<String>> taskList) {
		PriorityQueue<Task<String>> queue = new PriorityQueue<>();
		for (Task<String> task : taskList) {
			queue.offer(task);
		}
		Task<String> previous = null;
		while (queue.size() != 0) {
			Task<String> current = queue.poll();
			if (previous != null) Assert.assertEquals(true, current.getDateTime().isAfter(previous.getDateTime()));
			previous = current;
		}			
	}
	
}
