package com.example.demo.dto;

import java.util.Date;
import java.util.List;

import com.example.demo.entity.InterviewProcesses;
import com.example.demo.entity.StatusHistory;

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
public class EmployeeDto {
	
	private Long id;
	private String fullName;	
	private String email; 	
	private String jobProfile;	
	private String Qualification;	
	private Long mobileNo;	
	private String permanentAddress;	
	private String currentAddress;	
	private String gender;	
	private String previousOrganisation;	
    private Date  dob;	
    private String maritalStatus;	
    private String refferal;
    private String aadharFilename;
    private String initialStatus;
    private String processesStatus;
    private String hrStatus;
    private String managerStatus;
    private List<StatusHistory> statusHistories;
    private List<InterviewProcesses> interviewProcesses;
	private String aadhaarNumber;
	private String  languages;
	private Float experience;
	private String source;
	private String subSource;
	private Date creationDate;
	private String lastInterviewAssin;
	private String reMarksByHr;
	private String reMarksByManager;
	private String profileScreenRemarks;
	private String workExp;
	

}
