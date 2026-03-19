package com.example.com.rakesh.smart_task_manager.entity;
import jakarta.persistence.*;
import jakarta.persistence.EnumType;

//@Enumerated(EnumType.STRING)
public enum TaskPriority {
	LOW(1),
	MEDIUM(2),
	HIGH(3);
	
	private final int level;

	TaskPriority(int level) {
		this.level=level;
	}

	public int getLevel() {
		return level;
	}

}
