package com.springproject.expenses.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.springproject.expenses.model.Expense;
import com.springproject.expenses.model.User;
import com.springproject.expenses.service.ExpenseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ExpenseController {
	@Autowired
	ExpenseService expenseService;
	@GetMapping("/new-expense")
	public String newExpense(HttpSession session) {
		if(session.getAttribute("activeUser")!=null) {
			return "input";
		}
		return "login";
	}
	@PostMapping("/new-expense")
	public String postNewExpense(@ModelAttribute Expense expense,HttpSession session) {
		if(session.getAttribute("activeUser")!=null) {
	    User user = (User) session.getAttribute("activeUser");
		expense.setUser(user);	
	    expenseService.saveExpense(expense);
	    List<Expense> unfilteredExpenses= new ArrayList<>();
		unfilteredExpenses=expenseService.findExpenseByUserId(user.getId());
		LocalDate today = LocalDate.now();
		List<Expense> filteredList = new ArrayList<>();
        for (Expense exp : unfilteredExpenses) {
            if (exp.getDate().isEqual(today)) {
                filteredList.add(exp);
            }
        }
		session.setAttribute("todayList",filteredList);
		
		return "index";
		}
		return "/login";
	}
	@GetMapping("/expenses")
	public String expenses(HttpSession session,@ModelAttribute Expense expense,Model model) {
		if(session.getAttribute("activeUser")!=null) {	
			User user=(User) session.getAttribute("activeUser");
		model.addAttribute("allList", expenseService.findExpenseByUserId(user.getId()));
		return "expenses";
		}
		return "/login";
		
	}
}
