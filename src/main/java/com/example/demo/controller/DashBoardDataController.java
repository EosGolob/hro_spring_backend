package com.example.demo.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmployeeDashBoardDto;
import com.example.demo.serviceImpl.EmployeeServiceDashBoardImpl;

@RestController
@RequestMapping("/api/dashBord")
//@CrossOrigin(origins = {"http://localhost:3001","http://20.193.159.186:3001"})
@CrossOrigin(originPatterns = "*")
public class DashBoardDataController {

	@Autowired
	private EmployeeServiceDashBoardImpl employeeServiceDashBoardImpl;

	@GetMapping("/total-Count")
	public ResponseEntity<EmployeeDashBoardDto> getTotalCount(@RequestParam(required = false) Integer month,
			@RequestParam(required = false) Integer year) {
		Long totalCount = employeeServiceDashBoardImpl.getTotalCountEmp(month,year);
		LocalDate localDate = LocalDate.of(2024, 10, 7);
		Long totalCountOnDateWise = employeeServiceDashBoardImpl.getTotalCountEmpDayWise(localDate);
		Map<String, Long> managerStatusCount = employeeServiceDashBoardImpl.totalSelectedCount(month,year);
		EmployeeDashBoardDto employeeDashBoardDto = new EmployeeDashBoardDto();
		employeeDashBoardDto.setTotalEmp(totalCount);
		employeeDashBoardDto.setRegisterCountOnDateWise(totalCountOnDateWise);
		employeeDashBoardDto.setHiredCount(managerStatusCount.get("Select"));
		employeeDashBoardDto.setRejectedCount(managerStatusCount.get("Reject"));
		employeeDashBoardDto.setInProcessApplicationCount(managerStatusCount.get("NULL"));
		return new ResponseEntity<>(employeeDashBoardDto, HttpStatus.OK);
	}
	
	

	@GetMapping("/total-Count-today")
	public ResponseEntity<Long> getTotalCountDay(@RequestParam("date") Date date) {
		LocalDate localDate = date.toLocalDate();
		long totalCount = employeeServiceDashBoardImpl.getTotalCountEmpDayWise(localDate);
		return ResponseEntity.ok(totalCount);
	}

	@GetMapping("/count-by-job-profile-and-gender")
	public ResponseEntity<List<Map<String, Object>>> getCountByJobProfileAndGender() {
		List<Map<String, Object>> counts = employeeServiceDashBoardImpl.getCountByJobProfileAndGender();
		return new ResponseEntity<>(counts, HttpStatus.OK);
	}

	@GetMapping("/groupBySource")
	public ResponseEntity<List<Map<String, Object>>> getSourceCount() {
		List<Map<String, Object>> sourceCount = employeeServiceDashBoardImpl.getCountBySource();
		return ResponseEntity.ok(sourceCount);
	}
	
   @GetMapping("/groupByJobProfile")
   public ResponseEntity<List<Map<String,Object>>> getJobProfileCount(){
    List<Map<String,Object>> jobCount = employeeServiceDashBoardImpl.getCountByJobProfile();
	return ResponseEntity.ok(jobCount);
   }
   
   
   @GetMapping("/dayWiseCount")
   public ResponseEntity<List<Map<String, Object>>> getApplicationCountDayWise(){ 
	   List<Map<String, Object>> day = employeeServiceDashBoardImpl.getAppCountDayWise();
	return ResponseEntity.ok(day);
	   
   }
   
//   @GetMapping("/monthWiseCount")
//   public ResponseEntity<List<Map<String, Object>>> getApplicationCountMonthWise(){ 
//	   List<Map<String, Object>> day = employeeServiceDashBoardImpl.getAppCountMonthWise();
//	return ResponseEntity.ok(day);
//	   
//   }
   @GetMapping("/monthWiseCount")
   public ResponseEntity<List<Map<String, Object>>> getApplicationCountMonthWise(
           @RequestParam(required = false) Integer year,
           @RequestParam(required = false) Integer month) {
       
       List<Map<String, Object>> day = employeeServiceDashBoardImpl.getAppCountMonthWise(year, month);
       return ResponseEntity.ok(day);
   }

   @GetMapping("/yearWiseCount")
   public ResponseEntity<List<Map<String, Object>>> getApplicationCountYearWise(){ 
	   List<Map<String, Object>> day = employeeServiceDashBoardImpl.getAppCountYearWise();
	return ResponseEntity.ok(day);
	   
   }
   
   @GetMapping("/count-on-job-Profile")
   public ResponseEntity<List<Map<String ,Object>>> selectRejectCountOnJobPrfile(){
	   List<Map<String,Object>> response = employeeServiceDashBoardImpl.getSelectAndRejectCount();
	   return ResponseEntity.ok(response);
   }
}
