package com.example.com.rakesh.smart_task_manager.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.com.rakesh.smart_task_manager.dto.UserTaskCountDTO;
import com.example.com.rakesh.smart_task_manager.entity.Task;
import com.example.com.rakesh.smart_task_manager.entity.TaskPriority;
import com.example.com.rakesh.smart_task_manager.entity.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{
	List<Task> findByStatus(TaskStatus status);
	List<Task> findByPriority(TaskPriority priority);
	Page<Task> findByUserEmail(String email, Pageable pageable);
	Page<Task> findByUserEmailAndStatus(String email, TaskStatus status, Pageable pageable);
	Page<Task> findByUserEmailAndPriority(String email, TaskPriority priority, Pageable pageable);
	Page<Task> findByUserEmailAndStatusAndPriority(String email, TaskStatus status, TaskPriority priority, Pageable pageable);
	long countByUserEmail(String email);
	long countByUserEmailAndStatus(String email, TaskStatus status);
	
	@Query("SELECT new com.example.com.rakesh.smart_task_manager.dto.UserTaskCountDTO(t.user.email,COUNT(t))"+ "FROM Task t GROUP BY t.user.email")
	List<UserTaskCountDTO> countTasksPerUser();

}
