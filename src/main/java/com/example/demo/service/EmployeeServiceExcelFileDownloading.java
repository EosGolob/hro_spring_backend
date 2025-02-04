package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.example.demo.entity.Employee;

public interface EmployeeServiceExcelFileDownloading {

	ByteArrayOutputStream exportFilteredEmployees(List<Employee> filteredEmployees);

}
