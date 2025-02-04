package com.example.demo.service;

import org.springframework.http.ResponseEntity;

import com.example.demo.dto.NotificationResponseDto;


public interface NotificationService {
 
	public void notifyAdminNewEmployee(Long employeeId);
	ResponseEntity<NotificationResponseDto> showNotification();
	long countReadValue();
	ResponseEntity<NotificationResponseDto> markAllNotificationFalseToTrue();
	
}
