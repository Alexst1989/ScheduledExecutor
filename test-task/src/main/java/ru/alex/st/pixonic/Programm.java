package ru.alex.st.pixonic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.alex.st.pixonic.executor.ScheduledTaskExecutor;
import ru.alex.st.pixonic.executor.helpers.CallableResult;
import ru.alex.st.pixonic.executor.helpers.SimpleCallable;

/*
 * Условие:
 * На вход поступают пары (LocalDateTime, Callable). Нужно реализовать систему, которая будет выполнять 
 * Callable для каждого пришедшего события в указанный LocalDateTime, либо как можно скорее в случае если 
 * система перегружена и не успевает все выполнять (имеет беклог). Задачи должны выполняться в порядке согласно 
 * значению LocalDateTime либо в порядке прихода события для равных LocalDateTime. События могут приходить 
 * в произвольном порядке и добавление новых пар (LocalDateTime, Callable) может вызываться из разных потоков.
 * 
 * Реализация:
 * Новые задания поступают в DelayQueue. В этот момент фиксируется время прихода задания.
 * Поток выполняющий задания ждет доступного элемента очереди, затем из очереди достаются 
 * все доступные на данный момент задания и кладутся в PriorityQueue, где производиться сортировка 
 * по времени прихода задания. Из PriorityQueue задания поступают в SingleThreadPoolExecutor, 
 * чтобы гарантировать порядок их выполнения
 * 
 */

public class Programm {
	
	private static final Logger LOGGER = LogManager.getLogger(Programm.class);

	public static void main(String args[]) throws InterruptedException, ExecutionException {
	    ScheduledTaskExecutor<CallableResult> scheduledTaskExecutor = ScheduledTaskExecutor.startNewExecutor();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime executionTime = LocalDateTime.of(LocalDate.now(),
                        LocalTime.of(now.getHour(), now.getMinute(), now.getSecond() + 5, now.getNano()));

        LinkedList<Callable<CallableResult>> taskList = new LinkedList<>();
        taskList.add(new SimpleCallable(executionTime));
        taskList.add(new SimpleCallable(executionTime));
        taskList.add(new SimpleCallable(executionTime));

        int taskNumber = taskList.size();
        for (Callable<CallableResult> callable : taskList) {
            scheduledTaskExecutor.addTask(executionTime, callable);
            Thread.sleep(1);
        }
        BlockingQueue<CallableResult> outQueue = scheduledTaskExecutor.getOutQueue();
        int completedTasks = 0;
        while (completedTasks < taskNumber) {
            LOGGER.info(outQueue.take());
            completedTasks++;
        }
        scheduledTaskExecutor.stop();
	}
	
}
