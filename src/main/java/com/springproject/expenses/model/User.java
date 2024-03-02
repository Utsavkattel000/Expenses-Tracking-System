package com.springproject.expenses.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user")
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue
	 private long id;
	private String username;
	@Column(unique = true)
	private String email;
	private String password;
	@Transient
	private String password2;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Expense> expense = new ArrayList<>();
	@Transient
	private String otp;
}
