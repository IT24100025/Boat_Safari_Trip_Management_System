package com.se2030.BoatSafariManagement.repository;

import com.se2030.BoatSafariManagement.model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StaffRepository {

    private final JdbcTemplate jdbc;

    @Autowired
    public StaffRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Find all staff members with a given role (e.g. "Guide" or "Driver").
     */
    public List<Staff> findByRole(String role) {
        String sql =
                "SELECT s.StaffId, u.FirstName + ' ' + u.LastName AS name, s.Role " +
                        "FROM Staff s " +
                        "JOIN [User] u ON s.StaffId = u.UserId " +
                        "WHERE s.Role = ?";
        return jdbc.query(sql, (rs, rn) -> {
            Staff st = new Staff();
            st.setStaffId(rs.getInt("StaffId"));
            st.setName(rs.getString("name"));
            st.setRole(rs.getString("Role"));
            return st;
        }, role);
    }

    /**
     * Fetch all staff members, regardless of role.
     */
    public List<Staff> findAll() {
        String sql =
                "SELECT s.StaffId, u.FirstName + ' ' + u.LastName AS name, s.Role " +
                        "FROM Staff s " +
                        "JOIN [User] u ON s.StaffId = u.UserId";
        return jdbc.query(sql, (rs, rn) -> {
            Staff st = new Staff();
            st.setStaffId(rs.getInt("StaffId"));
            st.setName(rs.getString("name"));
            st.setRole(rs.getString("Role"));
            return st;
        });
    }

}

