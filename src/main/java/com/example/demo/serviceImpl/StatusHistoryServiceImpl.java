package com.example.demo.serviceImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.StatusHistory;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.StatusHistoryRepository;
import com.example.demo.service.StatusHistoryService;

@Service
public class StatusHistoryServiceImpl implements StatusHistoryService {

	@Autowired
	private StatusHistoryRepository statusHistoryRepository;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public void createInitialStatus(Employee employee) {

		StatusHistory initialStatus = new StatusHistory();
		initialStatus.setId(generateUniqueId());
		initialStatus.setEmployee(employee);
		initialStatus.setStatus("Active");
		LocalDateTime currentDateTime = LocalDateTime.now();
	    Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		initialStatus.setChangesDateTime(currentDate);
		statusHistoryRepository.save(initialStatus);

//	        employee.setStatus(null);

	}

	private Long generateUniqueId() {
		return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
	}

	@Override
	public void trackStatusChange(Employee employee, String newStatus,String responseSubmitbyName) {

		StatusHistory latestStatusHistory = statusHistoryRepository
				.findTopByEmployeeOrderByChangesDateTimeDesc(employee);
		if (latestStatusHistory != null && !latestStatusHistory.getStatus().equals(newStatus)) {
			StatusHistory statusHistory = new StatusHistory();
			statusHistory.setEmployee(employee);
			statusHistory.setStatus(newStatus);
			statusHistory.setHrName(responseSubmitbyName);
			LocalDateTime currentDateTime = LocalDateTime.now();
		    Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
			statusHistory.setChangesDateTime(currentDate);
			statusHistoryRepository.save(statusHistory);
			employeeRepository.save(employee);
		}

	}

	@Override
	public void trackStatusChange2(Employee employee, String newStatus, String responseSubmitbyName,
			String updateRemrks) {
		// TODO Auto-generated method stub
		StatusHistory latestStatusHistory = statusHistoryRepository
				.findTopByEmployeeOrderByChangesDateTimeDesc(employee);
		if (latestStatusHistory != null && !latestStatusHistory.getStatus().equals(newStatus)) {
			StatusHistory statusHistory = new StatusHistory();
			statusHistory.setEmployee(employee);
			statusHistory.setStatus(newStatus);
			statusHistory.setHrName(responseSubmitbyName);
			statusHistory.setRemarksOnEveryStages(updateRemrks);
			LocalDateTime currentDateTime = LocalDateTime.now();
		    Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
			statusHistory.setChangesDateTime(currentDate);
			statusHistoryRepository.save(statusHistory);
			employeeRepository.save(employee);
		
	}
	}

}
