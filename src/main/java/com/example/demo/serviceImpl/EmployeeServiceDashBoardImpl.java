package com.example.demo.serviceImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeDashBoardRepository;
import com.example.demo.service.EmployeeServiceDashBoard;

@Service
public class EmployeeServiceDashBoardImpl implements EmployeeServiceDashBoard {

	@Autowired
	private EmployeeDashBoardRepository employeeDashBoardRepository;

	@Override
	public Long getTotalCountEmp(Integer month, Integer year) {
		List<Employee> employeeDto ;
		/**
		= employeeDashBoardRepository.findAll();		
		if(date != null && date.isEmpty()) {
		long empCount = employeeDto.stream().count();
		}else {
			employeeDto.stream().filter(null).count();
		}
		*/
		
		if(month != null && year != null) {
//			LocalDate requestedDate = LocalDate.parse(date);
			employeeDto = employeeDashBoardRepository.totalCountOnDateWise(month,year);
		}else {
			employeeDto = employeeDashBoardRepository.findAll();
		}
		return (long) employeeDto.size();
	}

	@Override
	public Long getTotalCountEmpDayWise(LocalDate date) {
//		return employeeDashBoardRepository.countByCreationDate(date);
		// Start of the day (00:00:00)
		LocalDateTime startOfDay = date.atStartOfDay();

		// End of the day (23:59:59)
		LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

		// Convert LocalDateTime to Timestamp
		Timestamp startTimestamp = Timestamp.valueOf(startOfDay);
		Timestamp endTimestamp = Timestamp.valueOf(endOfDay);

		// Call the repository with the start and end timestamps
		return employeeDashBoardRepository.countByCreationDate(startTimestamp, endTimestamp);
	}

	@Override
	public Map<String, Long> totalSelectedCount(Integer month,Integer year) {
		List<Object[]> result;
		if(month != null && year!= null) {
//			LocalDate requestedDate = LocalDate.parse(date);
			result = employeeDashBoardRepository.countByManagerStatus(month,year);
		}else {
		result = employeeDashBoardRepository.countByManagerStatus();
		}
		
		Map<String, Long> statusCountMap = new HashMap<>();

		for (Object[] row : result) {
			String managerStatus = (String) row[0]; // Manager status
			Long count = (Long) row[1]; // Employee count for that status
			if (managerStatus == null) {
				managerStatus = "NULL"; // You can use any placeholder string like "NULL"
			}
			statusCountMap.put(managerStatus, count);
		}
		return statusCountMap;

//		return employeeDashBoardRepository.countByManagerStatus();
	}

	@Override
	public List<Map<String, Object>> getCountByJobProfileAndGender() {
		List<Object[]> result = employeeDashBoardRepository.countByJobProfileAndGender();
		List<Map<String, Object>> responseList = new ArrayList<>();
		/**
		 * for (Object[] row : result) { JobProfileGenderCountDto dto = new
		 * JobProfileGenderCountDto(); dto.setJobProfile((String) row[0]);
		 * dto.setGender((String) row[1]); dto.setCount((Long) row[2]);
		 * responseList.add(dto); }
		 */
		for (Object[] row : result) {
			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("jobProfile", row[0]); // First column is job profile
			responseMap.put("gender", row[1]); // Second column is gender
			responseMap.put("count", row[2]); // Third column is the count
			responseList.add(responseMap);
		}
		return responseList;
	}

	@Override
	public List<Map<String, Object>> getCountBySource() {
//           Map<Object, List<Employee>> response = employeeDashBoardRepository.findAll().stream()
//        		   .collect(Collectors.groupingBy(emp -> emp.getSource()));
           List<Object[]> results = employeeDashBoardRepository.getSourceCount();  
           List<Map<String, Object>> responseList = new ArrayList<>();
           for (Object[] result : results) {
               Map<String, Object> map = new HashMap<>();
               map.put("source", result[0]);
               map.put("count", result[1]);
               responseList.add(map);
           }
		return responseList;
	}

	@Override
	public List<Map<String, Object>> getCountByJobProfile() {
		// TODO Auto-generated method stub
		List<Object[]> results = employeeDashBoardRepository.getCountAppliedJobProfile();
		 List<Map<String, Object>> responseList = new ArrayList<>();
         for (Object[] result : results) {
             Map<String, Object> map = new HashMap<>();
             map.put("source", result[0]);
             map.put("count", result[1]);
             responseList.add(map);
         }
		return responseList;
	}
    @Override
	public List<Map<String, Object>> getAppCountDayWise() {
		// TODO Auto-generated method stub
    	List<Object[]> result = employeeDashBoardRepository.getDayWiseCount(); 
    	List<Map<String, Object>> responseList = new ArrayList<>() ;
    	 for(Object[] res : result) {
    		 Map<String,Object> map = new HashMap<>();
    		 map.put("Date", res[0]);
    		 map.put("count",res[1]);
    		 responseList.add(map);
    	 }
		return responseList;
	}

    @Override
	public List<Map<String, Object>> getAppCountMonthWise(Integer year, Integer month) {	
    	List<Object[]> result;
    	
    	 if (year != null && month != null) {
    	        result = employeeDashBoardRepository.getMonthWiseCountForYearAndMonth(year, month);
    	    } else {
    	        // Otherwise, return data for all months and years
    	        result = employeeDashBoardRepository.getMonthWiseCount();
    	    }

//    	List<Object[]> result = employeeDashBoardRepository.getMonthWiseCount(); 
    	List<Map<String, Object>> responseList = new ArrayList<>() ;
    	 for(Object[] res : result) {
   		 Map<String,Object> map = new HashMap<>();
   		 map.put("year", res[0]);
   		 map.put("month",res[1]);
   		 map.put("count", res[2]);
   		 responseList.add(map);
   	 }
		return responseList;
    	
    }
    @Override
	public List<Map<String, Object>> getAppCountYearWise() {
    	List<Object[]> result = employeeDashBoardRepository.getYearWiseCount(); 
    	List<Map<String, Object>> responseList = new ArrayList<>() ;
    	 for(Object[] res : result) {
   		 Map<String,Object> map = new HashMap<>();
   		 map.put("year", res[0]);
   		 map.put("count",res[1]);
   		 responseList.add(map);
   	 }
		return responseList;
	}

    @Override
	public List<Map<String, Object>> getSelectAndRejectCount() {
    	List<Object[]> result = employeeDashBoardRepository.getSelectRejectedCount();
    	List<Map<String, Object>> responseList = new ArrayList<>() ;
    	for(Object[] res : result) {
    		Map<String,Object> map = new HashMap<>();
    		map.put("jobTitle", res[0]);
    		map.put("totalApplications",res[1]);
    		map.put("hired", res[2]);
    		map.put("rejected", res[3]);
    		map.put("inProgress", res[4]);
    		responseList.add(map);
    	}
		return responseList;
	}

}
