package com.springproject.expenses.service;

import java.util.List;

import com.springproject.expenses.model.Expense;

public interface ExpenseService {
	List<Expense> findExpenseByUserId(Long userId);
	void saveExpense(Expense expense);
	void deleteExpense(Long id);
	void updateExpense(Expense exp);
	Expense getExpensesById(Long id);
}
