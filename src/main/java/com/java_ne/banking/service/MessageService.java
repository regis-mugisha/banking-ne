package com.java_ne.banking.service;

import com.java_ne.banking.model.Customer;
import com.java_ne.banking.model.TransactionType;
import com.java_ne.banking.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final JavaMailSender mailSender;
    private final MessageRepository messageRepository;

    public void sendMessage(Customer customer, TransactionType transaction_type, double amount, String account) {
        String emailBody = String.format("Dear %s %s Your %s of %.2f on your account %s has been Completed " +
                        "Successfully.",
                customer.getLastName(), customer.getFirstName(), transaction_type, amount, account);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(customer.getEmail());
        email.setSubject("Transaction Notification");
        email.setText(emailBody);
        mailSender.send(email);
    }

}
