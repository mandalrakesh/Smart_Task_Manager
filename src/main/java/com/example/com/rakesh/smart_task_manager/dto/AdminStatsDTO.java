package com.example.com.rakesh.smart_task_manager.dto;

import java.util.List;

public class AdminStatsDTO {
	private long totalTasks;
	private long totalUsers;
	private List<UserTaskCountDTO> tasksPerUser;
	public AdminStatsDTO(long totalTasks, long totalUsers, List<UserTaskCountDTO> tasksPerUser) {
		super();
		this.totalTasks = totalTasks;
		this.totalUsers = totalUsers;
		this.tasksPerUser = tasksPerUser;
	}
	public long getTotalTasks() {
		return totalTasks;
	}
	
	public long getTotalUsers() {
		return totalUsers;
	}
	
	public List<UserTaskCountDTO> getTasksPerUser() {
		return tasksPerUser;
	}
	
	
	

}
