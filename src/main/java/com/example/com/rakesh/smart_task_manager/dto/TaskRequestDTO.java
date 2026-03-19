package com.example.com.rakesh.smart_task_manager.dto;

import java.time.LocalDate;

import com.example.com.rakesh.smart_task_manager.entity.TaskPriority;
import com.example.com.rakesh.smart_task_manager.entity.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TaskRequestDTO {
	@NotBlank(message="Title cannot be empty")
	private String title;
	@NotBlank(message="Description cannot be empty")
	private String description;
	@NotNull(message="status cannot be empty")
	private TaskStatus status;
	@NotNull(message="Priority cannot be empty")
	private TaskPriority priority;
	private LocalDate dueDate;
	private String assignedTo;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public TaskPriority getPriority() {
		return priority;
	}
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	
	
	

}
