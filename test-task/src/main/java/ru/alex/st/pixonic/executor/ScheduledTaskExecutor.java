package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.alex.st.pixonic.Programm;
import ru.alex.st.pixonic.executor.helpers.SimpleCallable;

/**
 * 
 * @author alexp
 *
 * @param <T>
 */

public class ScheduledTaskExecutor<T> implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Programm.class);

    private DelayQueue<Task<T>> delayQueue = new DelayQueue<>();
    
    private PriorityQueue<Task<T>> priorityQueue = new PriorityQueue<>((o1, o2)->{
        return o1.getCreationTime().compareTo(o2.getCreationTime());
    });
    
    private boolean run = true;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private LinkedBlockingQueue<T> outQueue = new LinkedBlockingQueue<>();
    private Thread runningThread;

    public void addTask(LocalDateTime dateTime, Callable<T> callable) {
        delayQueue.offer(new Task<>(dateTime, LocalDateTime.now(), callable));
    }

    public static <T> ScheduledTaskExecutor<T> startNewExecutor() {
        ScheduledTaskExecutor<T> executor = new ScheduledTaskExecutor<>();
        Thread t = new Thread(executor);
        t.start();
        return executor;
    }

    public void stop() {
        run = false;
        if (runningThread != null)
            runningThread.interrupt();
        executor.shutdown();
        LOGGER.info("Main executor {} is shut down", executor);
    }

    @Override
    public void run() {
        runningThread = Thread.currentThread();
        while (run) {
            try {
                Task<T> firstAvailable = delayQueue.take();
                //At that moment of execution can be added for example 100 new tasks with elapsed delay time.
                //If so, priorityQueue comparator will place them at the end of the priority queue 
                //according to their creation time.
                delayQueue.drainTo(priorityQueue);
                priorityQueue.offer(firstAvailable);
                while (priorityQueue.size() > 0) {
                    LOGGER.trace("task:{} priorityQueue.size:{}", priorityQueue.peek(), priorityQueue.size());
                    Task<T> task = priorityQueue.poll();
                    Callable<T> call = task.getCallable();
                    if (call instanceof SimpleCallable) { // For testing and
                                                          // debugging
                        SimpleCallable simpleCall = (SimpleCallable) call;
                        simpleCall.setCreationTime(task.getCreationTime());
                    }
                    Future<T> future = executor.submit(call);
                    outQueue.add(future.get());
                }
            } catch (ExecutionException ex) {
                LOGGER.error(ex);
            } catch (InterruptedException ex) {
                LOGGER.info("Thread was stopped");
            }
        }
    }

    public int getTaskNumber() {
        return delayQueue.size();
    }

    public BlockingQueue<T> getOutQueue() {
        return outQueue;
    }

}
