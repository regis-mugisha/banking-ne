package com.java_ne.banking.service;

import com.java_ne.banking.dto.BankingRequestDTO;
import com.java_ne.banking.model.Banking;
import com.java_ne.banking.model.Customer;
import com.java_ne.banking.model.TransactionType;
import com.java_ne.banking.repository.BankingRepository;
import com.java_ne.banking.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankingService {
    private final BankingRepository bankingRepository;
    private final CustomerRepository customerRepository;


    public Banking saveWithdraw(BankingRequestDTO request) {
        Customer customer =
                customerRepository.findByAccount(request.getAccount()).orElseThrow(() -> new RuntimeException(
                        "Account doesn't exist"));

        double amount = request.getAmount();
        TransactionType transactionType = TransactionType.valueOf(request.getType().toUpperCase());

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        switch (transactionType) {
            case SAVE:
                customer.setBalance(customer.getBalance() + amount);
                break;
            case WITHDRAW:
                if (amount <= customer.getBalance()) {
                    customer.setBalance(customer.getBalance() - amount);
                } else {
                    throw new RuntimeException("Insufficient balance");
                }
                break;
            default:
                throw new RuntimeException("Invalid transaction type, save or withdraw");

        }

        customerRepository.save(customer);

        Banking transaction = Banking.builder()
                .customer(customer)
                .type(transactionType)
                .account(customer.getAccount())
                .amount(amount)
                .bankingDateTime(LocalDateTime.now())
                .build();

        return bankingRepository.save(transaction);
    }

    public Banking transfer(BankingRequestDTO request, String receiverAccount) {
        Customer sender =
                customerRepository.findByAccount(request.getAccount()).orElseThrow(() -> new RuntimeException(
                        "Sender Account doesn't exist"));
        Customer receiver = customerRepository.findByAccount(receiverAccount).orElseThrow(() -> new RuntimeException("Receiver Account doesn't exist"));

        double amount = request.getAmount();
        TransactionType transactionType = TransactionType.valueOf(request.getType().toUpperCase());

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (transactionType == TransactionType.TRANSFER) {
            if (amount >= sender.getBalance()) {
                throw new RuntimeException("Insufficient balance");
            }

            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);
        } else {
            throw new RuntimeException("Invalid transaction type, transfer only");
        }

        Banking transaction = Banking.builder()
                .customer(sender)
                .type(transactionType)
                .amount(amount)
                .account(receiver.getAccount())
                .bankingDateTime(LocalDateTime.now())
                .build();

        return bankingRepository.save(transaction);


    }
}