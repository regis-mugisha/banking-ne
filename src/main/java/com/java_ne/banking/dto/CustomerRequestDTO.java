package com.java_ne.banking.dto;

import lombok.Data;
import java.time.LocalDate;


@Data
public class CustomerRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private LocalDate dob;

}
