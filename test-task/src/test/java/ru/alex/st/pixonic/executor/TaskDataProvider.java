package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.testng.annotations.DataProvider;

import ru.alex.st.pixonic.executor.helpers.CallableResult;
import ru.alex.st.pixonic.executor.helpers.SimpleCallable;
import ru.alex.st.pixonic.executor.helpers.TaskData;

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
		for (int i = 0; i < size; i++) taskList.add(getRandomTaskForMinute());
		return new Object[][]{
			{taskList},
		};
	}
	
	public Object[][] getDataWithEqualTime(int size) {
		List<TaskData<CallableResult>> taskList = new LinkedList<>();
		LocalDateTime dateTime = getLocalDateTimeRandomSeconds();
		for (int i = 0; i < size; i++) taskList.add(getRandomTaskWithEqualExecutionTime(dateTime));
		return new Object[][]{
			{taskList},
		};
	}
	
	

	/**
	 * Creates task with scheduled execution time equal to current date, but with random time of day
	 * 
	 * @return Task<CallableResult>
	 */
	public static TaskData<CallableResult> getRandomTaskForDay() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime date = LocalDateTime.of(now.getYear(), 
						now.getMonthValue(), 
						now.getDayOfMonth(),
						(int) (Math.random() * 24),
						(int) (Math.random() * 60),
						(int) (Math.random() * 60),
						(int) ((Math.random() * 1000)) * 1_000_000);
		Callable<CallableResult> callable = new SimpleCallable(date);
		return new TaskData<CallableResult>(date, callable);
	}
	
	/**
	 * Creates task with scheduled execution time equal to current time, but with random seconds, nanos
	 *   
	 * @return Task<CallableResult>
	 */
	public static TaskData<CallableResult> getRandomTaskForMinute() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime date = LocalDateTime.of(now.getYear(), 
						now.getMonthValue(), 
						now.getDayOfMonth(),
						now.getHour(),
						now.getMinute(),
						(int) (Math.random() * 60),
						(int) ((Math.random() * 1000)) * 1_000_000);
		Callable<CallableResult> callable = new SimpleCallable(date);
		return new TaskData<CallableResult>(date, callable);
	}
	
	/**
	 * Creates task with scheduled execution time equal to current time, but with random seconds, nanos
	 *   
	 * @return Task<CallableResult>
	 */
	public static TaskData<CallableResult> getRandomTaskWithEqualExecutionTime(LocalDateTime localDateTime) {
		Callable<CallableResult> callable = new SimpleCallable(localDateTime);
		return new TaskData<CallableResult>(localDateTime, callable);
	}
	
	public static LocalDateTime getLocalDateTimeRandomSeconds() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime date = LocalDateTime.of(now.getYear(), 
						now.getMonthValue(), 
						now.getDayOfMonth(),
						now.getHour(),
						now.getMinute(),
						(int) (Math.random() * 60),
						(int) ((Math.random() * 1000)) * 1_000_000);
		return date;
	}
	
}
