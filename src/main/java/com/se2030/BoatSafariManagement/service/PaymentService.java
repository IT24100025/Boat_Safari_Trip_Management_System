package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Payment;
import com.se2030.BoatSafariManagement.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}