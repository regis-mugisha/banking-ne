package com.java_ne.banking.repository;

import com.java_ne.banking.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
