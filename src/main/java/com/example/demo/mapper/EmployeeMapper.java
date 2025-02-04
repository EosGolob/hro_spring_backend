package com.example.demo.mapper;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.entity.Employee;

public class EmployeeMapper {
	
	public static  EmployeeDto mapToEmployeeDto(Employee employee) {
		return new EmployeeDto(
				employee.getId(),
				employee.getFullName(),
				employee.getEmail(),
				employee.getJobProfile(),
				employee.getQualification(),
				employee.getMobileNo(),
				employee.getPermanentAddress(),
				employee.getCurrentAddress(),
				employee.getGender(),
				employee.getPreviousOrganisation(),
				employee.getDob(),
				employee.getMaritalStatus(),
				employee.getRefferal(),
				employee.getAadharFilename(),
				employee.getInitialStatus(),
				employee.getProcessesStatus(),
				employee.getHrStatus(),
				employee.getManagerStatus(),
				employee.getStatusHistories(),
				employee.getInterviewProcesses(),
				employee.getAadhaarNumber(),
				employee.getLanguages(),
				employee.getExperience(),
				employee.getSource(),
				employee.getSubSource(),
				employee.getCreationDate(),
				employee.getLastInterviewAssin(),
				employee.getReMarksByHr(),
				employee.getReMarksByManager(),
				employee.getProfileScreenRemarks(),
				employee.getWorkExp()
				);
	}

	
  public static Employee mapToEmployee(EmployeeDto employeeDto) {
	  return new Employee(
			  employeeDto.getId(),
			  employeeDto.getFullName(),
			  employeeDto.getEmail(),
			  employeeDto.getJobProfile(),
			  employeeDto.getQualification(),
			  employeeDto.getMobileNo(),
			  employeeDto.getPermanentAddress(),
			  employeeDto.getCurrentAddress(),
			  employeeDto.getGender(),
			  employeeDto.getPreviousOrganisation(),
			  employeeDto.getDob(),
			  employeeDto.getMaritalStatus(),
			  employeeDto.getRefferal(),  		 
			  employeeDto.getAadhaarNumber(),
			  employeeDto.getLanguages(),
			  employeeDto.getExperience(),
			  employeeDto.getSource(),
			  employeeDto.getSubSource(),
			  employeeDto.getInitialStatus(),
			  employeeDto.getProcessesStatus(),
			  employeeDto.getHrStatus(),
			  employeeDto.getManagerStatus(),
			  employeeDto.getStatusHistories(),
			  employeeDto.getInterviewProcesses(),
			  employeeDto.getAadharFilename(),
			  employeeDto.getCreationDate(),
			  employeeDto.getLastInterviewAssin(),
			  employeeDto.getReMarksByHr(),
			  employeeDto.getReMarksByManager(),
			  employeeDto.getProfileScreenRemarks(),
			  employeeDto.getWorkExp()
			  );
  }
}
