package com.example.pos_backend.repository;

import com.example.pos_backend.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    // İleride "Nakit" yöntemini adıyla bulmak için
    // PaymentMethod findByMethodName(String methodName);
}