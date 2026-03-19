package com.example.com.rakesh.smart_task_manager.service;
//import org.springframework.security.core.userdetails.User;

import com.example.com.rakesh.smart_task_manager.dto.AuthRequestDTO;
import com.example.com.rakesh.smart_task_manager.dto.AuthResponseDTO;
import com.example.com.rakesh.smart_task_manager.entity.User;

public interface AuthService {
	User register(AuthRequestDTO request);
	
	AuthResponseDTO login(AuthRequestDTO request);

}
