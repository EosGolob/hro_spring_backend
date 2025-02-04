package com.example.demo.service;

import com.example.demo.entity.Employee;

public interface StatusHistoryService {
	  public void createInitialStatus(Employee employee);
	  public void trackStatusChange(Employee employee, String newStatus ,String responseSubmitbyName);
	  public void trackStatusChange2(Employee employee, String newStatus, String responseSubmitbyName,
			String updateRemrks);
}
