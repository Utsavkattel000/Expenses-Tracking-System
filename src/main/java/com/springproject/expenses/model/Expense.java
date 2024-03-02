package com.springproject.expenses.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="expenses")
@Getter
@Setter
public class Expense {
@Id
@GeneratedValue
private long id;
private String subject;
private float amount;
private LocalDate date;
@ManyToOne
@JoinColumn(name="user_id")
private User user;
}
