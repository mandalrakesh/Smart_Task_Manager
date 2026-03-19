package com.example.com.rakesh.smart_task_manager.service;

//import java.awt.print.Pageable;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.com.rakesh.smart_task_manager.dto.AdminStatsDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskRequestDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskResponseDTO;
import com.example.com.rakesh.smart_task_manager.dto.TaskStatsDTO;
import com.example.com.rakesh.smart_task_manager.dto.UserTaskCountDTO;
import com.example.com.rakesh.smart_task_manager.entity.Task;
import com.example.com.rakesh.smart_task_manager.entity.TaskPriority;
import com.example.com.rakesh.smart_task_manager.entity.TaskStatus;
import com.example.com.rakesh.smart_task_manager.entity.User;
import com.example.com.rakesh.smart_task_manager.repository.TaskRepository;
import com.example.com.rakesh.smart_task_manager.repository.UserRepository;

@Service
public class TaskServiceImpl implements TaskService{
	
   private final TaskRepository taskRepository ;
   private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class); 
	
	public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	
    //Get all task
    //Pagination & sorting
   @Override
   public Page<TaskResponseDTO> getAllTask(int page, int size, String sortBy, String direction){
	   
	   String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
	   logger.info("User {} is fetching all tasks with pagination", email);
	   
	   Sort sort = direction.equalsIgnoreCase("desc")? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
	   Pageable pageable = PageRequest.of(page, size, sort);
	   Page<Task> taskPage = taskRepository.findByUserEmail(email,pageable);
	   
	   return taskPage.map(this::mapToResponse);
   }
   
   //Get Task by ID 
   //User specific security
   
   @Override
   public TaskResponseDTO getTaskById(Long id) {
	  
	   
	   String email = SecurityContextHolder.getContext().getAuthentication().getName();
	   
	   logger.info("User {} is fetching task with id: {}", email, id);
	   
	   Task task = taskRepository.findById(id).orElseThrow(()->{
		                                          logger.error("Task not found with id: {}", id); 
	                                              return new RuntimeException("Task Not Found");
	                                            });
	   
       validateTaskOwnerShip(task);
	   return mapToResponse(task);
   }

		
   // Update Task 
   //User specific Update means only task assigned user can update 
  @Override
  @PreAuthorize("hasAnyRole('USER','ADMIN')")// Updating task by user or admin
   public TaskResponseDTO updateTask(Long id, TaskRequestDTO request) {
	  
	   String email = SecurityContextHolder.getContext().getAuthentication().getName();
	   
	   logger.info("User {} is updating task with id: {}", email, id);
	   
	   Task existingTask = taskRepository.findById(id).orElseThrow(()->{
		                                                   logger.error("Task not Found with id: {}", id);
	                                                       return new RuntimeException("Task not found");
	                                                    });
	 
	   validateTaskOwnerShip(existingTask);
	   
	   existingTask.setTitle(request.getTitle());
	   existingTask.setDescription(request.getDescription());
	   existingTask.setStatus(request.getStatus());
	   existingTask.setPriority(request.getPriority());
	   existingTask.setDueDate(request.getDueDate());
	   
	   Task updateTask = taskRepository.save(existingTask);
	   
	   logger.info("Task updated Successfully with id: {}", id);
	   
	   return mapToResponse(updateTask);
	 
   }
   //Delete Task
  //protect task deletion by unassigned task user i.e assigned user can delete
   
   @Override
   
   public void deleteTask(Long id) {
	   
	   String email = SecurityContextHolder.getContext().getAuthentication().getName();
	   
	   logger.warn("User {} is deleting task with id: {}", email, id);
	   
	   Task task = taskRepository.findById(id).orElseThrow(()-> { 
		                                           logger.error("Task not found for deletion with id: {}", id); 
	                                               return new RuntimeException("Task Not Found");
	                                            });

	   validateTaskOwnerShip(task);
	   
	   taskRepository.delete(task);
	   
	   logger.warn("Task deleted successfully with id: {}", id);
   }
   
   //find Task by Status
   
   @Override
   public List<TaskResponseDTO> getTaskByStatus(TaskStatus status){
	   List<Task> tasks = taskRepository.findByStatus(status);
	   return tasks.stream().map(this::mapToResponse).collect(Collectors.toList());
   }
   
   //Find Tasks by Priority
   @Override
   public List<TaskResponseDTO> getTaskByPriority(TaskPriority priority){
	   List<Task> tasks = taskRepository.findByPriority(priority);
	   
	   return tasks.stream().map(this::mapToResponse).collect(Collectors.toList());
   }
		
		
		
	//ENTITY -> Response DTO Mapping
   
	private TaskResponseDTO mapToResponse(Task task) {
		TaskResponseDTO response = new TaskResponseDTO();
		response.setId(task.getId());
		response.setTitle(task.getTitle());
		response.setDescription(task.getDescription());
		response.setStatus(task.getStatus());
		response.setPriority(task.getPriority());
		response.setCreatedAt(task.getCreatedAt());
		response.setDueDate(task.getDueDate());
		response.setCreatedBy(task.getCreatedBy().getEmail());
		response.setAssignedTo(task.getUser().getEmail());
		
		
		return response;
	}
	
    
	//Create Task by User and admin and Linked task to the Logged-in user  
	@Autowired
	private UserRepository userRepository;
	
	@Override
	@PreAuthorize("hasAnyRole('USER','ADMIN')")// Created task by user or admin
	public TaskResponseDTO createTask(TaskRequestDTO request) {
		String loggedInEmail= SecurityContextHolder.getContext().getAuthentication().getName();
		
		var auth = SecurityContextHolder.getContext().getAuthentication();
		
		boolean isAdmin = auth.getAuthorities().stream().anyMatch(a-> a.getAuthority().equals("ROLE_ADMIN"));
		
		//User user;
		
		//Task Creator
		
		User creator = userRepository.findByEmail(loggedInEmail).orElseThrow(() -> new RuntimeException("Assigned user not found"));
		
		User assignedUser;
		
		//Admin assigned Task
		if(isAdmin && request.getAssignedTo()!= null) {
			logger.info("User {} is creating task: {}", loggedInEmail, request.getAssignedTo());
			
			assignedUser = userRepository.findByEmail(request.getAssignedTo()).orElseThrow(() -> new RuntimeException("Assigned user not found"));
			
		}
		else {
			assignedUser = creator;
		}

		Task task = new Task();
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		task.setStatus(request.getStatus());
		task.setPriority(request.getPriority());
		task.setDueDate(request.getDueDate());
		
		task.setUser(assignedUser);// Assigned To 
		task.setCreatedBy(creator);// Created by
		
		Task savedTask = taskRepository.save(task);
		
		logger.info("Task created by assigned to: {}", creator.getEmail(), assignedUser.getEmail() );
		
		return mapToResponse(savedTask);
	}
	
//	Create Ownership Validation
	
	private void validateTaskOwnerShip(Task task) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
        
		if(!isAdmin && !task.getUser().getEmail().equals(email)) {
			
			logger.error("Unauthorized access by user {} on task {}", email, task.getId());
			
			throw new RuntimeException("Access Denied");
		}
	
	
	
	}
	//Task Filtering API
	
	@Override
	public Page<TaskResponseDTO> filterTasks(TaskStatus status, TaskPriority priority, Pageable pageable){
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		logger.info("User {} is filtering tasks with status: {} and priority: {}", email, status, priority );
		
		Page<Task> tasks;
		
		if(status != null && priority != null) {
			tasks = taskRepository.findByUserEmailAndStatusAndPriority(email, status, priority, pageable);
			
		}
		else if(status != null) {
			tasks = taskRepository.findByUserEmailAndStatus(email, status, pageable);
				
			
		}
		else if(priority!=null) {
			tasks = taskRepository.findByUserEmailAndPriority(email, priority, pageable);
			
		}
		else {
			tasks = taskRepository.findByUserEmail(email, pageable);
		}
	return tasks.map(this::mapToResponse);
	}
	
	//Stats API
	
	@Override
	public TaskStatsDTO getTaskStats() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		 long total = taskRepository.countByUserEmail(email);
		 long completed = taskRepository.countByUserEmailAndStatus(email, TaskStatus.COMPLETED);
		 long pending = taskRepository.countByUserEmailAndStatus(email, TaskStatus.PENDING);
		 long inProgress = taskRepository.countByUserEmailAndStatus(email, TaskStatus.IN_PROGRESS);
		 
		 return new TaskStatsDTO(total, completed, pending, inProgress);
	}
	// Admin Stats
	@Override
	public AdminStatsDTO getAdminStats() {
		long totalTasks = taskRepository.count();
		long totalUsers = userRepository.count();
		
		List<UserTaskCountDTO> tasksPerUser = taskRepository.countTasksPerUser();
		return new AdminStatsDTO(totalTasks, totalUsers, tasksPerUser);
	}
	
}





	


