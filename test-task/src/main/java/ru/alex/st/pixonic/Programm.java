package ru.alex.st.pixonic;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.alex.st.pixonic.executor.ScheduledTaskExecutor;
import ru.alex.st.pixonic.executor.helpers.CallableResult;
import ru.alex.st.pixonic.executor.helpers.TaskData;
import ru.alex.st.pixonic.executor.helpers.TaskDataFactory;

/*
 * Условие:
 * На вход поступают пары (LocalDateTime, Callable). Нужно реализовать систему, которая будет выполнять 
 * Callable для каждого пришедшего события в указанный LocalDateTime, либо как можно скорее в случае если 
 * система перегружена и не успевает все выполнять (имеет беклог). Задачи должны выполняться в порядке согласно 
 * значению LocalDateTime либо в порядке прихода события для равных LocalDateTime. События могут приходить 
 * в произвольном порядке и добавление новых пар (LocalDateTime, Callable) может вызываться из разных потоков.
 * 
 * Реализация:
 * Условие задачи "Задачи должны выполняться в порядке прихода события для равных LocalDateTime"
 * выполнено с точностью до момента передачи задания из DelayedWorkQueue в потоки выполнения (Worker) 
 * в ScheduledThreadPoolExecutor. Очередь DelayedWorkQueue гарантирует порядок передачи FIFO для заданий с равным
 * временем выполенения в поток, который его будет выполнять. Однако, порядок начала выполнения этих 
 * заданий в потоках пула после передачи из очереди не гарантируется.
 * 
 * Остальные условия задачи выполнены полностью.
 *
 */

public class Programm {
	
	private static final Logger LOGGER = LogManager.getLogger(Programm.class);

	public static void main(String args[]) throws InterruptedException, ExecutionException {
		
		//Создание пула
		ScheduledTaskExecutor<CallableResult> executor = ScheduledTaskExecutor.startNewScheduledTaskExecutor();

		//Добавление пар LocalDateTime, Callable
		TaskData<CallableResult> task1 = TaskDataFactory.getRandomTaskForMinute();
		TaskData<CallableResult> task2 = TaskDataFactory.getRandomTaskForMinute();
		TaskData<CallableResult> task3 = TaskDataFactory.getRandomTaskForMinute();

		executor.addTask(task1.getLocalDateTime(), task1.getCallable());
		executor.addTask(task2.getLocalDateTime(), task2.getCallable());
		executor.addTask(task3.getLocalDateTime(), task3.getCallable());

		//Вывод результатов выполнения заданий
		Queue<ScheduledFuture<CallableResult>> outQueue = executor.getOutQueue(); 
		LinkedList<CallableResult> results = new LinkedList<>();
		while (!outQueue.isEmpty()) {
			ScheduledFuture<CallableResult> future = outQueue.poll();
			results.add(future.get());
			
		}
		for(CallableResult result : results) {
			LOGGER.info(result.getStringResult());
		}
		
		executor.stop();
	}
	
}
