package com.java_ne.banking.dto;


import lombok.Data;


@Data
public class BankingRequestDTO {
    private String account;
    private Double amount;
    private String type;
}
