//package com.example.demo.serviceImpl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.example.demo.entity.User;
//import com.example.demo.repository.UserRepository;
//
//@Service
//public class UserDetailsServiceImpl  implements UserDetailsService{
//    
//	@Autowired
//	private UserRepository userRepository;
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		// TODO Auto-generated method stub
//		User user = this.userRepository.findByUserName(username);
//		if(user == null) {
//			System.out.println("user not found");
//			throw new UsernameNotFoundException("No user found !!");
//		}
//		return user;
//	}
//
//}
