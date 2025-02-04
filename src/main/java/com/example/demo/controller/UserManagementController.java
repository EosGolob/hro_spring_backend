package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ReqRes;
import com.example.demo.entity.OurUsers;
import com.example.demo.serviceImpl.UsersManagementService;

import jakarta.websocket.server.PathParam;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("*")
@RestController
public class UserManagementController {
   
	@Autowired
	private UsersManagementService usersManagementService;
	
	@PostMapping("/auth/register")
	public ResponseEntity<ReqRes> register(@RequestBody ReqRes req) {		
		return ResponseEntity.ok(usersManagementService.register(req));
	}
	
	@PostMapping("/auth/login")
	public ResponseEntity<ReqRes> login(@RequestBody ReqRes req) {		
		return ResponseEntity.ok(usersManagementService.login(req));
	}
	
	@PostMapping("/auth/refresh")
	public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req) {		
		return ResponseEntity.ok(usersManagementService.refreshToken(req));
	}
	
	@GetMapping("/admin/get-all-users")
	public ResponseEntity<ReqRes> getAllUsers() {
		return ResponseEntity.ok(usersManagementService.getAllUsers());
	}
	@GetMapping("/admin/get-process-name")
	public ResponseEntity<List<String>> getAllProcess(){
		return ResponseEntity.ok(usersManagementService.getMyProcessName());
	}
	
	@GetMapping("/admin/get-users/{userId}")
	public ResponseEntity<ReqRes> getUserById(@PathVariable Integer userId) {
		return ResponseEntity.ok(usersManagementService.getUserById(userId));
	}
	
	@PutMapping("/admin/update/{userId}")
	public ResponseEntity<ReqRes> updateUser(@PathVariable Integer userId , @RequestBody OurUsers reqres) {
		return ResponseEntity.ok(usersManagementService.updateUser(userId,reqres));
	}
	
	@GetMapping("/adminuser/get-profile")
	public ResponseEntity<ReqRes> getMyProfile() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName();
		ReqRes response = usersManagementService.getMyInfo(email);
		return ResponseEntity.status(response.getStatusCode()).body(response);
	}
	
	@DeleteMapping("/admin/delete/{userId}")
	public ResponseEntity<ReqRes> deleteUser(@PathVariable Integer userId) {
		return ResponseEntity.ok(usersManagementService.deleteUser(userId));
	}
}
