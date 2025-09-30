package com.boatsafarimanagement.service;

import com.boatsafarimanagement.model.Staff;
import com.boatsafarimanagement.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    private final StaffRepository staffRepo;

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepo = staffRepository;
    }

    /**
     * Fetch all staff members with a given role.
     * @param role the role to filter by (e.g. "Guide" or "Driver")
     * @return list of matching Staff objects
     */
    public List<Staff> getStaffByRole(String role) {
        return staffRepo.findByRole(role);
    }

    /**
     * Optionally, fetch all staff regardless of role.
     * @return list of all Staff objects
     */
    public List<Staff> getAllStaff() {
        return staffRepo.findAll();
    }
}
