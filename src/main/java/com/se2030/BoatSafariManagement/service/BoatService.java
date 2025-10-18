package com.se2030.BoatSafariManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoatService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAllBoats() {
        String sql = "SELECT BoatId, BoatName, BoatType, Capacity, BoatAvailability FROM Boat ORDER BY BoatName";
        return jdbcTemplate.queryForList(sql);
    }
}