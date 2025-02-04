package com.example.demo.mapper;

import com.example.demo.dto.EmployeeSearchDto;
import com.example.demo.entity.Employee;

public class EmployeeSearchMappper {
	
	
	public static EmployeeSearchDto mapToEmployeeSearchDto(Employee emp) {
		
		return new EmployeeSearchDto(
				emp.getId(),
				emp.getFullName(),
				emp.getEmail(),
				emp.getInitialStatus(),
				emp.getProcessesStatus(),
				emp.getHrStatus(),
				emp.getManagerStatus(),
				emp.getAadhaarNumber(),
				emp.getLastInterviewAssin(),
				emp.getReMarksByHr(),
				emp.getReMarksByManager(),
				emp.getProfileScreenRemarks());
		
	}

}
