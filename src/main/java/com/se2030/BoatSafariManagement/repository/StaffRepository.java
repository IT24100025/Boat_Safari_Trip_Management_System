package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StaffRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Staff> staffRowMapper = (rs, rowNum) -> {
        Staff staff = new Staff();
        staff.setStaffId(rs.getInt("StaffId"));
        staff.setFirstName(rs.getString("FirstName"));
        staff.setLastName(rs.getString("LastName"));
        staff.setEmail(rs.getString("Email"));
        staff.setSalary(rs.getBigDecimal("Salary"));
        staff.setLaneNumber(rs.getString("LaneNumber"));
        staff.setCity(rs.getString("City"));
        staff.setAvailability(rs.getBoolean("Availability"));
        staff.setRole(rs.getString("Role"));
        return staff;
    };

    public List<Staff> findAllStaffWithUserDetails() {
        String sql = "SELECT s.StaffId, u.FirstName, u.LastName, u.Email," +
                "s.Salary, s.LaneNumber, s.City, s.Availability, u.Role " +
                "FROM Staff s INNER JOIN [User] u ON s.StaffId = u.UserId";
        return jdbcTemplate.query(sql, staffRowMapper);
    }

    public Staff findById(int id) {
        try {
            String sql = "SELECT s.StaffId, u.FirstName, u.LastName, u.Email," +
                    "s.Salary, s.LaneNumber, s.City, s.Availability, u.Role " +
                    "FROM Staff s INNER JOIN [User] u ON s.StaffId = u.UserId WHERE s.StaffId = ?";
            return jdbcTemplate.queryForObject(sql, staffRowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveStaffUser(User user) {
        String sql = "INSERT INTO [User] (Email, FirstName, LastName, Password, Role, CreatedDate) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getRole(),
                user.getCreatedDate());
    }

    public void saveStaffDetails(Staff staff) {
        String sql = "INSERT INTO Staff (StaffId, Salary, LaneNumber, City, Availability) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                staff.getStaffId(),
                staff.getSalary(),
                staff.getLaneNumber(),
                staff.getCity(),
                staff.getAvailability());
    }

    public void updateStaff(Staff staff) {
        // Update User table
        String userSql = "UPDATE [User] SET FirstName = ?, LastName = ?, Email = ? WHERE UserId = ?";
        jdbcTemplate.update(userSql,
                staff.getFirstName(),
                staff.getLastName(),
                staff.getEmail(),
                staff.getStaffId());

        // Update Staff table
        String staffSql = "UPDATE Staff SET Salary = ?, LaneNumber = ?, City = ?, Availability = ? WHERE StaffId = ?";
        jdbcTemplate.update(staffSql,
                staff.getSalary(),
                staff.getLaneNumber(),
                staff.getCity(),
                staff.getAvailability(),
                staff.getStaffId());
    }

    public void deleteStaff(int id) {
        // Delete from Staff table (User will be deleted due to CASCADE)
        jdbcTemplate.update("DELETE FROM Staff WHERE StaffId = ?", id);
    }

    public int getLastInsertedUserId() {
        String sql = "SELECT MAX(UserId) FROM [User]";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}