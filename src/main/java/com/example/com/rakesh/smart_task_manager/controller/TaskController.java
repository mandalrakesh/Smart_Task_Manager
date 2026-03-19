package com.example.com.rakesh.smart_task_manager.controller;

import java.time.LocalDate;
import java.util.List;

//import java.awt.print.Pageable;
//import java.util.List;

//import org.hibernate.query.Page;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.com.rakesh.smart_task_manager.dto.AdminStatsDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskRequestDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskResponseDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskStatsDTO;
import com.example.com.rakesh.smart_task_manager.entity.Task;
import com.example.com.rakesh.smart_task_manager.entity.TaskPriority;
import com.example.com.rakesh.smart_task_manager.entity.TaskStatus;
import com.example.com.rakesh.smart_task_manager.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	private final TaskService taskService;
	
	public TaskController(TaskService taskService) {
		this.taskService=taskService;
	}
	
	
	//Create Task
	
	@PostMapping
	public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO request) {
		return taskService.createTask(request);
	}
	
	//Get All Task
	
	
	
	
	//Pagination
	@Operation(summary = "Get all tasks with pagination")
	@GetMapping
    public Page<TaskResponseDTO> getAllTasks(
    		@RequestParam(defaultValue = "0")int page,
    		@RequestParam(defaultValue = "5")int size,
    		@RequestParam(defaultValue = "id")String sortBy,
    		@RequestParam(defaultValue = "asc")String direction){
		
		return taskService.getAllTask(page,size,sortBy,direction);
	}
	
	//Get Task by Id
	
	@GetMapping("/{id}")
	public TaskResponseDTO getTaskById(@PathVariable Long id) {
		return taskService.getTaskById(id);
	}
	//Get Task by Status
	@GetMapping("/status/{status}")
	public List<TaskResponseDTO> getTaskByStatus(@PathVariable TaskStatus status){
		return taskService.getTaskByStatus(status);
	}
	
	//Get Task By Priority filtering
	
	@GetMapping("/priority/{priority}")
	public List<TaskResponseDTO> getTaskByPriority(@PathVariable TaskPriority priority){
		return taskService.getTaskByPriority(priority);
		
	}
	
	// Update Task
	
	@PutMapping("/{id}")
	public TaskResponseDTO updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO request) {
		return taskService.updateTask(id, request);
	}
	
	//Patch Mapping
	//@PatchMapping("/{id}/status")
	//public TaskResponseDTO updateStatus
	//Delete Task
	
	@DeleteMapping("/{id}")
	public void deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
	}
	
	//Task filtering Api
	
	@GetMapping("/filter")
	public Page<TaskResponseDTO> filterTasks(@RequestParam(required = false) TaskStatus status, @RequestParam(required=false) TaskPriority priority, Pageable pageable){
		return taskService.filterTasks(status, priority, pageable);
	}
	
	@GetMapping("/stats")
	public TaskStatsDTO getTaskStats() {
		return taskService.getTaskStats();
	}
	
	@GetMapping("/admin/stats")
	@PreAuthorize("hasRole('ADMIN')")
	
	public AdminStatsDTO getAdminStats() {
		return taskService.getAdminStats();
	}
	
	

}
