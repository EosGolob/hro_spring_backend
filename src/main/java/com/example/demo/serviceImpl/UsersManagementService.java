package com.example.demo.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ReqRes;
import com.example.demo.entity.Notification;
import com.example.demo.entity.OurUsers;
import com.example.demo.repository.UsersRepository;

@Service
public class UsersManagementService {
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private JWTUtilsImpl jwtUtilsImpl;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

/**
	public ReqRes register(ReqRes registrationRequest) {
		ReqRes resp = new ReqRes();
		try {
			OurUsers ourUser = new OurUsers();
			ourUser.setEmail(registrationRequest.getEmail());
			ourUser.setCity(registrationRequest.getCity());
			ourUser.setRole(registrationRequest.getRole());
//			ourUser.setName(registrationRequest.getName().substring(0,1).toUpperCase()+registrationRequest.getName().substring(1));
			String name = registrationRequest.getName();
			if (name != null && !name.isEmpty()) {
			    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
			    ourUser.setName(name);
			}
			ourUser.setProcess(registrationRequest.getProcess().trim().toUpperCase());
			ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
			if(registrationRequest.getEmail() != null && !registrationRequest.getEmail().isEmpty()) {
				if(usersRepository.findByEmail(registrationRequest.getEmail())!= null) {
					resp.setStatusCode(400);
					resp.setError("Email is already in use");
					return resp;
				}
			}
			OurUsers ourUsersResult = usersRepository.save(ourUser);
			if (ourUsersResult.getId() > 0) {
				resp.setOurUsers((ourUsersResult));
				resp.setMessage("Users save Successfully");
				resp.setStatusCode(200);

			}
		} catch (Exception e) {

			resp.setStatusCode(500);
			resp.setError(e.getMessage());
			resp.setError("Incorrect password");
		}
		return resp;
	}
*/
	
	public ReqRes register(ReqRes registrationRequest) {
	    ReqRes resp = new ReqRes();
	    try {
	        String email = registrationRequest.getEmail();
	        if (email != null && !email.isEmpty()) {
	            Optional<OurUsers> existingUser = usersRepository.findByEmail(email);
	            if (existingUser.isPresent()) {
	                resp.setStatusCode(400);
	                resp.setError("Emp id is already in use");
	                return resp;
	            }
	        } else {
	            resp.setStatusCode(400);
	            resp.setError("Emp id cannot be empty");
	            return resp;
	        }

	        // Create and populate the new user object
	        OurUsers ourUser = new OurUsers();
	        ourUser.setEmail(email);
	        ourUser.setCity(registrationRequest.getCity());
	        ourUser.setRole(registrationRequest.getRole());

	        String name = registrationRequest.getName();
	        if (name != null && !name.isEmpty()) {
	            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	            ourUser.setName(name);
	        }

	        ourUser.setProcess(registrationRequest.getProcess().trim().toUpperCase());
	        ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));

	        // Save the new user
	        OurUsers ourUsersResult = usersRepository.save(ourUser);
	        if (ourUsersResult.getId() > 0) {
	            resp.setOurUsers(ourUsersResult);
	            resp.setMessage("User saved successfully");
	            resp.setStatusCode(200);
	        }
	    } catch (Exception e) {
	        resp.setStatusCode(500);
	        resp.setError("An error occurred: " + e.getMessage());
	    }
	    return resp;
	}

	public ReqRes login(ReqRes loginRequest) {
		ReqRes response = new ReqRes();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			var user = usersRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
			var jwt = jwtUtilsImpl.generateToken(user);
			var refreshToken = jwtUtilsImpl.generateRefreshToken(new HashMap<>(), user);
			response.setStatusCode(200);
			response.setToken(jwt);
			response.setRole(user.getRole());
			response.setProcess(user.getProcess());
			response.setName(user.getName());
			response.setRefreshToken(refreshToken);
			response.setCity(user.getCity());
			response.setExpirationTime("24Hrs");
			response.setMessage("SuccessFull login ");
			
			
		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage("Incorrect Password");
		}
		return response;
	}

	public ReqRes refreshToken(ReqRes refreshTokenReqiest) {
		ReqRes response = new ReqRes();

		try {
			String ourEmail = jwtUtilsImpl.extractUsername(refreshTokenReqiest.getToken());
			OurUsers users = usersRepository.findByEmail(ourEmail).orElseThrow();
			if (jwtUtilsImpl.isTokenValid(refreshTokenReqiest.getToken(), users)) {
				var jwt = jwtUtilsImpl.generateToken(users);
				response.setStatusCode(200);
				response.setToken(jwt);
				response.setRefreshToken(refreshTokenReqiest.getToken());
				response.setExpirationTime("24Hrs");
				response.setMessage("Successfully Refreshed Token");
			}
			response.setStatusCode(200);
			return response;
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	public ReqRes getAllUsers() {
		ReqRes reqRes = new ReqRes();

		try {
			List<OurUsers> result = usersRepository.findAll();
			if (!result.isEmpty()) {
				reqRes.setOurUserList(result);
				reqRes.setStatusCode(200);
				reqRes.setMessage("Successfull");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("No users found");
			}
			return reqRes;
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage(" Error occurred " + e.getMessage());
			return reqRes;
		}
	}

	public ReqRes getUserById(Integer id) {
		ReqRes reqRes = new ReqRes();

		try {
			OurUsers usersById = usersRepository.findById(id).orElseThrow(() -> new RuntimeException("user Not Found"));

			reqRes.setOurUsers(usersById);
			reqRes.setStatusCode(200);
			reqRes.setMessage("User with id " + id + "found successfully");

		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage(" Error occurred " + e.getMessage());

		}
		return reqRes;
	}

	public ReqRes deleteUser(Integer userId) {
		ReqRes reqRes = new ReqRes();

		try {
			Optional<OurUsers> userOptional = usersRepository.findById(userId);
			if (userOptional.isPresent()) {
				usersRepository.deleteById(userId);
				reqRes.setStatusCode(200);
				reqRes.setMessage("User deleted successfully");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("User not found for deletion");
			}
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage(" Error occurred " + e.getMessage());

		}
		return reqRes;
	}

	public ReqRes updateUser(Integer userId, OurUsers updatedUser) {

		ReqRes reqRes = new ReqRes();
		try {
			Optional<OurUsers> userOptional = usersRepository.findById(userId);
			if (userOptional.isPresent()) {
				OurUsers existingUser = userOptional.get();
				existingUser.setEmail(updatedUser.getEmail());
				existingUser.setName( updatedUser.getName());
				existingUser.setCity(updatedUser.getCity());
				existingUser.setRole(updatedUser.getRole());

				if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
					existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
				}

				OurUsers savedUser = usersRepository.save(existingUser);
				reqRes.setOurUsers(savedUser);
				reqRes.setStatusCode(200);
				reqRes.setMessage("User update successfully");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("User not found for update");
			}
		} catch (Exception e) {

			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occurred while updating user " + e.getMessage());
		}

		return reqRes;

	}
	
	
	public ReqRes getMyInfo(String email) {
		ReqRes  reqRes = new ReqRes();
		
		try {
			Optional<OurUsers> userOptional = usersRepository.findByEmail(email);
			if(userOptional.isPresent()) {
			   reqRes.setOurUsers(userOptional.get());
			   reqRes.setStatusCode(200);
			   reqRes.setMessage("Successfull");
			}else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("User not found for update");
			}
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error occured while getting use info " +e.getMessage());
		}
		return reqRes;
	}
	
	public List<String> getMyProcessName() {
            List<String> processes = usersRepository.findDistinctProcesses();
        return processes ;
    }
	
	public static final String capitalize(String str)   
	{  
	if (str == null || str.length() == 0) return str;  
	return str.substring(0, 1).toUpperCase() + str.substring(1);  
	}  
}