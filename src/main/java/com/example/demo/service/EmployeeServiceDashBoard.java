package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface EmployeeServiceDashBoard {

	Long getTotalCountEmp(Integer month,Integer year);

	Long getTotalCountEmpDayWise(LocalDate date);

	Map<String, Long> totalSelectedCount(Integer month, Integer year);

	List<Map<String, Object>> getCountByJobProfileAndGender();

	List<Map<String, Object>> getCountBySource();

	List<Map<String, Object>> getCountByJobProfile();

	List<Map<String, Object>> getAppCountDayWise();

//	List<Map<String, Object>> getAppCountMonthWise();

	List<Map<String, Object>> getAppCountYearWise();

	List<Map<String, Object>> getSelectAndRejectCount();

	List<Map<String, Object>> getAppCountMonthWise(Integer year, Integer month);


}
