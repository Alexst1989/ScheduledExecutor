package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.alex.st.pixonic.Programm;
import ru.alex.st.pixonic.executor.helpers.SimpleCallable;

/**
 * From documentation about ScheduledThreadPoolExecutor
 * 
 * Delayed tasks execute no sooner than they are enabled, but without any
 * real-time guarantees about when, after they are enabled, they will commence.
 * Tasks scheduled for exactly the same execution time are enabled in
 * first-in-first-out (FIFO) order of submission.
 * 
 * @author alexp
 *
 * @param <T>
 */

public class ScheduledTaskExecutor<T> implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Programm.class);

    private DelayQueue<Task<T>> delayQueue = new DelayQueue<>();
    private PriorityBlockingQueue<Task<T>> priorityQueue = new PriorityBlockingQueue<>();
    private boolean run = true;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private LinkedBlockingQueue<T> outQueue = new LinkedBlockingQueue<>();
    private AtomicInteger taskCounter = new AtomicInteger(0);
    private int maxTasks;
    private ReentrantLock lock = new ReentrantLock();

    private ScheduledTaskExecutor(int maxTasks) {
        this.maxTasks = maxTasks;
    }

    public void addTask(LocalDateTime dateTime, Callable<T> callable) {
//        lock.lock();
//        try {
            delayQueue.offer(new Task<>(dateTime, LocalDateTime.now(), callable));
//        } finally {
//            lock.unlock();
//        }
    }

    public void addTask(Task<T> task) {
        lock.lock();
        try {
            delayQueue.offer(task);
        } finally {
            lock.unlock();
        }
    }

    public static <T> ScheduledTaskExecutor<T> startNewExecutor(int maxTasks) {
        ScheduledTaskExecutor<T> executor = new ScheduledTaskExecutor<>(maxTasks);
        Thread t = new Thread(executor);
        t.start();
        return executor;
    }

    public void stop() {
        run = false;
        executor.shutdown();
        LOGGER.info("Main executor {} is shut down", executor);
    }

    @Override
    public void run() {
        while (run && taskCounter.getAndIncrement() < maxTasks) {
            lock.lock();
            try {
                Task<T> firstAvailable = delayQueue.take();
                delayQueue.drainTo(priorityQueue);
                priorityQueue.offer(firstAvailable);
                while (priorityQueue.size() > 0) {
                    LOGGER.trace("task:{} priorityQueue.size:{}", priorityQueue.peek(), priorityQueue.size());
                    Task<T> task = priorityQueue.poll();
                    Callable<T> call = task.getCallable();
                    if (call instanceof SimpleCallable) { //For testing and debugging
                        SimpleCallable simpleCall = (SimpleCallable) call;
                        simpleCall.setCreationTime(task.getCreationTime());
                    }
                    Future<T> future = executor.submit(call);
                    
                    outQueue.add(future.get());
                }
            } catch (InterruptedException | ExecutionException ex) {
                LOGGER.error(ex);
            } finally {
                if (lock.isLocked())
                    lock.unlock();
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
