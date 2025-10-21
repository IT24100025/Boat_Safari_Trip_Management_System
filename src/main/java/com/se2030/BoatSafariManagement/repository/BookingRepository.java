package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Boat;
import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.model.BookingDetails;
import com.se2030.BoatSafariManagement.model.Customer;
import com.se2030.BoatSafariManagement.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepository {
    @Autowired
    private JdbcTemplate jdbc;

    // Fetch a single booking - FIXED VERSION
    public Booking getBooking(int bookingId) {
        try {
            String sql = "SELECT b.BookingId, b.CustomerId, b.TripId, b.BookingDate, b.Status, " +
                    "b.NumOfGuests, b.TotalPrice, b.SpecialRequests, " +
                    "b.GuideId, b.DriverId, b.BoatId, " + // ADDED THESE COLUMNS
                    "c.UserId as CustomerUserId, c.FirstName, c.LastName, c.Email, " +
                    "t.TripId as Trip_TripId, t.TripName, t.DepartureTime, t.Destinations, t.BasePrice " +
                    "FROM Booking b " +
                    "JOIN Customer cust ON b.CustomerId = cust.CustomerId " +
                    "JOIN [User] c ON cust.CustomerId = c.UserId " +
                    "JOIN Trip t ON b.TripId = t.TripId " +
                    "WHERE b.BookingId = ?";

            return jdbc.queryForObject(sql, (rs, rowNum) -> {
                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("BookingId"));

                // Create and set Customer object
                Customer customer = new Customer();
                customer.setUserId(rs.getInt("CustomerId"));
                customer.setFirstName(rs.getString("FirstName"));
                customer.setLastName(rs.getString("LastName"));
                customer.setEmail(rs.getString("Email"));
                booking.setCustomer(customer);

                // Create and set Trip object
                Trip trip = new Trip();
                trip.setTripId(rs.getInt("Trip_TripId")); // Use aliased name
                trip.setTripName(rs.getString("TripName"));
                trip.setDepartureTime(rs.getTimestamp("DepartureTime").toLocalDateTime());
                trip.setDestinations(rs.getString("Destinations"));
                trip.setBasePrice(rs.getBigDecimal("BasePrice"));
                booking.setTrip(trip);

                booking.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
                booking.setStatus(rs.getString("Status"));
                booking.setNumOfGuests(rs.getInt("NumOfGuests"));
                booking.setTotalPrice(rs.getBigDecimal("TotalPrice"));
                booking.setSpecialRequests(rs.getString("SpecialRequests"));

                // Handle nullable staff assignments - FIXED
                Integer guideId = rs.getObject("GuideId", Integer.class);
                Integer driverId = rs.getObject("DriverId", Integer.class);
                Integer boatId = rs.getObject("BoatId", Integer.class);

                booking.setGuideId(guideId);
                booking.setDriverId(driverId);
                booking.setBoatId(boatId);

                return booking;
            }, bookingId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("No booking found with ID: " + bookingId);
            return null;
        } catch (Exception e) {
            System.out.println("Error fetching booking: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<BookingDetails> getAllBookings() {
        String sql = "SELECT " +
                "b.BookingId, b.CustomerId, t.TripName, b.BookingDate, " +
                "b.NumOfGuests, b.TotalPrice, b.Status, " +
                "g.FirstName + ' ' + g.LastName as GuideName, " +
                "d.FirstName + ' ' + d.LastName as DriverName, " +
                "bt.BoatName, b.SpecialRequests " +
                "FROM booking b " +
                "LEFT OUTER JOIN [User] d ON b.DriverId = d.UserId " +
                "LEFT OUTER JOIN [User] g ON b.GuideId = g.UserId " +
                "LEFT OUTER JOIN Boat bt ON b.BoatId = bt.BoatId " +
                "JOIN Trip t ON b.TripId = t.TripId";

        return jdbc.query(sql, (rs, rowNum) -> {
            BookingDetails details = new BookingDetails();
            details.setBookingId(rs.getInt("BookingId"));
            details.setCustomerId(rs.getInt("CustomerId"));
            details.setTripName(rs.getString("TripName"));
            details.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
            details.setNumOfGuests(rs.getInt("NumOfGuests"));
            details.setTotalPrice(rs.getBigDecimal("TotalPrice"));
            details.setStatus(rs.getString("Status"));
            details.setGuideName(rs.getString("GuideName"));
            details.setDriverName(rs.getString("DriverName"));
            details.setBoatName(rs.getString("BoatName"));
            details.setSpecialRequests(rs.getString("SpecialRequests"));
            return details;
        });
    }

    public List<Staff> getStaffByRole(String role) {
        return jdbc.query(
                "SELECT s.StaffId, u.FirstName, u.LastName, u.Email " +
                        "FROM Staff s JOIN [User] u ON s.StaffId = u.UserId " +
                        "WHERE u.Role = ? AND s.Availability = 1",
                (rs, rowNum) -> {
                    Staff st = new Staff();
                    st.setStaffId(rs.getInt("StaffId"));
                    st.setFirstName(rs.getString("FirstName"));
                    st.setLastName(rs.getString("LastName"));
                    st.setEmail(rs.getString("Email"));
                    return st;
                },
                role
        );
    }

    public void updateAvailability(int guideId, int driverId, int boatID){
        jdbc.update(
                "UPDATE Staff SET Availability = 0 WHERE StaffId = ?",
                guideId
        );

        jdbc.update(
                "UPDATE Staff SET Availability = 0 WHERE StaffId = ?",
                driverId
        );

        jdbc.update(
                "UPDATE Boat SET BoatAvailability = 0 WHERE BoatId = ?",
                boatID
        );
    }

    // Update other methods to use your current Booking model structure
    public void assignGuideAndDriver(int bookingId, int guideId, int driverId, int boatId) {
        String sql = "UPDATE Booking SET GuideId = ?, DriverId = ?, BoatId = ?, Status = 'Assigned' WHERE BookingId = ?";
        int rows = jdbc.update(sql, guideId, driverId, boatId, bookingId);
        System.out.println("BookingRepository: Updated " + rows + " rows for booking " + bookingId);
    }

    public void updateBookingStatus(int bookingId) {
        jdbc.update(
                "UPDATE Booking SET Status = 'Confirmed' WHERE BookingId = ?",
                bookingId
        );
    }

    // List available boats
    public List<Boat> getAvailableBoats() {
        return jdbc.query(
                "SELECT BoatId, BoatName FROM Boat WHERE BoatAvailability = 1",
                (rs, rowNum) -> {
                    Boat boat = new Boat();
                    boat.setBoatId(rs.getInt("BoatId"));
                    boat.setBoatName(rs.getString("BoatName"));
                    return boat;
                }
        );
    }

    // Update assigned boat
    public void assignBoatToBooking(int bookingId, int boatId) {
        jdbc.update(
                "UPDATE Booking SET BoatId = ? WHERE BookingId = ?", // Changed from AssignedBoatId to BoatId
                boatId, bookingId
        );
    }

    public int getTodayBookingCount() {
        String sql = "SELECT COUNT(*) FROM booking WHERE CAST(BookingDate AS DATE) = CAST(GETDATE() AS DATE)";
        return jdbc.queryForObject(sql, Integer.class);
    }

    public int getUnassignedBookingCount() {
        String sql = ""
                + "SELECT COUNT(*) "
                + "FROM booking "
                + "WHERE GuideId IS NULL "  // Changed from AssignedStaffId to GuideId
                + "  AND DriverId IS NULL " // Changed from AssignedStaffId to DriverId
                + "  AND BoatId IS NULL";   // Changed from AssignedBoatId to BoatId
        return jdbc.queryForObject(sql, Integer.class);
    }

    public Staff getStaffEmailAndName(int staffId) {
        return jdbc.queryForObject(
                "SELECT u.Email, u.FirstName + ' ' + u.LastName AS FullName " +
                        "FROM [User] u " +
                        "WHERE u.UserId = ?",
                (rs, rowNum) -> {
                    Staff info = new Staff();
                    info.setEmail(rs.getString("Email"));
                    // Fix the name splitting to handle edge cases
                    String fullName = rs.getString("FullName");
                    String[] nameParts = fullName.split(" ", 2);
                    info.setFirstName(nameParts[0]);
                    info.setLastName(nameParts.length > 1 ? nameParts[1] : "");
                    return info;
                },
                staffId
        );
    }

    // Get trip details for email
    public String getTripDetails(int bookingId) {
        return jdbc.queryForObject(
                "SELECT t.TripName + ' - ' + t.Destinations + ' (Date: ' + " +
                        "CONVERT(VARCHAR, b.BookingDate) + ')' " +
                        "FROM Booking b " +
                        "JOIN Trip t ON b.TripId = t.TripId " +
                        "WHERE b.BookingId = ?",
                String.class,
                bookingId
        );
    }
}