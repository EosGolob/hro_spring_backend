package com.example.demo.controller;

import com.example.demo.dto.InterviewsRequestDto;
import com.example.demo.entity.InterviewProcesses;
import com.example.demo.mapper.InterviewProcessesMapper;
import com.example.demo.service.InterviewProcessesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(originPatterns = "*")
//@CrossOrigin(origins = {"http://localhost:3001","http://20.193.159.186:3001"})
@RestController
@RequestMapping("/api/interviews")
public class InterviewProcessesController {

//    @Autowired
    private InterviewProcessesService interviewProcessesService;
    
    
    
    public InterviewProcessesController(InterviewProcessesService interviewProcessesService) {
		this.interviewProcessesService = interviewProcessesService;
	}

	@PostMapping
    public ResponseEntity<InterviewsRequestDto> addInterviewProcess(@RequestBody InterviewsRequestDto interviewProcessesDto) {
        InterviewsRequestDto createdInterviewProcess = interviewProcessesService.addInterviewProcess(interviewProcessesDto);
        return new ResponseEntity<>(createdInterviewProcess, HttpStatus.CREATED);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<InterviewsRequestDto>> getAllInterviewProcessesByEmployeeId(@PathVariable("employeeId") Long employeeId) {
        List<InterviewsRequestDto> interviewProcessesList = interviewProcessesService.getAllInterviewProcessesByEmployeeId(employeeId);
        return new ResponseEntity<>(interviewProcessesList, HttpStatus.OK);
    }
    
    @GetMapping("/getAttendenedInterview/{employeeId}")
    public ResponseEntity<List<InterviewsRequestDto>> getAttentendedInterviewByEmployee(@PathVariable("employeeId") Long employeeid) {
    	List<InterviewsRequestDto> interviewProcessesList = interviewProcessesService.getAttendedProcesses(employeeid);
        return new ResponseEntity<>(interviewProcessesList,HttpStatus.OK);
    }
    
}
