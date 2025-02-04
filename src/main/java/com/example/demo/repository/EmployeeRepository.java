package com.example.demo.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.EmployeeExcelReportDto;
import com.example.demo.dto.EmployeeExcelReportInSequenceDto;
import com.example.demo.dto.EmployeeSearchDto;
import com.example.demo.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	boolean existsByEmail(String email);

	boolean existsByAadhaarNumber(String aadhaarNumber);

//	@Query("SELECT e FROM Employee e JOIN InterviewProcess ip ON e.id = ip.employee.id WHERE ip.status = :status")
//    List<EmployeeDto> findByInterviewProcessStatus(@Param("status") String status);

	@Query("SELECT e.id, e.fullName, e.email, e.jobProfile, e.mobileNo, e.permanentAddress, e.gender, e.creationDate "
			+ "FROM Employee e WHERE e.initialStatus = 'Active' AND e.hrStatus IS NULL")
	List<Object[]> findEmployeesWithScheduledInterviews();

	@Query("SELECT e.id,e.fullName,e.email, e.jobProfile, e.mobileNo, e.permanentAddress ,e.gender "
			+ "FROM Employee e  WHERE e.hrStatus = 'Select' or e.hrStatus = 'Reject'")
	List<Object[]> findEmployeeWithHrResponseStatus();

	@Query("SELECT e.id, e.fullName, e.email, e.jobProfile, e.mobileNo, e.permanentAddress, e.currentAddress, e.gender,e.creationDate "
			+ "FROM Employee e " + "WHERE e.hrStatus = 'Select' AND e.processesStatus Is NUll")
	List<Object[]> getEmployeeWithSelectedValue();

	@Query("SELECT e.id,e.fullName,e.email, e.jobProfile, e.mobileNo, e.permanentAddress ,e.gender, e.previousOrganisation, e.processesStatus ,e.creationDate "
			+ "FROM Employee e WHERE e.processesStatus = 'HDFC' ")
	List<Object[]> findEmployeeOnHdfcProcesses();

	@Query("SELECT e.id,e.fullName,e.email, e.jobProfile, e.mobileNo, e.permanentAddress ,e.gender,e.previousOrganisation,e.processesStatus, e.creationDate "
			+ "FROM Employee e WHERE e.processesStatus = 'MIS' ")
	List<Object[]> findEmployeeOnMisProcesses();

	@Query("SELECT e.id,e.fullName,e.email, e.jobProfile, e.mobileNo, e.permanentAddress ,e.gender ,e.previousOrganisation,e.processesStatus , e.creationDate   "
			+ "FROM Employee e WHERE e.processesStatus = 'ICICI' ")
	List<Object[]> findEmployeeOnIciciProcesses();

	@Query("SELECT e.id,e.fullName,e.email, e.jobProfile, e.mobileNo, e.permanentAddress ,e.gender ,e.previousOrganisation,e.processesStatus , e.creationDate   "
			+ "FROM Employee e WHERE e.processesStatus = :role")
	List<Object[]> findEmployeesByRoleType(String role);

//	@Query("SELECT e.id, e.fullName, e.aadhaarNumber, e.email, e.creationDate,sh.status, sh.changesDateTime ,sh.hrName ,e.reMarksByHr, e.reMarksByManager,e.profileScreenRemarks,sh.remarksOnEveryStages "
//			+ "FROM Employee e " + "JOIN StatusHistory sh ON e.id = sh.employee.id " + "WHERE e.id = :id")
	@Query("SELECT e.id, e.fullName, e.aadhaarNumber, e.email, e.creationDate,sh.status, sh.changesDateTime ,sh.hrName,sh.remarksOnEveryStages "
			+ "FROM Employee e " + "JOIN StatusHistory sh ON e.id = sh.employee.id " + "WHERE e.id = :id")
	List<Object[]> getEmpDetailsInfoById(@Param("id") Long employeeId);

	@Query("SELECT e.id,e.fullName,e.email, e.jobProfile, e.mobileNo, e.permanentAddress ,e.gender, e.creationDate ,e.profileScreenRemarks  "
			+ "FROM Employee e  WHERE e.hrStatus = 'Reject'")
	List<Object[]> getHrRejectedEmployeeInfo();

	@Query("SELECT e.id,e.fullName,e.email, e.jobProfile, e.mobileNo, e.permanentAddress ,e.gender , e.previousOrganisation,e.processesStatus,e.creationDate,e.reMarksByHr,e.reMarksByManager,e.profileScreenRemarks "
			+ "FROM Employee e WHERE e.processesStatus = 'Reject' ")
	List<Object[]> getRejectedEmployeeInfo();

	@Query("SELECT e.id,e.fullName,e.email, e.jobProfile, e.mobileNo, e.permanentAddress ,e.gender , e.creationDate,e.reMarksByHr,e.reMarksByManager,e.profileScreenRemarks "
			+ "FROM Employee e WHERE e.managerStatus = 'Select' ")
	List<Object[]> getApprovedEmployeeInfo();

//	Optional<Employee> findByFullName(String empName);

	@Query("SELECT e.id, e.fullName, e.email, e.initialStatus " + "FROM Employee e WHERE e.fullName = :fullName")
	List<EmployeeSearchDto> searchByName(@Param("fullName") String fullName);

	List<Employee> findByFullName(String employeeName);

	@Query(value = "SELECT e.id ,e.full_Name,e.email,job_Profile,e.qualification,e.mobile_no,e.permanent_address,e.current_address, "
			+ "e.gender,e.previous_Organisation,e.dob,e.marital_Status,e.refferal,e.aadhaar_Number,e.languages,e.experience,"
			+ "e.source,e.sub_Source,e.initial_Status,e.processes_Status,e.hr_Status,e.manager_Status,e.creation_Date,e.last_Interview_Assin,"
			+ "e.re_Marks_By_Hr,e.re_Marks_By_Manager,e.profile_Screen_Remarks ,"
			+ "sh.status, sh.hr_name, sh.remarks_on_every_stages, sh.changes_date_time " + "FROM employees e "
			+ "INNER JOIN status_history sh ON e.id = sh.employee_id", nativeQuery = true)
	List<Object[]> getEmployeeDumpData();

	@Query(value = "SELECT " + "s.employee_id, " + "ed.full_name, " + "ed.qualification, " + "ed.aadhaar_number, "
			+ "ed.creation_date, " + "ed.current_address, " + "ed.dob, " + "ed.email, " + "ed.experience, "
			+ "ed.gender, " + "ed.hr_status, " + "ed.initial_status, " + "ed.job_profile, " + "ed.languages, "
			+ "ed.last_interview_assin, " + "ed.manager_status, " + "ed.marital_status, " + "ed.mobile_no, "
			+ "ed.permanent_address, " + "ed.previous_organisation, " + "ed.processes_status, "
			+ "ed.profile_Screen_remarks, " + "ed.re_marks_by_hr, " + "ed.re_marks_by_manager, " + "ed.refferal, "
			+ "ed.source, " + "ed.sub_source, " + "ed.work_exp, "
			+ "GROUP_CONCAT(IFNULL(s.hr_name, '') ORDER BY s.changes_date_time SEPARATOR ' | ') AS hr_names, "
			+ "GROUP_CONCAT(IFNULL(s.changes_date_time, '') ORDER BY s.changes_date_time SEPARATOR ' | ') AS changes_date_times, "
			+ "GROUP_CONCAT(IFNULL(s.remarks_on_every_stages, '') ORDER BY s.changes_date_time SEPARATOR ' | ') AS remarks, "
			+ "GROUP_CONCAT(IFNULL(s.status, '') ORDER BY s.changes_date_time SEPARATOR ' | ') AS status "
			+ "FROM status_history s " + "JOIN employees ed ON s.employee_id = ed.id "
			+ "WHERE ed.creation_date BETWEEN :startDate AND :endDate "
			+ "GROUP BY s.employee_id, ed.full_name", nativeQuery = true)
	List<Object[]> getEmployeeSeqDumpData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

//	List<EmployeeSearchDto> findSearchName(String employeeName);
}
