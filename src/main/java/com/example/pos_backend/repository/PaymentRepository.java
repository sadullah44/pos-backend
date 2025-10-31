package com.example.pos_backend.repository;

import com.example.pos_backend.model.Order; // Bu importu ekleyin
import com.example.pos_backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Bu importu ekleyin

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // YENİ EKLENEN METOT:
    // Spring Data JPA, bu metodun adından ("findByOrder")
    // 'Payment' sınıfındaki 'order' alanına göre
    // arama yapması gerektiğini ANLAR ve SQL sorgusunu kendi yazar.
    // Bu, "Beyin" katmanının parçalı ödemelerin toplamını hesaplaması için GEREKLİDİR.
    List<Payment> findByOrder(Order order);
}