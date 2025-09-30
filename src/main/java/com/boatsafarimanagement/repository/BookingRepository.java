package com.boatsafarimanagement.repository;

import com.boatsafarimanagement.model.Boat;
import com.boatsafarimanagement.model.Booking;
import com.boatsafarimanagement.model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepository {
    @Autowired
    private JdbcTemplate jdbc;

    // Fetch a single booking
    public Booking getBooking(int bookingId) {
        return jdbc.queryForObject(
                "SELECT BookingId, CustomerId, TripId, BookingDate, Status, NumOfGuests, TotalPrice, " +
                        "GuideId, DriverId, BoatId FROM Booking WHERE BookingId = ?",
                (rs, rowNum) -> {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("BookingId"));
                    b.setCustomerId(rs.getInt("CustomerId"));
                    b.setTripId(rs.getInt("TripId"));
                    b.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
                    b.setStatus(rs.getString("Status"));
                    b.setNoOfGuests(rs.getInt("NumOfGuests"));
                    b.setTripPrice(rs.getBigDecimal("TotalPrice"));
                    b.setGuideId((Integer) rs.getObject("GuideId"));
                    b.setDriverId((Integer) rs.getObject("DriverId"));
                    b.setAssignedBoatId((Integer) rs.getObject("BoatId"));
                    return b;
                },
                bookingId
        );
    }


    public List<Staff> getStaffByRole(String role) {
        return jdbc.query(
                "SELECT s.StaffId, u.FirstName + ' ' + u.LastName AS name " +
                        "FROM Staff s JOIN [User] u ON s.StaffId = u.UserId " +
                        "WHERE s.Role = ? AND s.Availability = '1'",
                (rs, rowNum) -> {
                    Staff st = new Staff();
                    st.setStaffId(rs.getInt("StaffId"));
                    st.setName(rs.getString("name"));
                    return st;
                },
                role
        );
    }

    public void updateAvailability(int guideId, int driverId, int boatID){
        jdbc.update(
                "UPDATE Staff SET Availability = '0' WHERE StaffId = ?",
                guideId
        );

        jdbc.update(
                "UPDATE Staff SET Availability = '0' WHERE StaffId = ?",
                driverId
        );

        jdbc.update(
                "UPDATE Boat SET BoatAvailability = '0' WHERE BoatId = ?",
                boatID
        );
    }

    //get all bookings
    public List<Booking> getAllBookings() {
        return jdbc.query(
                "SELECT BookingId," +
                        "CustomerId," +
                        "TripId," +
                        "BookingDate," +
                        "NumOfGuests," +
                        "TotalPrice," +
                        "Status," +
                        "GuideId," +
                        "DriverId," +
                        "BoatId," +
                        "SpecialRequests FROM Booking",
                new BeanPropertyRowMapper<>(Booking.class)
        );
    }

    public void assignGuideAndDriver(int bookingId, int guideId, int driverId) {
        jdbc.update(
                "UPDATE Booking SET GuideId = ?, DriverId = ? WHERE BookingId = ?",
                guideId, driverId, bookingId
        );
    }

    // List available boats
    public List<Boat> getAvailableBoats() {
        return jdbc.query(
                "SELECT BoatId, BoatName FROM Boat WHERE BoatAvailability = '1'",
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
                "UPDATE Booking SET BoatId = ? WHERE BookingId = ?",
                boatId, bookingId
        );
    }

}

