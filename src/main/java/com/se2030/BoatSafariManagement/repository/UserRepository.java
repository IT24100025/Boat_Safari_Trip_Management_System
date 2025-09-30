package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getInt("UserId"));
        user.setEmail(rs.getString("Email"));
        user.setFirstName(rs.getString("FirstName"));
        user.setLastName(rs.getString("LastName"));
        user.setPassword(rs.getString("Password"));
        user.setRole(rs.getString("Role"));
        user.setCreatedDate(rs.getObject("CreatedDate", LocalDateTime.class));
        return user;
    };

    public User findByEmail(String email) {
        String sql = "SELECT * FROM [User] WHERE Email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper, email);
        } catch (Exception e) {
            return null;
        }
    }

    public int save(User user) {
        String sql = "INSERT INTO [User] (Email, FirstName, LastName, Password, Role, CreatedDate) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole());
            ps.setObject(6, user.getCreatedDate());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void saveStaff(int userId) {
        String sql = "INSERT INTO Staff (StaffId, Availability) VALUES (?, 1)";
        jdbcTemplate.update(sql, userId);
    }

    public void saveSystemAdmin(int userId) {
        String sql = "INSERT INTO SystemAdmin (AdminId) VALUES (?)";
        jdbcTemplate.update(sql, userId);
    }

    // Helper method to get JdbcTemplate for other repositories if needed
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}