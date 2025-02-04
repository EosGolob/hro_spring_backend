package com.example.demo.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.NotificationResponseDto;
import com.example.demo.entity.Notification;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.NotificationService;

@Service
public class NotificationServiceImple implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Override
	public void notifyAdminNewEmployee(Long employeeId) {
		String notificationMessage = "A new employee with ID " + employeeId + " has been created.";
		Notification notification = new Notification(employeeId, notificationMessage, false);
		notificationRepository.save(notification);
	}

	@Override
	public ResponseEntity<NotificationResponseDto> showNotification() {
		List<Notification> notifications = notificationRepository.findAll();
		long countOfUnreadMessages = notificationRepository.countByIsRead(false);
		markAllAsReadResponse();
		NotificationResponseDto response = new NotificationResponseDto(notifications, countOfUnreadMessages);
		return ResponseEntity.ok(response);
	}

	private void markAllAsReadResponse() {
		List<Notification> notifications = notificationRepository.findAll();
		notifications.forEach(notification -> {
			if (!notification.isRead()) {
				notification.setRead(true);
			}
		});
		notificationRepository.saveAll(notifications);
	}

	@Override
	public long countReadValue() {
		return notificationRepository.countByIsRead(false);
	}

	@Override
	public ResponseEntity<NotificationResponseDto> markAllNotificationFalseToTrue() {
		List<Notification> notifications = notificationRepository.findAll();
		notifications.forEach(notification -> {
			if (!notification.isRead()) {
				notification.setRead(true);
			}
		});

		notificationRepository.saveAll(notifications);
		notifications.sort((n1, n2) -> n2.getId().compareTo(n1.getId()));

		NotificationResponseDto responseDto = new NotificationResponseDto();
		responseDto.setNotifications(notifications);
		return new ResponseEntity<NotificationResponseDto>(responseDto, HttpStatus.OK);
	}

}
