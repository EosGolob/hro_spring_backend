package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NotificationResponseDto;
import com.example.demo.serviceImpl.NotificationServiceImple;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/api")
//@CrossOrigin(origins = {"http://localhost:3001","http://20.193.159.186:3001"})
public class NotificationController {

	@Autowired
	private NotificationServiceImple notificationServiceImple;

	@GetMapping("/show-notification")
	public ResponseEntity<NotificationResponseDto> getAllNotification() {
		return notificationServiceImple.showNotification();
	}

	@GetMapping("/count/unread")
	public long getCountValues() {
		return notificationServiceImple.countReadValue();
	}

	@PostMapping("/marksAsReadAndShowNotification")
	public ResponseEntity<NotificationResponseDto> markAllNotificationRead() {
		return notificationServiceImple.markAllNotificationFalseToTrue();

	}

}
