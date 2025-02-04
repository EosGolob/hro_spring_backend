package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.InterviewProcesses;


public interface InterviewProcessesRepository extends JpaRepository<InterviewProcesses, Long> {
	
	    @Query("SELECT ip FROM InterviewProcesses ip WHERE ip.employee.id = :employeeId")
	    List<InterviewProcesses> findAllByEmployeeId(@Param("employeeId") Long employeeId);
	    
	    List<InterviewProcesses> findByEmployeeId(Long employeeId);
	    
	    @Query("SELECT ip FROM InterviewProcesses ip WHERE ip.employee.id = :employeeId")
	    List<InterviewProcesses> interviewProcessAttenndedByEmp(@Param("employeeId") Long employeeId);

	    	   

}
