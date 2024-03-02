package com.springproject.expenses.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.expenses.model.Expense;
import com.springproject.expenses.repository.ExpenseRepository;
import com.springproject.expenses.service.ExpenseService;
@Service
public class ExpenseServiceImpl implements ExpenseService{
    @Autowired   
	ExpenseRepository expenseRepo;
	
	@Override
	public List<Expense> findExpenseByUserId(Long userId) {
		
		return expenseRepo.findByUserId(userId);
	}
	@Override
	public Expense getExpensesById(Long id) {
	 return expenseRepo.findById(id).get();
		
	}
	@Override
	public void saveExpense(Expense expense) {
		expenseRepo.save(expense);
	}
	@Override
	public void deleteExpense(Long id) {
		expenseRepo.deleteById(id);
		
	}

	@Override
	public void updateExpense(Expense exp) {
		expenseRepo.save(exp);
		
	}

}
