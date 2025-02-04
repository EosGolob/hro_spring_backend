package com.example.demo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InterviewsRequestDto {
	private Long id;
	private Long employeeId;
	private String processName;
	private Date interviewDate;
	private String interviewTime;
	private String status;	
	private String scheduledBy;
	private String remarks;
}
