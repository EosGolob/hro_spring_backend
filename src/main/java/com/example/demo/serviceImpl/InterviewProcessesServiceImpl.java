package com.example.demo.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EmployeeDto;
import com.example.demo.dto.InterviewsRequestDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.InterviewProcesses;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.InterviewProcessesMapper;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.InterviewProcessesRepository;
import com.example.demo.service.InterviewProcessesService;

@Service
public class InterviewProcessesServiceImpl implements InterviewProcessesService {

	@Autowired
	private InterviewProcessesRepository interviewProcessesRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public InterviewsRequestDto addInterviewProcess(InterviewsRequestDto interviewProcessesDto) {
		Employee employee = employeeRepository.findById(interviewProcessesDto.getEmployeeId())
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		InterviewProcesses interviewProcesses = InterviewProcessesMapper.mapToInterviewProcesses(interviewProcessesDto);
		interviewProcesses.setEmployee(employee);
		InterviewProcesses savedInterviewProcess = interviewProcessesRepository.save(interviewProcesses);

		return InterviewProcessesMapper.mapToInterviewProcessesDto(savedInterviewProcess);
	}

	@Override
	public List<InterviewsRequestDto> getAllInterviewProcessesByEmployeeId(Long employeeId) {
		List<InterviewProcesses> interviewProcessesList = interviewProcessesRepository.findAllByEmployeeId(employeeId);
		return interviewProcessesList.stream().map(InterviewProcessesMapper::mapToInterviewProcessesDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<InterviewsRequestDto> getAttendedProcesses(Long employeeId) {
		List<InterviewProcesses> interviewProcessesList = interviewProcessesRepository.interviewProcessAttenndedByEmp(employeeId);
		return interviewProcessesList.stream().map(InterviewProcessesMapper::mapToInterviewProcessesDto)
				.collect(Collectors.toList());
	}

	
	
}
