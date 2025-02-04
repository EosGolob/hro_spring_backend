package com.example.demo.serviceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.EmployeeExcelReportDto;
import com.example.demo.dto.EmployeeExcelReportInSequenceDto;
import com.example.demo.dto.EmployeeSearchDto;
import com.example.demo.dto.StatusHistoryExcelDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.InterviewProcesses;
import com.example.demo.entity.StatusHistory;
import com.example.demo.enums.RemarksType;
import com.example.demo.exception.InvalidStatusException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.EmployeeMapper;
import com.example.demo.mapper.EmployeeSearchMappper;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.InterviewProcessesRepository;
import com.example.demo.repository.ManagerDetailsRepository;
import com.example.demo.repository.StatusHistoryRepository;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.FileService;
import com.example.demo.service.StatusHistoryService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private StatusHistoryService statusHistoryService;

	@Autowired
	private StatusHistoryRepository statusHistoryRepository;

	@Autowired
	private InterviewProcessesRepository interviewProcessesRepository;

	@Autowired
	private ManagerDetailsRepository managerDetailsRepository;

	@Value("${file.upload-dir}")
	private String path;

	private static final String UPLOAD_DIR = "./src/main/resources/static/img";

	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
		this.fileService = fileService;
		this.statusHistoryService = statusHistoryService;
	}

	@Override
	public EmployeeDto getEmployeeById(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(
				() -> new ResourceNotFoundException("Employee is not Exist with the given id" + employeeId));
		return EmployeeMapper.mapToEmployeeDto(employee);
	}

	@Override
	public List<EmployeeSearchDto> getEmployeeByFullName() {
		List<Employee> employees = employeeRepository.findAll();
		if (employees.isEmpty()) {
			throw new ResourceNotFoundException("No employees found with the given name:");
		}

		return employees.stream().sorted((id1, id2) -> Long.compare(id2.getId(), id1.getId()))
				.map(EmployeeSearchMappper::mapToEmployeeSearchDto).collect(Collectors.toList());

	}

	@Override
	public List<EmployeeDto> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		return employees.stream().map((employee) -> EmployeeMapper.mapToEmployeeDto(employee))
				.collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDto> getEmployeeWithSelectedValuefiled() {

		List<Object[]> results = employeeRepository.getEmployeeWithSelectedValue();
		List<EmployeeDto> employees = new ArrayList<>();

		for (Object[] result : results) {
			EmployeeDto employee = new EmployeeDto();
			employee.setId((Long) result[0]);
			employee.setFullName((String) result[1]);
			employee.setEmail((String) result[2]);
			employee.setJobProfile((String) result[3]);
			employee.setMobileNo((Long) result[4]);
			employee.setPermanentAddress((String) result[5]);
			employee.setCurrentAddress((String) result[6]);
			employee.setGender((String) result[7]);
			employee.setCreationDate((Date) result[8]);

			employees.add(employee);
		}
		return employees.stream()
				.sorted((e1, e2) -> Long.compare(e2.getCreationDate().getTime(), e1.getCreationDate().getTime()))
				.collect(Collectors.toList());
	}

	@Override
	public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployee) {
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(
				() -> new ResourceNotFoundException("Employee is not exists with given id : " + employeeId));

		employee.setFullName(updatedEmployee.getFullName());
		employee.setEmail(updatedEmployee.getEmail());
		employee.setJobProfile(updatedEmployee.getJobProfile());
		employee.setQualification(updatedEmployee.getQualification());
		employee.setMobileNo(updatedEmployee.getMobileNo());
		employee.setPermanentAddress(updatedEmployee.getPermanentAddress());
		employee.setCurrentAddress(updatedEmployee.getCurrentAddress());
		employee.setGender(updatedEmployee.getGender());
		employee.setPreviousOrganisation(updatedEmployee.getPreviousOrganisation());
		employee.setDob(updatedEmployee.getDob());
		employee.setMaritalStatus(updatedEmployee.getMaritalStatus());
		employee.setRefferal(updatedEmployee.getRefferal());
		Employee updatedEmployeeObj = employeeRepository.save(employee);
		return EmployeeMapper.mapToEmployeeDto(updatedEmployeeObj);
	}

	@Override
	public void deleteEmployee(Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId).orElseThrow(
				() -> new ResourceNotFoundException("Employee is not exists with given id : " + employeeId));

		employeeRepository.deleteById(employeeId);
	}

	/**
	 * @Override public EmployeeDto updateStatus(Long employeeId, String newStatus)
	 *           { Employee employee =
	 *           employeeRepository.findById(employeeId).orElseThrow(()-> new
	 *           ResourceNotFoundException("Employee is not exists with given id :
	 *           "+ employeeId)); StatusHistory statusHistory = new StatusHistory();
	 *           statusHistory.setEmployee(employee);
	 *           statusHistory.setStatus(newStatus);
	 *           statusHistory.setChangesDateTime(LocalDateTime.now());
	 *           StatusHistory save = StatusHistoryRepository.save(statusHistory);
	 * 
	 *           Employee .setStatus(newStatus);
	 * 
	 *           return employeeRepository.save(emplo);
	 * 
	 *           }
	 */
	private void updateEmployeeStatus(Employee employee) {
		StatusHistory latestStatus = statusHistoryRepository.findTopByEmployeeOrderByChangesDateTimeDesc(employee);
		if (latestStatus != null) {
//	        employee.setStatus(latestStatus.getStatus());
			employee.setInitialStatus(latestStatus.getStatus());
			employeeRepository.save(employee);
		}
	}

	@Override
	public EmployeeDto updateEmployeeStatus(Long employeeId, String newStatus) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
		statusHistoryService.trackStatusChange(employee, newStatus, null);
		return EmployeeMapper.mapToEmployeeDto(employee);
	}

	/**
	 * @Override public void assignInterviewProcessAndUpdateStatus(Long employeeId,
	 *           InterviewProcesses interviewProcesses, String newStatus) { Employee
	 *           employee = employeeRepository.findById(employeeId) .orElseThrow(()
	 *           -> new RuntimeException("Employee not found"));
	 *           interviewProcesses.setEmployee(employee);
	 * 
	 *           assignManagerForInterview(interviewProcesses); String
	 *           processNameUpdateInEmp = interviewProcesses.getProcessName();
	 *           employee.setProcessesStatus(processNameUpdateInEmp);
	 *           employee.setLastInterviewAssin(processNameUpdateInEmp);
	 *           InterviewProcesses savedInterviewProcess =
	 *           interviewProcessesRepository.save(interviewProcesses);
	 *           setStatusHistoryRecored(employeeId, savedInterviewProcess,
	 *           newStatus, employee); }
	 **/

	@Override
	public void assignInterviewProcessAndUpdateStatusTestCheck(Long employeeId, InterviewProcesses interviewProcesses,
			String newStatus, String remarks) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new RuntimeException("Employee not found"));
		interviewProcesses.setEmployee(employee);
		String processNameUpdateInEmp = interviewProcesses.getProcessName();
		employee.setProcessesStatus(processNameUpdateInEmp);
		employee.setLastInterviewAssin(processNameUpdateInEmp);
		InterviewProcesses savedInterviewProcess = interviewProcessesRepository.save(interviewProcesses);
		setStatusHistoryRecoredRemarksChecks(employeeId, savedInterviewProcess, newStatus, employee, remarks);

	}

	@Override
	public boolean checkDuplicateEmailAndAddharNo(String email, String aadhaarNumber) {
		// TODO Auto-generated method stub
		boolean emailExists = employeeRepository.existsByEmail(email);
		boolean addharnoExists = employeeRepository.existsByAadhaarNumber(aadhaarNumber);
		return emailExists || addharnoExists;
	}

	/**
	 * @Override public List<EmployeeDto> getAllScheduleInterview() { List<Employee>
	 *           employees =
	 *           employeeRepository.findEmployeesWithScheduledInterviews(); return
	 *           employees.stream().map((employee) ->
	 *           EmployeeMapper.mapToEmployeeDto(employee))
	 *           .collect(Collectors.toList()); }
	 * @Override public List<EmployeeDto>
	 *           getEmployeesByInterviewProcessStatus(String status) {
	 *           List<EmployeeDto> employees =
	 *           employeeRepository.findByInterviewProcessStatus(status); return
	 *           employees.stream() .map(employee ->
	 *           EmployeeMapper.mapToEmployeeDto(employee))
	 *           .collect(Collectors.toList()); }
	 */

	@Override
	public EmployeeDto updateEmployeeHrResponseStatus(Long employeeId, String newStatus, String responseSubmitbyName) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
		employee.setHrStatus(newStatus);
		statusHistoryService.trackStatusChange(employee, newStatus, responseSubmitbyName);
		return EmployeeMapper.mapToEmployeeDto(employee);
	}

	@Override
	public EmployeeDto updateEmployeeHrResponseStatus2(Long employeeId, String newStatus, String responseSubmitbyName,
			String updateRemrks) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
		if (newStatus == null) {
			throw new InvalidStatusException("Select the status Response");
		}
		employee.setHrStatus(newStatus);
		statusHistoryService.trackStatusChange2(employee, newStatus, responseSubmitbyName, updateRemrks);
		return EmployeeMapper.mapToEmployeeDto(employee);
	}

	/**
	 * @Override public EmployeeDto updateEmployeeMrResponseStatus(Long employeeId,
	 *           String newStatus, String responseSubmitbyname) { Employee employee
	 *           = employeeRepository.findById(employeeId) .orElseThrow(() -> new
	 *           ResourceNotFoundException("Employee not found"));
	 *           employee.setManagerStatus(newStatus);
	 *           employee.setProcessesStatus(newStatus);
	 *           statusHistoryService.trackStatusChange(employee, newStatus,
	 *           responseSubmitbyname); return
	 *           EmployeeMapper.mapToEmployeeDto(employee); }
	 */

	@Override
	public EmployeeDto updateEmployeeMrResponseStatusTestCheck(Long employeeId, String newStatus,
			String responseSubmitbyname, String remarks) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
		employee.setManagerStatus(newStatus);
		employee.setProcessesStatus(newStatus);
		statusHistoryService.trackStatusChange2(employee, newStatus, responseSubmitbyname, remarks);
		return EmployeeMapper.mapToEmployeeDto(employee);
	}

	@Override
	public EmployeeDto updateEmployeeHrRejectedScreeningResponse(Long employeeId, String reSetHrField, String newStatus,
			String responseSubmitbyname) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
		employee.setHrStatus(reSetHrField);
		statusHistoryService.trackStatusChange(employee, newStatus, responseSubmitbyname);
		return EmployeeMapper.mapToEmployeeDto(employee);
	}

	@Override
	public List<EmployeeDto> getAllScheduleInterview() {
		List<Object[]> employeeObjects = employeeRepository.findEmployeesWithScheduledInterviews();
		List<EmployeeDto> employees = new ArrayList<>();

		for (Object[] result : employeeObjects) {
			EmployeeDto employee = new EmployeeDto();
			employee.setId((Long) result[0]);
			employee.setFullName((String) result[1]);
			employee.setEmail((String) result[2]);
			employee.setJobProfile((String) result[3]);
			employee.setMobileNo((Long) result[4]);
			employee.setPermanentAddress((String) result[5]);
			employee.setGender((String) result[6]);
			employee.setCreationDate((Date) result[7]);
			employees.add(employee);
		}
		return employees.stream()
				.sorted((e1, e2) -> Long.compare(e2.getCreationDate().getTime(), e1.getCreationDate().getTime()))
				.collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDto> getAllHrResponseValue() {
		List<Object[]> employeeObjects = employeeRepository.findEmployeeWithHrResponseStatus();
		List<EmployeeDto> employees = new ArrayList<>();
		for (Object[] result : employeeObjects) {
			EmployeeDto employee = new EmployeeDto();
			employee.setId((Long) result[0]);
			employee.setFullName((String) result[1]);
			employee.setEmail((String) result[2]);
			employee.setJobProfile((String) result[3]);
			employee.setMobileNo((Long) result[4]);
			employee.setPermanentAddress((String) result[5]);
			employee.setGender((String) result[6]);
			employees.add(employee);
		}
		return employees;
	}

	/**
	 * @Override public List<EmployeeDto> getAllHdfcResponseValue() {
	 * 
	 *           List<Object[]> employeeObjects =
	 *           employeeRepository.findEmployeeOnHdfcProcesses(); List<EmployeeDto>
	 *           employees = new ArrayList<>(); for (Object[] result :
	 *           employeeObjects) { EmployeeDto employee = new EmployeeDto();
	 *           employee.setId((Long) result[0]); employee.setFullName((String)
	 *           result[1]); employee.setEmail((String) result[2]);
	 *           employee.setJobProfile((String) result[3]);
	 *           employee.setMobileNo((Long) result[4]);
	 *           employee.setPermanentAddress((String) result[5]);
	 *           employee.setGender((String) result[6]);
	 *           employee.setPreviousOrganisation((String) result[7]);
	 *           employee.setProcessesStatus((String) result[8]);
	 *           employee.setCreationDate((Date) result[9]);
	 *           employees.add(employee); } return employees; }
	 * 
	 * @Override public List<EmployeeDto> getAllIciciResponseValue() {
	 *           List<Object[]> employeeObjects =
	 *           employeeRepository.findEmployeeOnIciciProcesses();
	 *           List<EmployeeDto> employees = new ArrayList<>(); for (Object[]
	 *           result : employeeObjects) { EmployeeDto employee = new
	 *           EmployeeDto(); employee.setId((Long) result[0]);
	 *           employee.setFullName((String) result[1]);
	 *           employee.setEmail((String) result[2]);
	 *           employee.setJobProfile((String) result[3]);
	 *           employee.setMobileNo((Long) result[4]);
	 *           employee.setPermanentAddress((String) result[5]);
	 *           employee.setGender((String) result[6]);
	 *           employee.setPreviousOrganisation((String) result[7]);
	 *           employee.setProcessesStatus((String) result[8]);
	 *           employee.setCreationDate((Date) result[9]);
	 * 
	 *           employees.add(employee); } return employees; }
	 * 
	 * @Override public List<EmployeeDto> getAllMisResponseValue() { List<Object[]>
	 *           employeeObjects = employeeRepository.findEmployeeOnMisProcesses();
	 *           List<EmployeeDto> employees = new ArrayList<>(); for (Object[]
	 *           result : employeeObjects) { EmployeeDto employee = new
	 *           EmployeeDto(); employee.setId((Long) result[0]);
	 *           employee.setFullName((String) result[1]);
	 *           employee.setEmail((String) result[2]);
	 *           employee.setJobProfile((String) result[3]);
	 *           employee.setMobileNo((Long) result[4]);
	 *           employee.setPermanentAddress((String) result[5]);
	 *           employee.setGender((String) result[6]);
	 *           employee.setPreviousOrganisation((String) result[7]);
	 *           employee.setProcessesStatus((String) result[8]);
	 *           employee.setCreationDate((Date) result[9]);
	 *           employees.add(employee); } return employees; }
	 */
	@Override
	public List<EmployeeDto> getAllResponseValueOnProcessType(String role) {
		List<Object[]> employeeObjects = employeeRepository.findEmployeesByRoleType(role);
		List<EmployeeDto> employees = employeeObjects.stream().map(result -> {
			EmployeeDto employee = new EmployeeDto();
			employee.setId((Long) result[0]);
			employee.setFullName((String) result[1]);
			employee.setEmail((String) result[2]);
			employee.setJobProfile((String) result[3]);
			employee.setMobileNo((Long) result[4]);
			employee.setPermanentAddress((String) result[5]);
			employee.setGender((String) result[6]);
			employee.setPreviousOrganisation((String) result[7]);
			employee.setProcessesStatus((String) result[8]);
			employee.setCreationDate((Date) result[9]);
			return employee;
		}).collect(Collectors.toList());

		return employees.stream()
				.sorted((e1, e2) -> Long.compare(e2.getCreationDate().getTime(), e1.getCreationDate().getTime()))
				.collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDto> getAllRejectedEmp() {
		List<Object[]> employeeObjects = employeeRepository.getRejectedEmployeeInfo();
		List<EmployeeDto> employees = new ArrayList<>();
		for (Object[] result : employeeObjects) {
			EmployeeDto employee = new EmployeeDto();
			employee.setId((Long) result[0]);
			employee.setFullName((String) result[1]);
			employee.setEmail((String) result[2]);
			employee.setJobProfile((String) result[3]);
			employee.setMobileNo((Long) result[4]);
			employee.setPermanentAddress((String) result[5]);
			employee.setGender((String) result[6]);
			employee.setPreviousOrganisation((String) result[7]);
			employee.setProcessesStatus((String) result[8]);
			employee.setCreationDate((Date) result[9]);
			employee.setReMarksByHr((String) result[10]);
			employee.setReMarksByManager((String) result[11]);
			employee.setProfileScreenRemarks((String) result[12]);

			employees.add(employee);
		}
		return employees.stream()
				.sorted((e1, e2) -> Long.compare(e2.getCreationDate().getTime(), e1.getCreationDate().getTime()))
				.collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDto> getAllApprovedEmp() {
		List<Object[]> employeeObjects = employeeRepository.getApprovedEmployeeInfo();
		List<EmployeeDto> employees = new ArrayList<>();
		for (Object[] result : employeeObjects) {
			EmployeeDto employee = new EmployeeDto();
			employee.setId((Long) result[0]);
			employee.setFullName((String) result[1]);
			employee.setEmail((String) result[2]);
			employee.setJobProfile((String) result[3]);
			employee.setMobileNo((Long) result[4]);
			employee.setPermanentAddress((String) result[5]);
			employee.setGender((String) result[6]);
			employee.setCreationDate((Date) result[7]);
			employee.setReMarksByHr((String) result[8]);
			employee.setReMarksByManager((String) result[9]);
			employee.setProfileScreenRemarks((String) result[10]);
			employees.add(employee);
		}
		return employees.stream()
				.sorted((e1, e2) -> Long.compare(e2.getCreationDate().getTime(), e1.getCreationDate().getTime()))
				.collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDto> getHrRejectedEmp() {
		List<Object[]> employeeObjects = employeeRepository.getHrRejectedEmployeeInfo();
		List<EmployeeDto> employees = new ArrayList<>();
		for (Object[] result : employeeObjects) {
			EmployeeDto employee = new EmployeeDto();
			employee.setId((Long) result[0]);
			employee.setFullName((String) result[1]);
			employee.setEmail((String) result[2]);
			employee.setJobProfile((String) result[3]);
			employee.setMobileNo((Long) result[4]);
			employee.setPermanentAddress((String) result[5]);
			employee.setGender((String) result[6]);
			employee.setCreationDate((Date) result[7]);
			employee.setProfileScreenRemarks((String) result[8]);

			employees.add(employee);
		}
		return employees.stream()
				.sorted((e1, e2) -> Long.compare(e2.getCreationDate().getTime(), e1.getCreationDate().getTime()))
				.collect(Collectors.toList());
	}

	@Override
	public List<EmployeeDto> getEmpDetailsInfoById(Long employeeId) {
		List<Object[]> empObj = employeeRepository.getEmpDetailsInfoById(employeeId);
		Map<Long, EmployeeDto> employeeMap = new HashMap<>();

		for (Object[] result : empObj) {
			Long id = (Long) result[0];
			String fullName = (String) result[1];
			String aadhaarNumber = (String) result[2];
			String email = (String) result[3];
			Date creationDate = (Date) result[4];
			String status = (String) result[5];
			Date changesDateTime = (Date) result[6];
			String hrName = (String) result[7];
			String remarksOnEveryStages = (String) result[8];

			if (!employeeMap.containsKey(id)) {
				EmployeeDto employeeDetailsDto = new EmployeeDto();
				employeeDetailsDto.setId(id);
				employeeDetailsDto.setFullName(fullName);
				employeeDetailsDto.setAadhaarNumber(aadhaarNumber);
				employeeDetailsDto.setEmail(email);
				employeeDetailsDto.setCreationDate(creationDate);

				List<StatusHistory> statusHistoryList = new ArrayList<>();
				employeeDetailsDto.setStatusHistories(statusHistoryList);
				employeeMap.put(id, employeeDetailsDto);
			}

			StatusHistory statusHistory = new StatusHistory();
			statusHistory.setStatus(status);
			statusHistory.setChangesDateTime(changesDateTime);
			statusHistory.setHrName(hrName);
			statusHistory.setRemarksOnEveryStages(remarksOnEveryStages);
			employeeMap.get(id).getStatusHistories().add(statusHistory);
		}

		return new ArrayList<>(employeeMap.values());

	}

	/**
	 * private void assignManagerForInterview(InterviewProcesses interviewProcesses)
	 * { Long managerId = null; String processType =
	 * interviewProcesses.getProcessName(); if (processType != null) { switch
	 * (processType) { case "HDFC": { managerId = 1L; break; } case "ICICI": {
	 * managerId = 2L; break; } case "MIS": { managerId = 3L; break; } default:
	 * throw new IllegalArgumentException("Unexpected value: " + processType); } }
	 * 
	 * if (managerId != null) { ManagerDetails ma =
	 * managerDetailsRepository.findById(managerId) .orElseThrow(() -> new
	 * RuntimeException("Manager details not found for id: ")); ;
	 * interviewProcesses.setManagerDetails(ma); } }
	 */

	/**
	 * @Override public void updateEmployeeRemarksHrAndManager(Long employeeId,
	 *           String hrRemarks, String managerRemaks, String profileScreenRemark)
	 *           { Employee employee = employeeRepository.findById(employeeId)
	 *           .orElseThrow(() -> new ResourceNotFoundException("Employee not
	 *           found"));
	 * 
	 * 
	 *           if (hrRemarks != null) { employee.setReMarksByHr(hrRemarks); } if
	 *           (managerRemaks != null) {
	 *           employee.setReMarksByManager(managerRemaks); } if
	 *           (profileScreenRemark != null) {
	 *           employee.setProfileScreenRemarks(profileScreenRemark); }
	 *           employeeRepository.save(employee);
	 * 
	 *           }
	 */

	@Override
	public void updateEmployeeRemarks(Long employeeId, String remarks, RemarksType remarksType, String jobProfile) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
		switch (remarksType) {
		case SCHEDULE:
			employee.setReMarksByHr(remarks);
			employee.setJobProfile(jobProfile);
			break;
		case PROFILE:
			employee.setProfileScreenRemarks(remarks);
			break;
		case MANAGER:
			employee.setReMarksByManager(remarks);
			break;
		default:
			throw new IllegalArgumentException("Unknown remark type");
		}
		employeeRepository.save(employee);
	}

	/**
	 * @Override public void updateEmployeeRemarksHrAndManagerchecks(Long
	 *           employeeId, String hrRemarks, String managerRemaks, String
	 *           profileScreenRemark, String updateName) { Employee employee =
	 *           employeeRepository.findById(employeeId) .orElseThrow(() -> new
	 *           ResourceNotFoundException("Employee not found")); if (hrRemarks !=
	 *           null) { employee.setReMarksByHr(hrRemarks);
	 *           setStatusHistoryRecoredRemarksChecks(employeeId, null, updateName,
	 *           employee, hrRemarks);
	 * 
	 *           } if (managerRemaks != null) {
	 *           employee.setReMarksByManager(managerRemaks);
	 *           setStatusHistoryRecoredRemarksChecks(employeeId, null, updateName,
	 *           employee, managerRemaks); } if (profileScreenRemark != null) {
	 *           employee.setProfileScreenRemarks(profileScreenRemark);
	 *           setStatusHistoryRecoredRemarksChecks(employeeId, null, updateName,
	 *           employee, profileScreenRemark); }
	 *           employeeRepository.save(employee);
	 * 
	 *           }
	 */

	/**
	 * private void setStatusHistoryRecored(Long employeeId, InterviewProcesses
	 * savedInterviewProcess, String newStatus, Employee employee) { StatusHistory
	 * statusHistory = new StatusHistory(); statusHistory.setEmployee(employee);
	 * statusHistory.setInterviewProcess(savedInterviewProcess);
	 * statusHistory.setStatus(newStatus);
	 * statusHistory.setHrName(savedInterviewProcess.getScheduledBy());
	 * LocalDateTime currentDateTime = LocalDateTime.now(); Date currentDate =
	 * Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
	 * statusHistory.setChangesDateTime(currentDate);
	 * statusHistoryRepository.save(statusHistory); }
	 */
	private void setStatusHistoryRecoredRemarksChecks(Long employeeId, InterviewProcesses savedInterviewProcess,
			String newStatus, Employee employee, String remarksOnEveryStages) {
		StatusHistory statusHistory = new StatusHistory();
		statusHistory.setEmployee(employee);
		statusHistory.setInterviewProcess(savedInterviewProcess);
		statusHistory.setStatus(newStatus);
		statusHistory.setHrName(savedInterviewProcess.getScheduledBy());
		statusHistory.setRemarksOnEveryStages(remarksOnEveryStages);
		LocalDateTime currentDateTime = LocalDateTime.now();
		Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
		statusHistory.setChangesDateTime(currentDate);
		statusHistoryRepository.save(statusHistory);
	}

	@Override
	public Long getTotalCountEmp() {
		// TODO Auto-generated method stub
		List<Employee> employeeDto = employeeRepository.findAll();
		long empCount = employeeDto.stream().count();
		return empCount;
	}

	@Override
	public List<EmployeeExcelReportDto> getEmployeesDumpReportData(LocalDate startDate, LocalDate endDate) {
		List<Object[]> result = employeeRepository.getEmployeeDumpData();
		List<EmployeeExcelReportDto> dtos = new ArrayList<>();
		for (Object[] row : result) {
			EmployeeExcelReportDto dto = new EmployeeExcelReportDto();
			dto.setId((Long) row[0]);
			dto.setFullName((String) row[1]);
			dto.setEmail((String) row[2]);
			dto.setJobProfile((String) row[3]);
			dto.setQualification((String) row[4]);
			dto.setMobileNo((Long) row[5]);
			dto.setPermanentAddress((String) row[6]);
			dto.setCurrentAddress((String) row[7]);
			dto.setGender((String) row[8]);
			dto.setPreviousOrganisation((String) row[9]);
			dto.setDob((Date) row[10]);
			dto.setMaritalStatus((String) row[11]);
			dto.setRefferal((String) row[12]);
			dto.setAadhaarNumber((String) row[13]);
			dto.setLanguages((String) row[14]);
			dto.setExperience((Float) row[15]);
			dto.setSource((String) row[16]);
			dto.setSubSource((String) row[17]);
			dto.setInitialStatus((String) row[18]);
			dto.setProcessesStatus((String) row[19]);
			dto.setHrStatus((String) row[20]);
			dto.setManagerStatus((String) row[21]);
			dto.setCreationDate((Date) row[22]);
			dto.setLastInterviewAssin((String) row[23]);
			dto.setReMarksByHr((String) row[24]);
			dto.setReMarksByManager((String) row[25]);
			dto.setProfileScreenRemarks((String) row[26]);
			dto.setStatus((String) row[27]);
			dto.setHrName((String) row[28]);
			dto.setRemarksOnEveryStages((String) row[29]);
			dto.setChangesDateTime((Date) row[30]);
//    	 dtos.add(dto);
			if (dto.getCreationDate() != null) {
				LocalDate creationDate = dto.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if ((startDate == null || !creationDate.isBefore(startDate))
						&& (endDate == null || !creationDate.isAfter(endDate))) {
					dtos.add(dto);
				}
			}

		}
		return dtos;
	}

	@Override
	public List<EmployeeExcelReportInSequenceDto> getEmployeesDumpReportData(Date startDate, Date endDate) {
		List<Object[]> result = employeeRepository.getEmployeeSeqDumpData(startDate, endDate);
		List<EmployeeExcelReportInSequenceDto> response = new ArrayList<>();

		for (Object[] row : result) {
			EmployeeExcelReportInSequenceDto dto = new EmployeeExcelReportInSequenceDto();
			dto.setId((Long) row[0]);
			dto.setFullName((String) row[1]);
			dto.setQualification((String) row[2]);
			dto.setAadhaarNumber((String) row[3]);
			dto.setCreationDate((Date) row[4]);
			dto.setCurrentAddress((String) row[5]);
			dto.setDob((Date) row[6]);
			dto.setEmail((String) row[7]);
			if (row[8] != null) {
				try {
					dto.setExperience(Float.parseFloat(row[8].toString()));
				} catch (NumberFormatException e) {
					dto.setExperience(0f);
				}
			}
			dto.setGender((String) row[9]);
			dto.setHrStatus((String) row[10]);
			dto.setInitialStatus((String) row[11]);
			dto.setJobProfile((String) row[12]);
			dto.setLanguages((String) row[13]);
			dto.setLastInterviewAssin((String) row[14]);
			dto.setManagerStatus((String) row[15]);
			dto.setMaritalStatus((String) row[16]);
			dto.setMobileNo((Long) row[17]);
			dto.setPermanentAddress((String) row[18]);
			dto.setPreviousOrganisation((String) row[19]);
			dto.setProcessesStatus((String) row[20]);
			dto.setProfileScreenRemarks((String) row[21]);
			dto.setReMarksByHr((String) row[22]);
			dto.setReMarksByManager((String) row[23]);
			dto.setRefferal((String) row[24]);
			dto.setSource((String) row[25]);
			dto.setSubSource((String) row[26]);
			dto.setWorkExp((String) row[27]);
			if (row.length > 28) {
				String[] hrNames = ((String) row[28]).split(" \\| ");
				String[] changesDateTimes = ((String) row[29]).split(" \\| ");
				String[] remarks = ((String) row[30]).split(" \\| ");
				String[] statuses = ((String) row[31]).split(" \\| ");

				// Ensure that these arrays have the same length
				int maxLength = Math.max(hrNames.length,
						Math.max(changesDateTimes.length, Math.max(remarks.length, statuses.length)));
				List<StatusHistoryExcelDto> statusHistoryList = new ArrayList<>();

				for (int i = 0; i < maxLength; i++) {
					String hrName = (i < hrNames.length) ? hrNames[i] : "";
					String changeDateTime = (i < changesDateTimes.length) ? changesDateTimes[i] : "";
					String remark = (i < remarks.length) ? remarks[i] : "";
					String status = (i < statuses.length) ? statuses[i] : "";

					StatusHistoryExcelDto statusHistoryDto = new StatusHistoryExcelDto(hrName, changeDateTime, remark,
							status);
					statusHistoryList.add(statusHistoryDto);
				}
				dto.setStatusHistory(statusHistoryList);
			} else {
				dto.setStatusHistory(new ArrayList<>());
			}

			response.add(dto);
		}

		return response;
	}

	public void exportToExcel(List<EmployeeExcelReportInSequenceDto> data, HttpServletResponse response)
			throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employees");
		createSeqReportExcelHeader(sheet);
		createDynamicStatusHistoryHeaders(sheet, data);
		populateRowsWithEmployeeData(sheet, data);
		writeToResponse(workbook, response);

		/**
		 * // Create header row Row headerRow = sheet.createRow(0); String[] headers = {
		 * "Employee ID", "Full Name", "Qualification", "Aadhaar Number", "Creation
		 * Date", "Current Address", "DOB", "Email", "Experience", "Gender", "HR
		 * Status", "Initial Status", "Job Profile", "Languages", "Last Interview
		 * Assigned", "Manager Status", "Marital Status", "Mobile No", "Permanent
		 * Address", "Previous Organisation", "Processes Status", "Profile Screen
		 * Remarks", "Remarks by HR", "Remarks by Manager", "Referral", "Source", "Sub
		 * Source", "Work Experience" };
		 * 
		 * // Set the fixed headers for (int i = 0; i < headers.length; i++) {
		 * headerRow.createCell(i).setCellValue(headers[i]); }
		 * 
		 * // Dynamic status history headers int maxStatusHistorySize =
		 * data.stream().mapToInt(dto -> dto.getStatusHistory().size()).max().orElse(0);
		 * 
		 * // Loop for dynamic status history headers for (int i = 0; i <
		 * maxStatusHistorySize; i++) { int columnOffset = 28 + i * 4;
		 * 
		 * // Determine which header set to use based on `i` int headerIndex = i < 6 ? i
		 * : 4 + (i - 6) % 2; // Repeat headers from index 4 and 5 after i >= 6
		 * 
		 * switch (headerIndex) { case 0:
		 * headerRow.createCell(columnOffset).setCellValue("Name");
		 * headerRow.createCell(columnOffset + 1).setCellValue("Register Time and
		 * Date"); headerRow.createCell(columnOffset + 2).setCellValue("Register
		 * Remarks"); headerRow.createCell(columnOffset + 3).setCellValue("Register
		 * Status"); break; case 1: headerRow.createCell(columnOffset).setCellValue("HR
		 * Name"); headerRow.createCell(columnOffset + 1).setCellValue("HR Time and
		 * Date"); headerRow.createCell(columnOffset + 2).setCellValue("Profile
		 * Remarks"); headerRow.createCell(columnOffset + 3).setCellValue("HR Status");
		 * break; case 2: headerRow.createCell(columnOffset).setCellValue("HR Name");
		 * headerRow.createCell(columnOffset + 1).setCellValue("HR Time and Date");
		 * headerRow.createCell(columnOffset + 2).setCellValue("Interview Schedule
		 * Remarks"); headerRow.createCell(columnOffset + 3).setCellValue("HR Status");
		 * break; case 3: headerRow.createCell(columnOffset).setCellValue("Manager
		 * Name"); headerRow.createCell(columnOffset + 1).setCellValue("Manager Time and
		 * Date"); headerRow.createCell(columnOffset + 2).setCellValue("Manager
		 * Remarks"); headerRow.createCell(columnOffset + 3).setCellValue("Manager
		 * Status"); break; case 4: headerRow.createCell(columnOffset).setCellValue("HR
		 * Name"); headerRow.createCell(columnOffset + 1).setCellValue("ReScheduled Time
		 * and Date"); headerRow.createCell(columnOffset + 2).setCellValue("ReScheduled
		 * Remarks"); headerRow.createCell(columnOffset + 3).setCellValue("HR Status");
		 * break; case 5: headerRow.createCell(columnOffset).setCellValue("Manager
		 * Name"); headerRow.createCell(columnOffset + 1).setCellValue("Manager Time and
		 * Date"); headerRow.createCell(columnOffset + 2).setCellValue("Manager
		 * Remarks"); headerRow.createCell(columnOffset + 3).setCellValue("Manager
		 * Status"); break; } }
		 * 
		 * int rowNum = 1; for (EmployeeExcelReportInSequenceDto dto : data) { Row row =
		 * sheet.createRow(rowNum++);
		 * 
		 * row.createCell(0).setCellValue(dto.getId());
		 * row.createCell(1).setCellValue(dto.getFullName());
		 * row.createCell(2).setCellValue(dto.getQualification());
		 * row.createCell(3).setCellValue(dto.getAadhaarNumber());
		 * row.createCell(4).setCellValue(dto.getCreationDate().toString());
		 * row.createCell(5).setCellValue(dto.getCurrentAddress());
		 * row.createCell(6).setCellValue(dto.getDob().toString());
		 * row.createCell(7).setCellValue(dto.getEmail());
		 * row.createCell(8).setCellValue(dto.getExperience());
		 * row.createCell(9).setCellValue(dto.getGender());
		 * row.createCell(10).setCellValue(dto.getHrStatus());
		 * row.createCell(11).setCellValue(dto.getInitialStatus());
		 * row.createCell(12).setCellValue(dto.getJobProfile());
		 * row.createCell(13).setCellValue(dto.getLanguages());
		 * row.createCell(14).setCellValue(dto.getLastInterviewAssin());
		 * row.createCell(15).setCellValue(dto.getManagerStatus());
		 * row.createCell(16).setCellValue(dto.getMaritalStatus());
		 * row.createCell(17).setCellValue(dto.getMobileNo());
		 * row.createCell(18).setCellValue(dto.getPermanentAddress());
		 * row.createCell(19).setCellValue(dto.getPreviousOrganisation());
		 * row.createCell(20).setCellValue(dto.getProcessesStatus());
		 * row.createCell(21).setCellValue(dto.getProfileScreenRemarks());
		 * row.createCell(22).setCellValue(dto.getReMarksByHr());
		 * row.createCell(23).setCellValue(dto.getReMarksByManager());
		 * row.createCell(24).setCellValue(dto.getRefferal());
		 * row.createCell(25).setCellValue(dto.getSource());
		 * row.createCell(26).setCellValue(dto.getSubSource());
		 * row.createCell(27).setCellValue(dto.getWorkExp());
		 * 
		 * for (int i = 0; i < dto.getStatusHistory().size(); i++) {
		 * StatusHistoryExcelDto statusDto = dto.getStatusHistory().get(i); int
		 * columnOffset = 28 + i * 4;
		 * 
		 * row.createCell(columnOffset).setCellValue(statusDto.getHrName());
		 * row.createCell(columnOffset +
		 * 1).setCellValue(statusDto.getChangesDateTime()); row.createCell(columnOffset
		 * + 2).setCellValue(statusDto.getRemarks()); row.createCell(columnOffset +
		 * 3).setCellValue(statusDto.getStatus()); } }
		 * response.setHeader("Content-Disposition", "attachment;
		 * filename=employees_report.xlsx"); workbook.write(response.getOutputStream());
		 * workbook.close();
		 * 
		 */
	}

	@Override
	public ByteArrayOutputStream exportToRawExcel(List<EmployeeExcelReportDto> data) throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Employee Report");
		createRawExcelHeader(sheet);
		populateRows(sheet, data);
		return writeToOutputStream(workbook);

		/**
		 * Row header = sheet.createRow(0); String[] columns = { "ID", "Full Name",
		 * "Email", "Job Profile", "Qualification", "Mobile No", "Permanent Address",
		 * "Current Address", "Gender", "Previous Organisation", "DOB", "Marital
		 * Status", "Referral", "Aadhaar Number", "Languages", "Experience", "Source",
		 * "Sub Source", "Initial Status", "HR Status", "Remarks by HR", "Processes
		 * Status", "Manager Status", "Remarks by Manager", "Creation Date", "Last
		 * Interview Assign", "Profile Screen Remarks", "Status", "HR Name", "Remarks on
		 * Every Stages", "Changes Date Time" };
		 * 
		 * for (int i = 0; i < columns.length; i++) {
		 * header.createCell(i).setCellValue(columns[i]); }
		 * 
		 * int rowNum = 1; for (EmployeeExcelReportDto dto : data) { Row row =
		 * sheet.createRow(rowNum++);
		 * 
		 * row.createCell(0).setCellValue(dto.getId());
		 * row.createCell(1).setCellValue(dto.getFullName());
		 * row.createCell(2).setCellValue(dto.getEmail());
		 * row.createCell(3).setCellValue(dto.getJobProfile());
		 * row.createCell(4).setCellValue(dto.getQualification());
		 * row.createCell(5).setCellValue(dto.getMobileNo() != null ? dto.getMobileNo()
		 * : 0); row.createCell(6).setCellValue(dto.getPermanentAddress() != null ?
		 * dto.getPermanentAddress() : "");
		 * row.createCell(7).setCellValue(dto.getCurrentAddress() != null ?
		 * dto.getCurrentAddress() : ""); row.createCell(8).setCellValue(dto.getGender()
		 * != null ? dto.getGender() : "");
		 * row.createCell(9).setCellValue(dto.getPreviousOrganisation() != null ?
		 * dto.getPreviousOrganisation() : "");
		 * row.createCell(10).setCellValue(dto.getDob() != null ?
		 * dto.getDob().toString() : "N/A");
		 * row.createCell(11).setCellValue(dto.getMaritalStatus() != null ?
		 * dto.getMaritalStatus() : "");
		 * row.createCell(12).setCellValue(dto.getRefferal() != null ? dto.getRefferal()
		 * : ""); row.createCell(13).setCellValue(dto.getAadhaarNumber() != null ?
		 * dto.getAadhaarNumber() : "");
		 * row.createCell(14).setCellValue(dto.getLanguages() != null ?
		 * dto.getLanguages() : ""); row.createCell(15).setCellValue(dto.getExperience()
		 * != null ? dto.getExperience() : 0);
		 * row.createCell(16).setCellValue(dto.getSource() != null ? dto.getSource() :
		 * ""); row.createCell(17).setCellValue(dto.getSubSource() != null ?
		 * dto.getSubSource() : "");
		 * row.createCell(18).setCellValue(dto.getInitialStatus() != null ?
		 * dto.getInitialStatus() : "");
		 * row.createCell(19).setCellValue(dto.getHrStatus() != null ? dto.getHrStatus()
		 * : ""); row.createCell(20).setCellValue(dto.getReMarksByHr() != null ?
		 * dto.getReMarksByHr() : "");
		 * row.createCell(21).setCellValue(dto.getProcessesStatus() != null ?
		 * dto.getProcessesStatus() : "");
		 * row.createCell(22).setCellValue(dto.getManagerStatus() != null ?
		 * dto.getManagerStatus() : "");
		 * row.createCell(23).setCellValue(dto.getReMarksByManager() != null ?
		 * dto.getReMarksByManager() : "");
		 * row.createCell(24).setCellValue(dto.getCreationDate() != null ?
		 * dto.getCreationDate().toString() : "N/A");
		 * row.createCell(25).setCellValue(dto.getLastInterviewAssin() != null ?
		 * dto.getLastInterviewAssin() : "");
		 * row.createCell(26).setCellValue(dto.getProfileScreenRemarks() != null ?
		 * dto.getProfileScreenRemarks() : "");
		 * row.createCell(27).setCellValue(dto.getStatus() != null ? dto.getStatus() :
		 * ""); row.createCell(28).setCellValue(dto.getHrName() != null ?
		 * dto.getHrName() : "");
		 * row.createCell(29).setCellValue(dto.getRemarksOnEveryStages() != null ?
		 * dto.getRemarksOnEveryStages() : ""); row.createCell(30)
		 * .setCellValue(dto.getChangesDateTime() != null ?
		 * dto.getChangesDateTime().toString() : "N/A"); }
		 * 
		 * ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		 * workbook.write(byteArrayOutputStream); workbook.close();
		 * 
		 * return byteArrayOutputStream;
		 */
	}

	private void createSeqReportExcelHeader(Sheet sheet) {
		Row headerRow = sheet.createRow(0);
		String[] headers = { "Employee ID", "Full Name", "Qualification", "Aadhaar Number", "Creation Date",
				"Current Address", "DOB", "Email", "Experience", "Gender", "HR Status", "Initial Status", "Job Profile",
				"Languages", "Last Interview Assigned", "Manager Status", "Marital Status", "Mobile No",
				"Permanent Address", "Previous Organisation", "Processes Status", "Profile Screen Remarks",
				"Remarks by HR", "Remarks by Manager", "Referral", "Source", "Sub Source", "Work Experience" };

		// Set the fixed headers
		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(i).setCellValue(headers[i]);
		}
	}

	private void createDynamicStatusHistoryHeaders(Sheet sheet, List<EmployeeExcelReportInSequenceDto> data) {
		int maxStatusHistorySize = data.stream().mapToInt(dto -> dto.getStatusHistory().size()).max().orElse(0);
		Row headerRow = sheet.getRow(0);
		for (int i = 0; i < maxStatusHistorySize; i++) {
			int columnOffSet = 28 + i * 4;
			int headerIndex = i < 6 ? i : 4 + (i - 6) % 2;

			switch (headerIndex) {
			case 0:
				addStatusHistoryHeaders(headerRow, columnOffSet, "Name", "Register Time and Date", "Register Remarks",
						"Register Status");
				break;
			case 1:
				addStatusHistoryHeaders(headerRow, columnOffSet, "HR Name", "HR Time and Date", "Profile Remarks",
						"HR Status");
				break;
			case 2:
				addStatusHistoryHeaders(headerRow, columnOffSet, "HR Name", "HR Time and Date",
						"Interview Schedule Remarks", "HR Status");
				break;
			case 3:
				addStatusHistoryHeaders(headerRow, columnOffSet, "Manager Name", "Manager Time and Date",
						"Manager Remarks", "Manager Status");
				break;
			case 4:
				addStatusHistoryHeaders(headerRow, columnOffSet, "HR Name", "ReScheduled Time and Date",
						"ReScheduled Remarks", "HR Status");
				break;
			case 5:
				addStatusHistoryHeaders(headerRow, columnOffSet, "Manager Name", "Manager Time and Date",
						"Manager Remarks", "Manager Status");
				break;
			}
		}
	}

	private void addStatusHistoryHeaders(Row headerRow, int columnOffSet, String... headers) {
		for (int i = 0; i < headers.length; i++) {
			headerRow.createCell(columnOffSet + i).setCellValue(headers[i]);
		}
	}

	private void populateRowsWithEmployeeData(Sheet sheet, List<EmployeeExcelReportInSequenceDto> data) {
		int rowNum = 1;
		for (EmployeeExcelReportInSequenceDto dto : data) {
			Row row = sheet.createRow(rowNum++);

			// Populate fixed columns
			populateFixedColumns(row, dto);

			// Populate dynamic status history columns
			populateStatusHistoryColumns(row, dto);
		}
	}

	private void populateFixedColumns(Row row, EmployeeExcelReportInSequenceDto dto) {
		row.createCell(0).setCellValue(dto.getId());
		row.createCell(1).setCellValue(dto.getFullName());
		row.createCell(2).setCellValue(dto.getQualification());
		row.createCell(3).setCellValue(dto.getAadhaarNumber());
		row.createCell(4).setCellValue(dto.getCreationDate().toString());
		row.createCell(5).setCellValue(dto.getCurrentAddress());
		row.createCell(6).setCellValue(dto.getDob().toString());
		row.createCell(7).setCellValue(dto.getEmail());
		row.createCell(8).setCellValue(dto.getExperience());
		row.createCell(9).setCellValue(dto.getGender());
		row.createCell(10).setCellValue(dto.getHrStatus());
		row.createCell(11).setCellValue(dto.getInitialStatus());
		row.createCell(12).setCellValue(dto.getJobProfile());
		row.createCell(13).setCellValue(dto.getLanguages());
		row.createCell(14).setCellValue(dto.getLastInterviewAssin());
		row.createCell(15).setCellValue(dto.getManagerStatus());
		row.createCell(16).setCellValue(dto.getMaritalStatus());
		row.createCell(17).setCellValue(dto.getMobileNo());
		row.createCell(18).setCellValue(dto.getPermanentAddress());
		row.createCell(19).setCellValue(dto.getPreviousOrganisation());
		row.createCell(20).setCellValue(dto.getProcessesStatus());
		row.createCell(21).setCellValue(dto.getProfileScreenRemarks());
		row.createCell(22).setCellValue(dto.getReMarksByHr());
		row.createCell(23).setCellValue(dto.getReMarksByManager());
		row.createCell(24).setCellValue(dto.getRefferal());
		row.createCell(25).setCellValue(dto.getSource());
		row.createCell(26).setCellValue(dto.getSubSource());
		row.createCell(27).setCellValue(dto.getWorkExp());
	}

	private void populateStatusHistoryColumns(Row row, EmployeeExcelReportInSequenceDto dto) {
		for (int i = 0; i < dto.getStatusHistory().size(); i++) {
			StatusHistoryExcelDto statusDto = dto.getStatusHistory().get(i);
			int columnOffset = 28 + i * 4;

			row.createCell(columnOffset).setCellValue(statusDto.getHrName());
			row.createCell(columnOffset + 1).setCellValue(statusDto.getChangesDateTime());
			row.createCell(columnOffset + 2).setCellValue(statusDto.getRemarks());
			row.createCell(columnOffset + 3).setCellValue(statusDto.getStatus());
		}
	}

	private void writeToResponse(Workbook workbook, HttpServletResponse response) throws IOException {
		response.setHeader("Content-Disposition", "attachment; filename=employees_report.xlsx");
		workbook.write(response.getOutputStream());
		workbook.close();
	}

	private void createRawExcelHeader(Sheet sheet) {
		Row header = sheet.createRow(0);
		String[] columns = { "ID", "Full Name", "Email", "Job Profile", "Qualification", "Mobile No",
				"Permanent Address", "Current Address", "Gender", "Previous Organisation", "DOB", "Marital Status",
				"Referral", "Aadhaar Number", "Languages", "Experience", "Source", "Sub Source", "Initial Status",
				"HR Status", "Remarks by HR", "Processes Status", "Manager Status", "Remarks by Manager",
				"Creation Date", "Last Interview Assign", "Profile Screen Remarks", "Status", "HR Name",
				"Remarks on Every Stages", "Changes Date Time" };

		for (int i = 0; i < columns.length; i++) {
			header.createCell(i).setCellValue(columns[i]);
		}
	}

	private void populateRows(Sheet sheet, List<EmployeeExcelReportDto> data) {
		int rowNum = 1;
		for (EmployeeExcelReportDto dto : data) {
			Row row = sheet.createRow(rowNum++);
			populateRowWithData(row, dto);
		}
	}

	private void populateRowWithData(Row row, EmployeeExcelReportDto dto) {
		row.createCell(0).setCellValue(dto.getId());
		row.createCell(1).setCellValue(dto.getFullName());
		row.createCell(2).setCellValue(dto.getEmail());
		row.createCell(3).setCellValue(dto.getJobProfile());
		row.createCell(4).setCellValue(dto.getQualification());
		row.createCell(5).setCellValue(dto.getMobileNo() != null ? dto.getMobileNo() : 0);
		row.createCell(6).setCellValue(dto.getPermanentAddress() != null ? dto.getPermanentAddress() : "");
		row.createCell(7).setCellValue(dto.getCurrentAddress() != null ? dto.getCurrentAddress() : "");
		row.createCell(8).setCellValue(dto.getGender() != null ? dto.getGender() : "");
		row.createCell(9).setCellValue(dto.getPreviousOrganisation() != null ? dto.getPreviousOrganisation() : "");
		row.createCell(10).setCellValue(dto.getDob() != null ? dto.getDob().toString() : "N/A");
		row.createCell(11).setCellValue(dto.getMaritalStatus() != null ? dto.getMaritalStatus() : "");
		row.createCell(12).setCellValue(dto.getRefferal() != null ? dto.getRefferal() : "");
		row.createCell(13).setCellValue(dto.getAadhaarNumber() != null ? dto.getAadhaarNumber() : "");
		row.createCell(14).setCellValue(dto.getLanguages() != null ? dto.getLanguages() : "");
		row.createCell(15).setCellValue(dto.getExperience() != null ? dto.getExperience() : 0);
		row.createCell(16).setCellValue(dto.getSource() != null ? dto.getSource() : "");
		row.createCell(17).setCellValue(dto.getSubSource() != null ? dto.getSubSource() : "");
		row.createCell(18).setCellValue(dto.getInitialStatus() != null ? dto.getInitialStatus() : "");
		row.createCell(19).setCellValue(dto.getHrStatus() != null ? dto.getHrStatus() : "");
		row.createCell(20).setCellValue(dto.getReMarksByHr() != null ? dto.getReMarksByHr() : "");
		row.createCell(21).setCellValue(dto.getProcessesStatus() != null ? dto.getProcessesStatus() : "");
		row.createCell(22).setCellValue(dto.getManagerStatus() != null ? dto.getManagerStatus() : "");
		row.createCell(23).setCellValue(dto.getReMarksByManager() != null ? dto.getReMarksByManager() : "");
		row.createCell(24).setCellValue(dto.getCreationDate() != null ? dto.getCreationDate().toString() : "N/A");
		row.createCell(25).setCellValue(dto.getLastInterviewAssin() != null ? dto.getLastInterviewAssin() : "");
		row.createCell(26).setCellValue(dto.getProfileScreenRemarks() != null ? dto.getProfileScreenRemarks() : "");
		row.createCell(27).setCellValue(dto.getStatus() != null ? dto.getStatus() : "");
		row.createCell(28).setCellValue(dto.getHrName() != null ? dto.getHrName() : "");
		row.createCell(29).setCellValue(dto.getRemarksOnEveryStages() != null ? dto.getRemarksOnEveryStages() : "");
		row.createCell(30).setCellValue(dto.getChangesDateTime() != null ? dto.getChangesDateTime().toString() : "N/A");
	}

	private ByteArrayOutputStream writeToOutputStream(Workbook workbook) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		workbook.write(byteArrayOutputStream);
		workbook.close();
		return byteArrayOutputStream;
	}
}
