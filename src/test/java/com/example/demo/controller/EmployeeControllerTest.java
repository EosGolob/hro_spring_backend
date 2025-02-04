package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.service.EmployeeService;

public class EmployeeControllerTest {
	
	@InjectMocks
	private EmployeeController employeeController;
	
	@Mock
	private EmployeeService employeeService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}
    

	@Test
	public void getAllEmployeesSuccessTestCase() {
		List<EmployeeDto> mockEmployees = new ArrayList<>();
		EmployeeDto empDto = new EmployeeDto();
		empDto.setFullName("Amrit raj");
		empDto.setId(1l);
		mockEmployees.add(empDto);
		
		EmployeeDto empDto2 = new EmployeeDto();
		empDto2.setFullName("Jay Kumar");
		empDto2.setId(2l);
		mockEmployees.add(empDto2);
			
		when(employeeService.getAllEmployees()).thenReturn(mockEmployees);
		ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		
	}
	
	
	@Test
	public void getAllEmployeesEmptyTestCase() {
		List<EmployeeDto> mockEmployees = new ArrayList<>();
		when(employeeService.getAllEmployees()).thenReturn(mockEmployees);
		ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmployees();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0, response.getBody().size());
	}
	
	
	@Test
	public void getAllEmpSuccessTestCase() {
		List<EmployeeDto> mockObj = new ArrayList<>();
		
		EmployeeDto  empDto = new EmployeeDto();
		empDto.setFullName("Xys");
		empDto.setId(1l);
		mockObj.add(empDto);
		
		EmployeeDto empDto1 = new EmployeeDto();
		empDto1.setFullName("Lex");
		empDto1.setId(2l);
		mockObj.add(empDto1);
		
		when(employeeService.getEmployeeWithSelectedValuefiled()).thenReturn(mockObj);
		ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmp();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
		
		
	}

	@Test
	public void getAllEmpSuccessEmptyTestCase() {
		List<EmployeeDto> mockObj = new ArrayList<>();
		when(employeeService.getEmployeeWithSelectedValuefiled()).thenReturn(mockObj);
		ResponseEntity<List<EmployeeDto>> response = employeeController.getAllEmp();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(0, response.getBody().size());
		
		
	}
	
	
	@Test
	public void testUpateEmployeeStatus_Success() {
		Long employeeId = 1l;
		String newStatus = "Active";
		
		EmployeeDto updatedEmployee = new EmployeeDto();
		updatedEmployee.setId(employeeId);
		when(employeeService.updateEmployeeStatus(employeeId, newStatus)).thenReturn(updatedEmployee);
		ResponseEntity<EmployeeDto> responseEntity = employeeController.updateEmployeeStatus(employeeId, newStatus);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(updatedEmployee, responseEntity.getBody());
		
		
	}
	
	@Test 
	public void testUpdateEmployeeStatus_EmployeeNotFound() {
		Long emploeeId = 1l;
		String newStatus = "Active";
		
		when(employeeService.updateEmployeeStatus(emploeeId, newStatus)).thenThrow(new RuntimeException("Employee not found"));
		ResponseEntity<EmployeeDto> response;
		try {
			response = employeeController.updateEmployeeStatus(emploeeId, newStatus);
		} catch (RuntimeException e) {
			response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
