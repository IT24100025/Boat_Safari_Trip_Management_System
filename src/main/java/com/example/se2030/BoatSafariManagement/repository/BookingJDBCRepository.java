package com.example.se2030.BoatSafariManagement.repository;

import com.example.se2030.BoatSafariManagement.model.Booking;
import com.example.se2030.BoatSafariManagement.model.Customer;
import com.example.se2030.BoatSafariManagement.model.Payment;
import com.example.se2030.BoatSafariManagement.model.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookingJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    // Customer methods
    public Optional<Customer> findCustomerByEmail(String email) {
        String sql = "SELECT u.UserId, u.Email, u.FirstName, u.LastName, u.Password, u.Role, u.CreatedDate " +
                "FROM [User] u INNER JOIN Customer c ON u.UserId = c.CustomerId WHERE u.Email = ?";
        try {
            Customer customer = jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), email);
            return Optional.ofNullable(customer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Customer saveCustomer(Customer customer) {
        // Save User first
        String userSql = "INSERT INTO [User] (Email, FirstName, LastName, Password, Role, CreatedDate) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getEmail());
            ps.setString(2, customer.getFirstName());
            ps.setString(3, customer.getLastName());
            ps.setString(4, customer.getPassword());
            ps.setString(5, customer.getRole());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        Integer userId = keyHolder.getKey().intValue();

        // Save Customer
        String customerSql = "INSERT INTO Customer (CustomerId) VALUES (?)";
        jdbcTemplate.update(customerSql, userId);

        customer.setUserId(userId);
        return customer;
    }

    public void saveUserContact(Integer userId, String contactNo) {
        try {
            String sql = "INSERT INTO UserContact (UserId, ContactNo) VALUES (?, ?)";
            jdbcTemplate.update(sql, userId, contactNo);
        } catch (Exception e) {
            System.out.println("Warning: Could not save user contact: " + e.getMessage());
        }
    }

    // Trip methods
    public List<Trip> findAllTripsAfter(LocalDateTime now) {
        try {
            String sql = "SELECT TripId, TripName, Duration, DepartureTime, Destinations, Availability, Description, BasePrice " +
                    "FROM Trip WHERE DepartureTime > ? ORDER BY DepartureTime";
            return jdbcTemplate.query(sql, new TripRowMapper(), now);
        } catch (Exception e) {
            System.out.println("Error fetching trips: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Trip findTripById(Integer tripId) {
        try {
            String sql = "SELECT TripId, TripName, Duration, DepartureTime, Destinations, Availability, Description, BasePrice " +
                    "FROM Trip WHERE TripId = ?";
            return jdbcTemplate.queryForObject(sql, new TripRowMapper(), tripId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void updateTripAvailability(Integer tripId, Integer newAvailability) {
        String sql = "UPDATE Trip SET Availability = ? WHERE TripId = ?";
        jdbcTemplate.update(sql, newAvailability, tripId);
    }

    // Booking methods
    public Booking saveBooking(Booking booking) {
        String sql = "INSERT INTO Booking (CustomerId, TripId, BookingDate, BookingTime, NumOfGuests, TotalPrice, SpecialRequests, Status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, booking.getCustomer().getUserId());
            ps.setInt(2, booking.getTrip().getTripId());
            ps.setTimestamp(3, Timestamp.valueOf(booking.getBookingDate()));
            ps.setTime(4, Time.valueOf(booking.getBookingTime()));
            ps.setInt(5, booking.getNumOfGuests());
            ps.setBigDecimal(6, booking.getTotalPrice());
            ps.setString(7, booking.getSpecialRequests());
            ps.setString(8, booking.getStatus());
            return ps;
        }, keyHolder);

        Integer bookingId = keyHolder.getKey().intValue();
        booking.setBookingId(bookingId);
        return booking;
    }

    public List<Booking> findBookingsByTripIdAndDate(Integer tripId, java.time.LocalDate date) {
        try {
            String sql = "SELECT b.BookingId, b.CustomerId, b.TripId, b.NumOfGuests, b.TotalPrice, b.Status, b.SpecialRequests " +
                    "FROM Booking b WHERE b.TripId = ? AND CAST(b.BookingDate AS DATE) = ?";
            return jdbcTemplate.query(sql, new BookingRowMapper(), tripId, date);
        } catch (Exception e) {
            System.out.println("Error fetching bookings: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Integer getTotalBookedSeatsForTrip(Integer tripId, java.time.LocalDate date) {
        try {
            String sql = "SELECT COALESCE(SUM(NumOfGuests), 0) FROM Booking WHERE TripId = ? AND CAST(BookingDate AS DATE) = ?";
            return jdbcTemplate.queryForObject(sql, Integer.class, tripId, date);
        } catch (Exception e) {
            System.out.println("Error calculating booked seats: " + e.getMessage());
            return 0;
        }
    }

    // Payment methods
    public Payment savePayment(Payment payment) {
        String sql = "INSERT INTO Payment (BookingId, PaymentDate, Amount, Status, PaymentMethod, TransactionId) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, payment.getBookingId());
            ps.setTimestamp(2, Timestamp.valueOf(payment.getPaymentDate()));
            ps.setBigDecimal(3, payment.getAmount());
            ps.setString(4, payment.getStatus());
            ps.setString(5, payment.getPaymentMethod());
            ps.setString(6, payment.getTransactionId());
            return ps;
        }, keyHolder);

        Integer paymentId = keyHolder.getKey().intValue();
        payment.setPaymentId(paymentId);
        return payment;
    }

    // RowMapper classes
    private static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setUserId(rs.getInt("UserId"));
            customer.setEmail(rs.getString("Email"));
            customer.setFirstName(rs.getString("FirstName"));
            customer.setLastName(rs.getString("LastName"));
            customer.setPassword(rs.getString("Password"));
            customer.setRole(rs.getString("Role"));
            Timestamp createdDate = rs.getTimestamp("CreatedDate");
            if (createdDate != null) {
                customer.setCreatedDate(createdDate.toLocalDateTime());
            }
            return customer;
        }
    }

    private static class TripRowMapper implements RowMapper<Trip> {
        @Override
        public Trip mapRow(ResultSet rs, int rowNum) throws SQLException {
            Trip trip = new Trip();
            trip.setTripId(rs.getInt("TripId"));
            trip.setTripName(rs.getString("TripName"));
            trip.setDuration(rs.getInt("Duration"));
            Timestamp departureTime = rs.getTimestamp("DepartureTime");
            if (departureTime != null) {
                trip.setDepartureTime(departureTime.toLocalDateTime());
            }
            trip.setDestinations(rs.getString("Destinations"));
            trip.setAvailability(rs.getInt("Availability"));
            trip.setDescription(rs.getString("Description"));
            trip.setBasePrice(rs.getBigDecimal("BasePrice"));
            return trip;
        }
    }

    private static class BookingRowMapper implements RowMapper<Booking> {
        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            Booking booking = new Booking();
            booking.setBookingId(rs.getInt("BookingId"));
            booking.setNumOfGuests(rs.getInt("NumOfGuests"));
            booking.setTotalPrice(rs.getBigDecimal("TotalPrice"));
            booking.setStatus(rs.getString("Status"));
            booking.setSpecialRequests(rs.getString("SpecialRequests"));
            return booking;
        }
    }
}