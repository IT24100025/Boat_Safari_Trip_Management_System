package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Boat;
import com.se2030.BoatSafariManagement.model.Booking;
import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.model.BookingDetails;
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
                        "GuideId, DriverId, BoatId, SpecialRequests FROM Booking WHERE BookingId = ?",
                (rs, rowNum) -> {
                    Booking b = new Booking();
                    b.setBookingId(rs.getInt("BookingId"));
                    b.setCustomerId(rs.getInt("CustomerId"));
                    b.setTripId(rs.getInt("TripId"));
                    b.setBookingDate(rs.getTimestamp("BookingDate").toLocalDateTime());
                    b.setStatus(rs.getString("Status"));
                    b.setNumOfGuests(rs.getInt("NumOfGuests"));
                    b.setTotalPrice(rs.getBigDecimal("TotalPrice"));
                    b.setGuideId((Integer) rs.getObject("GuideId"));
                    b.setDriverId((Integer) rs.getObject("DriverId"));
                    b.setBoatId((Integer) rs.getObject("BoatId"));
                    b.setSpecialRequests(rs.getString("SpecialRequests"));
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
    public List<BookingDetails> getAllBookings() {
        return jdbc.query(
                "SELECT" +
                        "    b.BookingId," +
                        "    b.CustomerId," +
                        "    t.TripName," +
                        "    b.BookingDate," +
                        "    b.NumOfGuests," +
                        "    b.TotalPrice," +
                        "    b.Status," +
                        "    g.FirstName as GuideName," +
                        "    d.FirstName as DriverName," +
                        "    bt.BoatName," +
                        "    b.SpecialRequests " +
                        "FROM" +
                        "    booking b" +
                        "        LEFT OUTER JOIN " +
                        "    [User] d ON b.DriverId = d.UserId" +
                        "        LEFT OUTER JOIN" +
                        "    [User] g ON b.GuideId = g.UserId" +
                        "        LEFT OUTER JOIN" +
                        "    Boat bt ON b.BoatId = bt.BoatId" +
                        "        JOIN" +
                        "    Trip t ON b.TripId = t.TripId;",
                new BeanPropertyRowMapper<>(BookingDetails.class)
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

