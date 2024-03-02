package com.springproject.expenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springproject.expenses.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);

	boolean existsByEmail(String email);
}
