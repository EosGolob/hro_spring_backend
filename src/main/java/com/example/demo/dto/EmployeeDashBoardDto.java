package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDashBoardDto {

//	private Long id;
	private Long totalEmp;
	private Long hiredCount;
	private Long rejectedCount;
	private Long inProcessApplicationCount;
	private Long registerCountOnDateWise;
	
}
