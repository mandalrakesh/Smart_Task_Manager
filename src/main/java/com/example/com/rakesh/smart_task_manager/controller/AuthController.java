package com.example.com.rakesh.smart_task_manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.com.rakesh.smart_task_manager.dto.AuthRequestDTO;
import com.example.com.rakesh.smart_task_manager.dto.AuthResponseDTO;
import com.example.com.rakesh.smart_task_manager.entity.User;
import com.example.com.rakesh.smart_task_manager.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}
	
	//Register API
	
	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody AuthRequestDTO request){
		User user = authService.register(request);
		
		return ResponseEntity.ok(user);
	}
	
	//Log in API
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request){
		AuthResponseDTO response = authService.login(request);
		return ResponseEntity.ok(response);
	}
	

}
