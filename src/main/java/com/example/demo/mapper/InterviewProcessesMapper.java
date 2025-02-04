package com.example.demo.mapper;

import com.example.demo.dto.InterviewsRequestDto;
import com.example.demo.entity.InterviewProcesses;

public class InterviewProcessesMapper {

	
	 public static InterviewsRequestDto mapToInterviewProcessesDto(InterviewProcesses interviewProcesses) {
	        return new InterviewsRequestDto(
	                interviewProcesses.getId(),
	                interviewProcesses.getEmployee().getId(),
	                interviewProcesses.getProcessName(),
	                interviewProcesses.getInterviewDate(),
	                interviewProcesses.getInterviewTime(),
	                interviewProcesses.getStatus(),
	                interviewProcesses.getScheduledBy(),
	                interviewProcesses.getRemarks()
	        );
	    }

	    public static InterviewProcesses mapToInterviewProcesses(InterviewsRequestDto interviewRequestDto) {
	        InterviewProcesses interviewProcesses = new InterviewProcesses();
	        interviewProcesses.setId( interviewRequestDto.getId());
	        interviewProcesses.setProcessName( interviewRequestDto.getProcessName());
	        interviewProcesses.setInterviewDate( interviewRequestDto.getInterviewDate());
	        interviewProcesses.setInterviewTime( interviewRequestDto.getInterviewTime());
	        interviewProcesses.setStatus( interviewRequestDto.getStatus());
	        return interviewProcesses;
	    }
	
	
}
