package com.example.com.rakesh.smart_task_manager.dto;

public class TaskStatsDTO {
	private long total;
	private long completed;
	private long pending;
	private long inProgress;
	public TaskStatsDTO(long total, long completed, long pending, long inProgress) {
		super();
		this.total = total;
		this.completed = completed;
		this.pending = pending;
		this.inProgress = inProgress;
	}
	public long getTotal() {
		return total;
	}
	
	public long getCompleted() {
		return completed;
	}
	
	public long getPending() {
		return pending;
	}
	
	public long getInProgress() {
		return inProgress;
	}
	
	

}
