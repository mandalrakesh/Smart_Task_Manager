package com.example.com.rakesh.smart_task_manager.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.com.rakesh.smart_task_manager.dto.AuthRequestDTO;
import com.example.com.rakesh.smart_task_manager.dto.AuthResponseDTO;
import com.example.com.rakesh.smart_task_manager.entity.Role;
import com.example.com.rakesh.smart_task_manager.entity.User;
import com.example.com.rakesh.smart_task_manager.repository.UserRepository;
import com.example.com.rakesh.smart_task_manager.security.jwt.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);//For Logging 
	
	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}
	//Register User
	@Override
	public User register(AuthRequestDTO request) {
		
		logger.info("Register request received for email: {}", request.getEmail());// Register request receive
		// Check If user already exists
		if(userRepository.findByEmail(request.getEmail()).isPresent()) {
			logger.warn("User already exists with email: {}", request.getEmail());// User already exist
			throw new RuntimeException("User already Exists");
		}
		User user = new User();
		user.setName(request.getName());
		//user.setuser_Name(request.getUser_Name());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		user.setRole(Role.USER);//Assign Role to specific user
		
		User savedUser = userRepository.save(user);
		
		logger.info("User registered successfully with email: {}", request.getEmail());// User registered successfully
		
		return savedUser;
	}
	//Login User
	@Override
	public AuthResponseDTO login(AuthRequestDTO request) {
		
		logger.info("Login attempt for email: {}", request.getEmail());//Login attempt
		
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->{
			logger.error("User not found: {}", request.getEmail()); // User not found
		
		    return new RuntimeException("User not found");
		});
				
		if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			
			logger.warn("Invalid password attempt for email: {}", request.getEmail());// Entered wrong password 
			throw new RuntimeException("Invalid Password");
		}
		// Generate JWT token
		String token  = jwtUtil.generateToken(user.getEmail());
		
		logger.info("Userlogged in successfully: {}", request.getEmail());// login successfully
		return new AuthResponseDTO(token);
	}
	

}
