package com.springproject.expenses.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.springproject.expenses.model.Expense;
import com.springproject.expenses.model.User;
import com.springproject.expenses.service.ExpenseService;
import com.springproject.expenses.service.UserService;
import com.springproject.expenses.utils.MailUtils;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	ExpenseService expenseService;
	@Autowired
	MailUtils mailUtils;
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@GetMapping({ "/", "/login" })
	public String userLogin(HttpSession session) {
		if (session.getAttribute("activeUser") != null) {
			return "index";
		}
		return "login";
	}

	@PostMapping("/login")
	public String postLogin(Model model, @RequestParam("password") String password, @RequestParam("email") String email,
			@RequestParam(name = "remember", required = false) boolean remember, HttpSession session) {
		User user = userService.findUserByEmail(email);
		if (userService.verifyPassword(password, user, bCryptPasswordEncoder) != true) {
			model.addAttribute("error", "Invalid email or password");
			return "login";
		} else {
			session.setAttribute("activeUser", user);
			List<Expense> unfilteredExpenses = new ArrayList<>();
			unfilteredExpenses = expenseService.findExpenseByUserId(user.getId());
			LocalDate today = LocalDate.now();
			List<Expense> filteredList = new ArrayList<>();
			for (Expense exp : unfilteredExpenses) {
				if (exp.getDate().isEqual(today)) {
					filteredList.add(exp);
				}
			}
			session.setAttribute("todayList", filteredList);
			List<Expense> sortedList = unfilteredExpenses.stream()
					.sorted(Comparator.comparingDouble(Expense::getAmount).reversed()).limit(5)
					.collect(Collectors.toList());
			List<Expense> topExpenses = sortedList.stream().limit(3).collect(Collectors.toList());
			session.setAttribute("topExpenses", topExpenses);
			if (remember) {
				session.setMaxInactiveInterval(-1);
				return "index";
			}
			session.setMaxInactiveInterval(500);
			return "index";
		}
	}

	@GetMapping("/signup")
	public String getUserSignup(HttpSession session) {
		if (session.getAttribute("activeUser") != null) {
			session.invalidate();
		}
		return "signup";
	}

	@PostMapping("/signup")
	public String postSignup(@ModelAttribute User user, Model model, HttpSession session) {
		if (user.getPassword().equals(user.getPassword2())) {
			try {
				// Generate OTP
				Random random = new Random();
				int randomNumber = random.nextInt(900000) + 100000;
				String otp = Integer.toString(randomNumber);
				session.setAttribute("otp", otp);
				session.setAttribute("signupUser", user);

				// Send OTP via email
				mailUtils.sendEmail(user.getEmail(), otp);

				return "redirect:/verifyotp";
			} catch (Exception e) {
				model.addAttribute("error", "An error occurred during signup. Please try again.");
				return "signup";
			}
		}
		model.addAttribute("error", "Passwords do not match");
		return "signup";
	}
	@GetMapping("/verifyotp")
    public String verifyOtp(HttpSession session) {
        if (session.getAttribute("signupUser") != null) {
            return "signupOtp";
        }
        return "redirect:/signup";
    }

    @PostMapping("/verifyotp")
    public String postVerifyOtp(@RequestParam("otp") String otp, Model model, HttpSession session) {
        if (session.getAttribute("signupUser") != null) {
            String sessionOtp = (String) session.getAttribute("otp");
            if (sessionOtp.equals(otp)) {
                User user = (User) session.getAttribute("signupUser");
                try {
                    // Encrypt password and add user to database
                    String hashedPassword = bCryptPasswordEncoder.encode(user.getPassword());
                    user.setPassword(hashedPassword);
                    userService.userSignup(user);

                    session.removeAttribute("signupUser");
                    session.removeAttribute("otp");

                    model.addAttribute("success", "Account created successfully");
                    return "login";
                } catch (DataIntegrityViolationException e) {
                    model.addAttribute("dupError", user.getEmail() + " already exists");
                    return "signup";
                }
            } else {
                model.addAttribute("error", "OTP is incorrect.");
                return "signupOtp";
            }
        }
        return "redirect:/signup";
    }

	@GetMapping("/forgot")
	public String forgotPassword() {

		return "forget";
	}

	@PostMapping("/forgot")
	public String postforgot(@RequestParam("email") String email, Model model, HttpSession session) {
		User user = userService.findUserByEmail(email);
		if (user == null) {
			model.addAttribute("error", "Email doesn't exist");
			return "forget";
		}
		session.setAttribute("forgotUser", user);
		Random random = new Random();
		int randomNumber = random.nextInt(900000) + 100000;
		String otp = Integer.toString(randomNumber);
		session.setAttribute("otp", otp);
		mailUtils.sendEmail(email, otp);
		return "redirect:/recover";
	}

	@GetMapping("/recover")
	public String recover(HttpSession session) {
		if (session.getAttribute("forgotUser") != null) {
			return "otp";
		}
		return "forget";
	}

	@PostMapping("/recover")
	public String postRecover(@RequestParam("otp") String otp, @ModelAttribute User user, Model model,
			HttpSession session) {
		if (session.getAttribute("forgotUser") != null) {
			String sessionOtp = (String) session.getAttribute("otp");
			if (sessionOtp.equals(otp)) {
				return "newpassword";
			} else {
				model.addAttribute("error", "OTP is incorrect.");
				return "otp";
			}
		}
		return "forget";
	}

	@GetMapping("/newpassword")
	public String newPassword(HttpSession session) {
		if (session.getAttribute("forgotUser") != null) {
			return "Newpassword";
		}
		return "forget";
	}

	@PostMapping("/newpassword")
	public String postPassword(Model model, @RequestParam("password") String password,
			@RequestParam("password2") String password2, HttpSession session) {
		if (session.getAttribute("forgotUser") != null) {
			if (password.equals(password2)) {
				String hashedPassword = bCryptPasswordEncoder.encode(password);
				User user = (User) session.getAttribute("forgotUser");
				userService.updateByEmail(user.getEmail(), hashedPassword);
				return "/login";
			}
			model.addAttribute("error", "Passwords do not match");
			return "/newpassword";
		}
		return "forget";

	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "/login";
	}
}
