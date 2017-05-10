package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Task<T> implements Delayed {

    private static HashMap<TimeUnit, ChronoUnit> convertMap = new HashMap<>();

    private static final TimeUnit UNIT = TimeUnit.NANOSECONDS;

    private static final AtomicInteger atomicTaskCounter = new AtomicInteger(0);

    private int taskId;

    static {
        convertMap.put(TimeUnit.DAYS, ChronoUnit.DAYS);
        convertMap.put(TimeUnit.HOURS, ChronoUnit.HOURS);
        convertMap.put(TimeUnit.MICROSECONDS, ChronoUnit.MICROS);
        convertMap.put(TimeUnit.MILLISECONDS, ChronoUnit.MILLIS);
        convertMap.put(TimeUnit.MINUTES, ChronoUnit.MINUTES);
        convertMap.put(TimeUnit.NANOSECONDS, ChronoUnit.NANOS);
        convertMap.put(TimeUnit.SECONDS, ChronoUnit.SECONDS);
    }

    private LocalDateTime executionTime;

    private Callable<T> callable;

    private LocalDateTime creationTime;

    public Task(LocalDateTime dateTime, LocalDateTime creationTime, Callable<T> callabele) {
        this.executionTime = dateTime;
        this.callable = callabele;
        this.creationTime = creationTime;
        this.taskId = atomicTaskCounter.incrementAndGet();
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(LocalDateTime executionTime) {
        this.executionTime = executionTime;
    }

    public Callable<T> getCallable() {
        return callable;
    }

    public void setCallable(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return LocalDateTime.now().until(this.executionTime, convertMap.get(unit));
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.getDelay(UNIT) > o.getDelay(UNIT)) {
            return 1;
        } else if (this.getDelay(UNIT) < o.getDelay(UNIT)) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return String.format("Task Id = %s, CreationTime = %s, ExecutionTime = %s", taskId, creationTime,
                        executionTime);
    }

}
