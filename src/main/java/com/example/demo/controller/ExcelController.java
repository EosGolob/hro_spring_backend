package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeServiceExcelFileDownloading;

public class ExcelController {
	    @Autowired
	    private EmployeeServiceExcelFileDownloading employeeService;

	    @Autowired
	    private EmployeeRepository employeeRepository; // Assuming you have this to fetch employees

//	    @GetMapping("/download/employees")
//	    public ResponseEntity<byte[]> downloadFilteredEmployees(@RequestParam List<Long> employeeIds) {
//	        List<Employee> filteredEmployees = employeeRepository.findAllById(employeeIds);
//	        ByteArrayOutputStream outputStream = employeeService.exportFilteredEmployees(filteredEmployees);
//
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.add("Content-Disposition", "attachment; filename=filtered_employees.xlsx");
//	        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
//	    }
}
