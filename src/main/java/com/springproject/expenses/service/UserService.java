package com.springproject.expenses.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.springproject.expenses.model.User;

public interface UserService {
	String userSignup(User user);

	User findUserByEmail(String email);

	boolean verifyPassword(String password, User user, BCryptPasswordEncoder encoder);

	User updateByEmail(String email, String password);
}
