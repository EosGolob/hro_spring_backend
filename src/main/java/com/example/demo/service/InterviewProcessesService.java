package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.InterviewsRequestDto;

public interface InterviewProcessesService {
	InterviewsRequestDto addInterviewProcess(InterviewsRequestDto interviewProcessesDto);

	List<InterviewsRequestDto> getAllInterviewProcessesByEmployeeId(Long employeeId);

	List<InterviewsRequestDto> getAttendedProcesses(Long employeeId);
//	 public List<InterviewProcesses> getInterviewProcessesByStatus(String status); 

}
