package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.model.Payment;
import com.se2030.BoatSafariManagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;
import java.math.BigDecimal; // Import BigDecimal

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/payments")
    public String paymentsPage(@SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);

        // Calculate total amount - safe approach
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Payment payment : payments) {
            if (payment.getAmount() != null) {
                totalAmount = totalAmount.add(payment.getAmount());
            }
        }
        model.addAttribute("totalAmount", totalAmount);

        return "payments";
    }
}