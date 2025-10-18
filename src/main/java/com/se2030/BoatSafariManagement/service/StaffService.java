package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Staff;
import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    private StaffRepository staffRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public List<Staff> getAllStaff() {
        return staffRepository.findAllStaffWithUserDetails();
    }

    public Staff getStaffById(int id) {
        return staffRepository.findById(id);
    }

    public void saveStaff(Staff staff, String role) {
        // First create user
        User user = new User();
        user.setEmail(staff.getEmail());
        user.setFirstName(staff.getFirstName());
        user.setLastName(staff.getLastName());
        user.setPassword("default123"); // Default password
        user.setRole(role);
        user.setCreatedDate(java.time.LocalDateTime.now());

        staffRepository.saveStaffUser(user);

        // Get the generated UserId
        int userId = staffRepository.getLastInsertedUserId();
        staff.setStaffId(userId);

        // Save staff details
        staffRepository.saveStaffDetails(staff);
    }

    public void updateStaff(Staff staff) {
        staffRepository.updateStaff(staff);
    }

    public void deleteStaff(int id) {
        staffRepository.deleteStaff(id);
    }
}