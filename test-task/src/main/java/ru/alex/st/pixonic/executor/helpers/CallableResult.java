package ru.alex.st.pixonic.executor.helpers;

import java.time.LocalDateTime;

public class CallableResult {

	private String stringResult;
	
	private Integer taskId;

	private LocalDateTime startTime;
	
	private LocalDateTime scheduleTime;

	private LocalDateTime finishTime;
	
	private LocalDateTime creationTime;

	public CallableResult(String stringResult, int taskId, LocalDateTime scheduleTime, LocalDateTime startTime, LocalDateTime finishTime, 
	                LocalDateTime creationTime) {
		this.stringResult = stringResult;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.scheduleTime = scheduleTime;
		this.taskId = taskId;
		this.creationTime = creationTime;
	}

	public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getStringResult() {
		return stringResult;
	}

	public void setStringResult(String stringResult) {
		this.stringResult = stringResult;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalDateTime finishTime) {
		this.finishTime = finishTime;
	}

	public LocalDateTime getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(LocalDateTime scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

}
