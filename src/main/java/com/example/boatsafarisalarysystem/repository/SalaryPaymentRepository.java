package com.example.boatsafarisalarysystem.repository;

import com.example.boatsafarisalarysystem.entity.SalaryPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryPaymentRepository extends JpaRepository<SalaryPayment, Integer> {
}