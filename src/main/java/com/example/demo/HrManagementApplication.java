package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication

public class HrManagementApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(HrManagementApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HrManagementApplication.class);
	}

//	@Bean
//	public ModelMapper modelMapper() {
//		
//		return new ModelMapper();
//		
//	}
}
