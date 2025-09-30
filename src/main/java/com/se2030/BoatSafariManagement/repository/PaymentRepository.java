package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PaymentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("PaymentId"));
        payment.setBookingId(rs.getInt("BookingId"));
        payment.setAmount(rs.getBigDecimal("Amount"));
        payment.setPaymentDate(rs.getObject("PaymentDate", LocalDateTime.class));
        payment.setPaymentMethod(rs.getString("PaymentMethod"));
        payment.setStatus(rs.getString("Status"));
        payment.setTransactionId(rs.getString("TransactionId"));
        return payment;
    };

    public List<Payment> findAll() {
        String sql = "SELECT * FROM Payment ORDER BY PaymentDate DESC";
        return jdbcTemplate.query(sql, paymentRowMapper);
    }
}