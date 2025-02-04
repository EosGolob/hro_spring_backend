package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import java.io.ByteArrayOutputStream;
import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.EmployeeExcelReportDto;
import com.example.demo.dto.EmployeeExcelReportInSequenceDto;
import com.example.demo.dto.EmployeeSearchDto;
import com.example.demo.entity.InterviewProcesses;
import com.example.demo.enums.RemarksType;

import jakarta.servlet.http.HttpServletResponse;

public interface EmployeeService {

//	EmployeeDto createEmployee(EmployeeDto employeeDto);

	EmployeeDto getEmployeeById(Long employeeId);

	List<EmployeeDto> getAllEmployees();

//	EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee, MultipartFile file) throws IOException;

	void deleteEmployee(Long employeeId);

//	EmployeeDto createEmployee(EmployeeDto employeeDto, MultipartFile file, String path) throws IOException;

//	void assignInterviewProcessAndUpdateStatus(Long employeeId, InterviewProcesses interviewProcesses, String newStatus);
	void assignInterviewProcessAndUpdateStatusTestCheck(Long employeeId, InterviewProcesses interviewProcesses,
			String newStatus, String remarks);

	boolean checkDuplicateEmailAndAddharNo(String email, String aadhaarNumber);

	List<EmployeeDto> getAllScheduleInterview();

	List<EmployeeDto> getAllHrResponseValue();

//	List<EmployeeDto> getAllHdfcResponseValue();
//	 
//	List<EmployeeDto> getAllIciciResponseValue();
//	
//	List<EmployeeDto> getAllMisResponseValue();

	List<EmployeeDto> getAllResponseValueOnProcessType(String role);

	List<EmployeeDto> getEmployeeWithSelectedValuefiled();

	List<EmployeeDto> getEmpDetailsInfoById(Long employeeId);

	List<EmployeeDto> getAllRejectedEmp();

	List<EmployeeDto> getAllApprovedEmp();

	List<EmployeeDto> getHrRejectedEmp();

//	EmployeeDto updateEmployeeHrRejectedScreeningResponse(Long employeeId, String reSetHrField, String responseSubmitByName);

	EmployeeDto updateEmployeeHrResponseStatus(Long employeeId, String newStatus, String responseSubmitbyName);

	EmployeeDto updateEmployeeHrResponseStatus2(Long employeeId, String newStatus, String responseSubmitbyName,
			String updateRemrks);

	EmployeeDto updateEmployeeMrResponseStatusTestCheck(Long employeeId, String newStatus, String responseSubmitbyname,
			String remarks);

//	EmployeeDto updateStatus(Long employeeId, String newStatus);

	EmployeeDto updateEmployeeStatus(Long employeeId, String newStatus);

	EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee);

	EmployeeDto updateEmployeeHrRejectedScreeningResponse(Long employeeId, String reSetHrField, String newStatus,
			String responseSubmitByName);

	List<EmployeeSearchDto> getEmployeeByFullName();

	void updateEmployeeRemarks(Long employeeId, String remarks, RemarksType profile, String jobProfile);

	Long getTotalCountEmp();
	
	List<EmployeeExcelReportDto> getEmployeesDumpReportData(LocalDate startDate, LocalDate endDate);
	
	List<EmployeeExcelReportInSequenceDto> getEmployeesDumpReportData(Date startDate, Date endDate);
	
	void exportToExcel(List<EmployeeExcelReportInSequenceDto> data, HttpServletResponse response) throws IOException;
	
	ByteArrayOutputStream exportToRawExcel(List<EmployeeExcelReportDto> data) throws IOException;

	/**
	 * void updateEmployeeRemarksHrAndManagerchecks(Long employeeId, String
	 * hrRemarks, String managerRemaks, String profileScreenRemark, String
	 * updateName);
	 */

	/**
	 * void updateEmployeeRemarksHrAndManager(Long employeeId, String
	 * hrRemarks,String managerRemaks,String profileScreenRemark );
	 */
	
	/**
	 * EmployeeDto updateEmployeeMrResponseStatus(Long employeeId, String newStatus, String responseSubmitbyName);
	 */

}
