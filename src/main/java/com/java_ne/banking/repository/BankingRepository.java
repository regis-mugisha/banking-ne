package com.java_ne.banking.repository;

import com.java_ne.banking.model.Banking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankingRepository extends JpaRepository<Banking, Long> {
}
