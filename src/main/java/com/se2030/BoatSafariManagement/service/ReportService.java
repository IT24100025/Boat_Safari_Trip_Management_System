package com.se2030.BoatSafariManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getBusinessPerformanceReport(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT 
                t.TripName,
                t.DepartureTime,
                t.Destinations,
                COUNT(b.BookingId) as TotalBookings,
                SUM(p.Amount) as TotalRevenue,
                AVG(f.Rating) as AverageRating
            FROM Trip t
            LEFT JOIN Booking b ON t.TripId = b.TripId 
                AND b.BookingDate BETWEEN ? AND ?
            LEFT JOIN Payment p ON b.BookingId = p.BookingId 
                AND p.Status = 'Successful'
            LEFT JOIN Feedback f ON b.BookingId = f.BookingId
            WHERE t.DepartureTime BETWEEN ? AND ?
            GROUP BY t.TripId, t.TripName, t.DepartureTime, t.Destinations
            ORDER BY t.DepartureTime
            """;

        return jdbcTemplate.queryForList(sql, startDate, endDate, startDate, endDate);
    }

    public List<Map<String, Object>> getFinancialReport(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT 
                p.PaymentDate,
                p.Amount,
                p.PaymentMethod,
                p.Status,
                t.TripName,
                b.NumOfGuests
            FROM Payment p
            JOIN Booking b ON p.BookingId = b.BookingId
            JOIN Trip t ON b.TripId = t.TripId
            WHERE p.PaymentDate BETWEEN ? AND ?
            ORDER BY p.PaymentDate
            """;

        return jdbcTemplate.queryForList(sql, startDate, endDate);
    }

    public List<Map<String, Object>> getMaintenanceReport(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT 
                m.MaintenanceId,
                b.BoatName,
                m.IssueDescription,
                m.DateReported,
                m.DateResolved,
                m.Cost,
                m.Status,
                u.FirstName + ' ' + u.LastName as ReportedBy
            FROM MaintenanceLog m
            JOIN Boat b ON m.BoatId = b.BoatId
            JOIN [User] u ON m.ReportedBy_AdminId = u.UserId
            WHERE m.DateReported BETWEEN ? AND ?
            ORDER BY m.DateReported
            """;

        return jdbcTemplate.queryForList(sql, startDate, endDate);
    }
}