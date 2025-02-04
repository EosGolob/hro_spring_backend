package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobProfileGenderCountDto {
		 private String jobProfile;
		 private String gender;
		 private Long count;
	}
