package com.example.demo.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InterviewProcesses {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY,optional = false )
	@JoinColumn(name = "employee_id" , nullable = false)
	@JsonBackReference
	private Employee employee;
	
	@OneToMany(mappedBy = "interviewProcess", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<StatusHistory> statusHistories;
	
	@Column(name = "process_name")
	private String processName;
	
	@Column(name = "interview_date")
	private Date interviewDate;
	
	@Column(name = "interview_time")
	private String interviewTime;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "scheduled_By")
	private String scheduledBy;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name ="jobProfile")
	private String jobProfile;
	  
	@Column(name = "remarksManager")
	private String remarksManager;
	
//	@ManyToOne(fetch = FetchType.LAZY , optional = true)
//	@JoinColumn(name = "managerDetails_id")
//	private ManagerDetails managerDetails;
	




	
	
	
	
}
