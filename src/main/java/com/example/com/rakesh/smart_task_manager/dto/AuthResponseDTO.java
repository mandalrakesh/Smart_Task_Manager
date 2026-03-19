package com.example.com.rakesh.smart_task_manager.dto;

public class AuthResponseDTO {
	private String token;

	public AuthResponseDTO(String token) {
		// TODO Auto-generated constructor stub
		this.token = token;
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

}
