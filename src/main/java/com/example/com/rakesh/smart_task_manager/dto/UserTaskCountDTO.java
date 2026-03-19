package com.example.com.rakesh.smart_task_manager.dto;

public class UserTaskCountDTO {
	private String email;
	private Long taskCont;
	public UserTaskCountDTO(String email, Long taskCont) {
		super();
		this.email = email;
		this.taskCont = taskCont;
	}
	public String getEmail() {
		return email;
	}
	
	public Long getTaskCont() {
		return taskCont;
	}
	
	
	

}
