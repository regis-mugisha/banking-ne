package com.java_ne.banking.controller;

import com.java_ne.banking.dto.BankingRequestDTO;
import com.java_ne.banking.model.Banking;
import com.java_ne.banking.model.Customer;
import com.java_ne.banking.service.BankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class BankingController {

    private final BankingService bankingService;


    @PostMapping
    public ResponseEntity<Banking> saveWithdraw(@RequestBody BankingRequestDTO request) {
        Banking transaction = bankingService.saveWithdraw(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @PostMapping("/transfer/{account}")
    public ResponseEntity<Banking> transferWithdraw(@RequestBody BankingRequestDTO request,
                                                    @PathVariable("account") String account) {
        Banking transaction = bankingService.transfer(request, account);

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }
}
