package com.springproject.expenses.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component 
public class MailUtils {
	@Autowired
    private JavaMailSender javaMailSender;
	
	public void sendEmail(String email, String otp) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("OTP");
        msg.setText(otp);
        javaMailSender.send(msg);

    }
}
