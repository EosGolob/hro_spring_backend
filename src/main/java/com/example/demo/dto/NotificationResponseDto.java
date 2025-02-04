package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {
	
	private List<Notification> notifications;
	private long unreadCount;

}
