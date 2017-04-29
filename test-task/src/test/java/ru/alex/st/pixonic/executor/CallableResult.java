package ru.alex.st.pixonic.executor;

import java.time.LocalDateTime;

public class CallableResult {

	private String stringResult;

	private LocalDateTime startTime;

	private LocalDateTime finishTime;

	public CallableResult(String stringResult, LocalDateTime startTime, LocalDateTime finishTime) {
		this.stringResult = stringResult;
		this.startTime = startTime;
		this.finishTime = finishTime;
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

}
