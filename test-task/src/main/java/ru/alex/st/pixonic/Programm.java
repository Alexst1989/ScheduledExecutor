package ru.alex.st.pixonic;

import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
//	    ScheduledTaskExecutor<CallableResult> scheduledTaskExecutor2 = ScheduledTaskExecutor.startNewExecutor(100);
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime executionTime = LocalDateTime.of(LocalDate.now(),
//                        LocalTime.of(now.getHour(), now.getMinute(), now.getSecond() + 5, now.getNano()));
//
//        LinkedList<Callable<CallableResult>> taskList = new LinkedList<>();
//        taskList.add(new SimpleCallable(executionTime));
//        taskList.add(new SimpleCallable(executionTime));
//        taskList.add(new SimpleCallable(executionTime));
//
//        for (Callable<CallableResult> callable : taskList) {
//            scheduledTaskExecutor2.addTask(executionTime, callable);
//        }
	}
	
}
