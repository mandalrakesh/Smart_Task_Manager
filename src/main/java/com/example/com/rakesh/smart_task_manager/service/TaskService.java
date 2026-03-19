package com.example.com.rakesh.smart_task_manager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.com.rakesh.smart_task_manager.dto.AdminStatsDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskRequestDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskResponseDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskStatsDTO;
import com.example.com.rakesh.smart_task_manager.entity.Task;
import com.example.com.rakesh.smart_task_manager.entity.TaskPriority;
import com.example.com.rakesh.smart_task_manager.entity.TaskStatus;

public interface TaskService {
	TaskResponseDTO createTask(TaskRequestDTO request);
	//List<TaskResponseDTO> getAllTask();
	Page<TaskResponseDTO> getAllTask(int page,int size,String sortBy, String direction);
	TaskResponseDTO getTaskById(Long id);
	TaskResponseDTO updateTask(Long id, TaskRequestDTO task);
	List<TaskResponseDTO> getTaskByStatus(TaskStatus status);
	List<TaskResponseDTO> getTaskByPriority(TaskPriority priority);
	void deleteTask(Long id);
	//Page<TaskResponseDTO> filterTasks(TaskStatus status, TaskPriority priority);
	Page<TaskResponseDTO> filterTasks(TaskStatus status, TaskPriority priority, Pageable pageable);
	TaskStatsDTO getTaskStats();
	AdminStatsDTO getAdminStats();

}
