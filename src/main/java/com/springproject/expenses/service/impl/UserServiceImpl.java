package com.springproject.expenses.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springproject.expenses.model.User;
import com.springproject.expenses.repository.UserRepository;
import com.springproject.expenses.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
    UserRepository userRepo;
	@Override
	public String userSignup(User user) {
		if (userRepo.existsByEmail(user.getEmail())) {
			return "Email";
		}
		userRepo.save(user);
		return null;
	}

	@Override
	public User findUserByEmail(String email) {
		
		return userRepo.findByEmail(email);
	}

	@Override
	public boolean verifyPassword(String password, User user, BCryptPasswordEncoder encoder) {
		if (user != null && encoder.matches(password, user.getPassword())) {
			return true;
		} else {
		return false;
		}
	}
	
    
	@Override
		public User updateByEmail(String email, String password) {
	        User user = userRepo.findByEmail(email);
	        if (user != null) {
	            user.setPassword(password);
	            // Update other fields if needed
	            return userRepo.save(user);
	        } else {
	            // Handle case where entity with given email is not found
	            return null;
	        }
	}

}
