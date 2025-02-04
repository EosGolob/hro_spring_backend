package com.example.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.EmployeeExcelReportDto;
import com.example.demo.dto.EmployeeExcelReportInSequenceDto;
import com.example.demo.dto.EmployeeSearchDto;
import com.example.demo.entity.InterviewProcesses;
import com.example.demo.enums.RemarksType;
import com.example.demo.exception.InvalidInputException;
import com.example.demo.service.EmployeeService;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(originPatterns = "*")
//@CrossOrigin(origins = {"http://localhost:3001","http://20.193.159.186:3001"})
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private EmployeeService employeeService;

	@Value("${file.upload-dir}")
	private String path;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}


	@GetMapping("{id}")
	public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable("id") Long employeeId) {
		EmployeeDto employeeDto = employeeService.getEmployeeById(employeeId);

		return ResponseEntity.ok(employeeDto);

	}

	@GetMapping("/searchByNameAPI")
	public ResponseEntity<List<EmployeeSearchDto>> getEmployeeByFullName() {
		List<EmployeeSearchDto> employeeDto = employeeService.getEmployeeByFullName();
		return ResponseEntity.ok(employeeDto);

	}

	@GetMapping
	public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
		List<EmployeeDto> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);

	}

	@GetMapping("/getAllEmp")
	public ResponseEntity<List<EmployeeDto>> getAllEmp() {
		List<EmployeeDto> employees = employeeService.getEmployeeWithSelectedValuefiled();
		return ResponseEntity.ok(employees);

	}

	// Build update Employee REST API
	@PutMapping("{id}")
	public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable("id") Long employeeId,
			@RequestBody EmployeeDto updatedEmployee) {
		EmployeeDto employeeDto = employeeService.updateEmployee(employeeId, updatedEmployee);
		return ResponseEntity.ok(employeeDto);
	}

	/**
	 * @PutMapping("{id}") public ResponseEntity<EmployeeDto>
	 * updateEmployee(@PathVariable("id") Long employeeId, @ModelAttribute
	 * EmployeeDto updatedEmployee, @RequestParam MultipartFile file) throws
	 * IOException { EmployeeDto updatedEmployeeDto =
	 * employeeService.updateEmployee(employeeId, updatedEmployee, file); return
	 * ResponseEntity.ok(updatedEmployeeDto); }
	 */

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
		employeeService.deleteEmployee(employeeId);
		return ResponseEntity.ok("Employee deleted successfully");
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<EmployeeDto> updateEmployeeStatus(@PathVariable("id") Long employeeId,
			@RequestParam String newStatus) {
		EmployeeDto updatedEmployee = employeeService.updateEmployeeStatus(employeeId, newStatus);
		return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	}

	/**
	 * @GetMapping("/employeesProcessesInterview") public
	 * ResponseEntity<List<EmployeeDto>>
	 * getEmployeesByInterviewProcessStatus(@RequestParam String status) {
	 * List<EmployeeDto> employees =
	 * employeeService.getEmployeesByInterviewProcessStatus(status); return
	 * ResponseEntity.ok(employees); }
	 */

	@GetMapping("/employees-schedule-interview")
	public ResponseEntity<List<EmployeeDto>> employeeScheduleInterview() {
		List<EmployeeDto> employees = employeeService.getAllScheduleInterview();
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/managerResponeField")
	public ResponseEntity<List<EmployeeDto>> managerResponseField() {
		List<EmployeeDto> employees = employeeService.getAllHrResponseValue();
		return ResponseEntity.ok(employees);
	}

	/**
	 * @GetMapping("/managerHdfcResponeField") public
	 * ResponseEntity<List<EmployeeDto>> hdfcmanagerResponseField() {
	 * List<EmployeeDto> employees = employeeService.getAllHdfcResponseValue();
	 * return ResponseEntity.ok(employees); }
	 * 
	 * @GetMapping("/managerIciciResponeField") public
	 * ResponseEntity<List<EmployeeDto>> icicmanagerResponseField() {
	 * List<EmployeeDto> employees = employeeService.getAllIciciResponseValue();
	 * return ResponseEntity.ok(employees); }
	 * 
	 * @GetMapping("/managerMisResponeField") public
	 * ResponseEntity<List<EmployeeDto>> mismanagerResponseField() {
	 * List<EmployeeDto> employees = employeeService.getAllMisResponseValue();
	 * return ResponseEntity.ok(employees); }
	 */
	@GetMapping("/getAllEmployeeOnManagersPage/{role}")
	public ResponseEntity<List<EmployeeDto>> managerPageEmployeedetailsOnRole(@PathVariable String role) {
		List<EmployeeDto> employees = employeeService.getAllResponseValueOnProcessType(role);
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/rejectedEmpdetails")
	public ResponseEntity<List<EmployeeDto>> rejectedEmployees() {
		List<EmployeeDto> employees = employeeService.getAllRejectedEmp();
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/approvedEmpdetails")
	public ResponseEntity<List<EmployeeDto>> approvedEmployees() {
		List<EmployeeDto> employees = employeeService.getAllApprovedEmp();
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/hrRejectedEmpDetails")
	public ResponseEntity<List<EmployeeDto>> hrRejectedEmployeesDetails() {
		List<EmployeeDto> employees = employeeService.getHrRejectedEmp();
		return ResponseEntity.ok(employees);
	}

	@PutMapping("/{id}/mRResponse")
	public ResponseEntity<EmployeeDto> updateEmployeeMrRespone(@PathVariable("id") Long employeeId,
			@RequestBody Map<String, String> requestBody) {
		String newStatus = requestBody.get("newStatus");
		String responseSubmitbyName = requestBody.get("mrUserName");
		String managerRemarks = requestBody.get("managerRemarks");
		employeeService.updateEmployeeRemarks(employeeId, managerRemarks, RemarksType.MANAGER, null);
		EmployeeDto updatedEmployee = employeeService.updateEmployeeMrResponseStatusTestCheck(employeeId, newStatus,
				responseSubmitbyName, managerRemarks);
		System.out.println("new Status value" + newStatus);
		return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	}

	@PutMapping("/{id}/hrResponse")
	public ResponseEntity<EmployeeDto> updateEmployeehrRespone(@PathVariable("id") Long employeeId,
			@RequestBody Map<String, String> requestBody) {
		String newStatus = requestBody.get("newStatus");
		String responseSubmitbyName = requestBody.get("hrUserName");
		String profileScreenRemark = requestBody.get("profileScreenRemark");

		if (profileScreenRemark == null || profileScreenRemark.trim().isEmpty()) {
			throw new InvalidInputException("Remarks should not be blank");
		}
		if (newStatus == null || newStatus.trim().isEmpty()) {
			throw new InvalidInputException("Action should not be blank");
		}
		employeeService.updateEmployeeRemarks(employeeId, profileScreenRemark, RemarksType.PROFILE, null);
//		EmployeeDto updatedEmployee = employeeService.updateEmployeeHrResponseStatus(employeeId, newStatus,
//				responseSubmitbyName);
		EmployeeDto updatedEmployee = employeeService.updateEmployeeHrResponseStatus2(employeeId, newStatus,
				responseSubmitbyName, profileScreenRemark);
		return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	}

	@PostMapping("/{employeeId}/interview-process")
	public ResponseEntity<Void> assignInterviewProcess(@PathVariable Long employeeId,
			@RequestBody InterviewProcesses interviewProcesses) {
		String newStatus = interviewProcesses.getStatus();
		String remarks = interviewProcesses.getRemarks();
		String processName = interviewProcesses.getProcessName();
		String jobProfile = interviewProcesses.getJobProfile();

		if (remarks == null || remarks.trim().isEmpty()) {
			throw new InvalidInputException("Remarks is required.");
		}

		if (processName == null || processName.trim().isEmpty()) {
			throw new InvalidInputException("Process name is required.");
		}
		if (jobProfile == null || jobProfile.trim().isEmpty()) {
			throw new InvalidInputException("job Profile name is required.");
		}
		employeeService.updateEmployeeRemarks(employeeId, remarks, RemarksType.SCHEDULE, jobProfile);
//		employeeService.assignInterviewProcessAndUpdateStatus(employeeId, interviewProcesses, newStatus);
		employeeService.assignInterviewProcessAndUpdateStatusTestCheck(employeeId, interviewProcesses, newStatus,
				remarks);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/{employeeId}/reinterview-process")
	public ResponseEntity<Void> ReInterviewProcess(@PathVariable Long employeeId,
			@RequestBody InterviewProcesses interviewProcesses) {
		String newStatus = interviewProcesses.getStatus();
		String remarks = interviewProcesses.getRemarks();
		String processName = interviewProcesses.getProcessName();
		if (processName == null || processName.trim().isEmpty()) {
			throw new InvalidInputException("Select the process");
		}
		employeeService.assignInterviewProcessAndUpdateStatusTestCheck(employeeId, interviewProcesses, newStatus,
				remarks);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/rejectByhrReInterviewSchedule")
	public ResponseEntity<EmployeeDto> reInterviewForRejectedByHr(@PathVariable("id") Long employeeId,
			@RequestBody Map<String, String> requestBody) {
		String reSetHrField = requestBody.get("resetHrResponse");
		String responseSubmitByName = requestBody.get("userName");
		String newStatus = "Re Screening By Hr";
		EmployeeDto updateEmployee = employeeService.updateEmployeeHrRejectedScreeningResponse(employeeId, reSetHrField,
				newStatus, responseSubmitByName);
		return new ResponseEntity<>(updateEmployee, HttpStatus.OK);

	}

	@GetMapping("/empDetailsInfo/{id}")
	public ResponseEntity<List<EmployeeDto>> empDetailsInfo(@PathVariable("id") Long employeeId) {
		List<EmployeeDto> employees = employeeService.getEmpDetailsInfoById(employeeId);
		return ResponseEntity.ok(employees);

	}

	@GetMapping("/reportData")
	public ResponseEntity<byte[]> dumpReportData(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws IOException {
		List<EmployeeExcelReportDto> response = employeeService.getEmployeesDumpReportData(startDate, endDate);
		ByteArrayOutputStream byteArrayOutputStream = employeeService.exportToRawExcel(response);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=Employee_Report.xlsx");
		return new ResponseEntity<>(byteArrayOutputStream.toByteArray(), headers, HttpStatus.OK);
	}

	@GetMapping("/report")
	public List<EmployeeExcelReportInSequenceDto> getEmployeesReport(
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return employeeService.getEmployeesDumpReportData(startDate, endDate);
	}

	@GetMapping("/export")
	public void exportToExcel(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, HttpServletResponse response)
			throws IOException {
		List<EmployeeExcelReportInSequenceDto> data = employeeService.getEmployeesDumpReportData(startDate, endDate);
		employeeService.exportToExcel(data, response);
	}

}
