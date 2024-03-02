package com.springproject.expenses.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.springproject.expenses.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long>{
	List<Expense> findByUserId(Long userId);
}
