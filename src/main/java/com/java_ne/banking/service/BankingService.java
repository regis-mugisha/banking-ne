package com.java_ne.banking.service;

import com.java_ne.banking.dto.BankingRequestDTO;
import com.java_ne.banking.exception.AccountNotFoundException;
import com.java_ne.banking.exception.InsufficientBalanceException;
import com.java_ne.banking.exception.InvalidTransactionTypeException;
import com.java_ne.banking.model.Banking;
import com.java_ne.banking.model.Customer;
import com.java_ne.banking.model.TransactionType;
import com.java_ne.banking.repository.BankingRepository;
import com.java_ne.banking.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankingService {
    private final BankingRepository bankingRepository;
    private final CustomerRepository customerRepository;
    private final MessageService messageService;


    public Banking saveWithdraw(BankingRequestDTO request) {
        Customer customer =
                customerRepository.findByAccount(request.getAccount()).orElseThrow(() -> new AccountNotFoundException(
                        "Account not found"));

        double amount = request.getAmount();
        String account = request.getAccount();
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
                    throw new InsufficientBalanceException("Insufficient balance");
                }
                break;
            default:
                throw new InvalidTransactionTypeException("Invalid transaction type, save or withdraw");

        }

        customerRepository.save(customer);

        messageService.sendMessage(customer, transactionType, amount, account);

        Banking transaction = Banking.builder()
                .customer(customer)
                .type(transactionType)
                .account(customer.getAccount())
                .amount(amount)
                .bankingDateTime(LocalDateTime.now())
                .build();

        return bankingRepository.save(transaction);
    }

    @Transactional
    public Banking transfer(BankingRequestDTO request, String receiverAccount) {
        Customer sender =
                customerRepository.findByAccount(request.getAccount()).orElseThrow(() -> new AccountNotFoundException(
                        "Sender's account not found"));
        Customer receiver =
                customerRepository.findByAccount(receiverAccount).orElseThrow(() -> new AccountNotFoundException("Receiver's " +
                        "account not found"));

        double amount = request.getAmount();
        TransactionType transactionType = TransactionType.valueOf(request.getType().toUpperCase());
        String account = request.getAccount();

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (transactionType == TransactionType.TRANSFER) {
            if (amount >= sender.getBalance()) {
                throw new InsufficientBalanceException("Insufficient balance");
            }

            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + amount);

            customerRepository.save(sender);
            customerRepository.save(receiver);
        } else {
            throw new InvalidTransactionTypeException("Invalid transaction type, transfer only");
        }


        messageService.sendMessage(sender, transactionType, amount, account);


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