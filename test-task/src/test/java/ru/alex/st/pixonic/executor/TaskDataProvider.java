package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.DataProvider;

import ru.alex.st.pixonic.executor.helpers.CallableResult;
import ru.alex.st.pixonic.executor.helpers.TaskDataFactory;

public class TaskDataProvider {
	
	@DataProvider(name="taskDataProvider1000")
	public Object[][] getData1000() {
		return getData(1000);
	}
	
	@DataProvider(name="taskDataProvider100")
	public Object[][] getData100() {
		return getData(100);
	}
	
	@DataProvider(name="taskDataProviderEqualTime100")
	public Object[][] getDatawithEqualTime100() {
		return getDataWithEqualTime(100);
	}
	
	@DataProvider(name="taskDataProviderEqualTime1000")
	public Object[][] getDatawithEqualTime1000() {
		return getDataWithEqualTime(1000);
	}
	
	public Object[][] getData(int size) {
		List<TaskData<CallableResult>> taskList = new LinkedList<>();
		for (int i = 0; i < size; i++) taskList.add(TaskDataFactory.getRandomTaskForMinute());
		return new Object[][]{
			{taskList},
		};
	}
	
	public Object[][] getDataWithEqualTime(int size) {
		List<TaskData<CallableResult>> taskList = new LinkedList<>();
		LocalDateTime dateTime = TaskDataFactory.getLocalDateTimeRandomSeconds();
		for (int i = 0; i < size; i++) taskList.add(TaskDataFactory.getRandomTaskWithEqualExecutionTime(dateTime));
		return new Object[][]{
			{taskList},
		};
	}
	
}
