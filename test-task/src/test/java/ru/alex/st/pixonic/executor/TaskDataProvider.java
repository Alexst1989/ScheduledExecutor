package ru.alex.st.pixonic.executor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

import org.testng.annotations.DataProvider;

public class TaskDataProvider {
	
	@DataProvider(name="taskDataProvider")
	public Object[][] getData() {
		List<Task<CallableResult>> taskList = new LinkedList<>();
		for (int i = 0 ; i< 10; i++) taskList.add(getRandomTaskForMinute());
		return new Object[][]{
			{taskList},
		};
	}

	/**
	 * For tests
	 * 
	 * @return
	 */
	public static Task<String> getRandomTaskForDay() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime date = LocalDateTime.of(now.getYear(), 
						now.getMonthValue(), 
						now.getDayOfMonth(),
						(int) (Math.random() * 24),
						(int) (Math.random() * 60),
						(int) (Math.random() * 60),
						(int) (Math.random() * 1000));
		Callable<String> callable = new SimpleCallable(date);
		return new Task<String>(date, callable);
	}
	
	/**
	 * Instantiates {@link Clock#systemDefaultZone() system clock}
	 * 
	 * @return
	 */
	public static Task<String> getRandomTaskForMinute() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime date = LocalDateTime.of(now.getYear(), 
						now.getMonthValue(), 
						now.getDayOfMonth(),
						now.getHour(),
						now.getMinute(),
						(int) (Math.random() * 60),
						(int) (Math.random() * 1000));
		Callable<String> callable = new SimpleCallable(date);
		return new Task<String>(date, callable);
	}
	
}
