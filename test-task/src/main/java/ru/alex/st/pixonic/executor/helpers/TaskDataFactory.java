package ru.alex.st.pixonic.executor.helpers;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

public class TaskDataFactory {

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
