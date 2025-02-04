package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSearchDto {
	
	private Long id;
	private String fullName;
	private String email;
	private String initialStatus;
	private String processesStatus;
	private String hrStatus;
	private String managerStatus;
	private String aadhaarNumber;
	private String lastInterviewAssin;
	private String reMarksByHr;
	private String reMarksByManager;
	private String profileScreenRemarks;
}
