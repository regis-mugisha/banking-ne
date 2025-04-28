package com.java_ne.banking.service;

import com.java_ne.banking.dto.CustomerRequest;
import com.java_ne.banking.model.Customer;
import com.java_ne.banking.repository.CustomerRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer createCustomer(CustomerRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .dob(request.getDob())
                .mobile(request.getMobile())
                .balance(0.0)
                .account(generateAccountNumber())
                .lastUpdateDateTime(LocalDateTime.now())
                .build();

        return customerRepository.save(customer);
    }

    private String generateAccountNumber() {
        return "ACC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
