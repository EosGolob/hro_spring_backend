package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Employee;
import com.example.demo.entity.StatusHistory;

public interface StatusHistoryRepository extends  JpaRepository<StatusHistory, Long> {

	StatusHistory findTopByEmployeeOrderByChangesDateTimeDesc(Employee employee);
//	  List<StatusHistory> findByInterviewProcessId(Long interviewProcessId);
}
