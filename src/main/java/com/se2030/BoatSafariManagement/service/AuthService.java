package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional
    public User register(User user, String role) {
        String plainPassword = user.getPassword();
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        String hashed = encoder.encode(plainPassword);
        user.setPassword(hashed);
        user.setRole(role);
        user.setCreatedDate(java.time.LocalDateTime.now());

        // Save user and get the generated UserId
        int userId = userRepository.save(user);

        // If registering as Admin, create Staff and SystemAdmin records
        if ("Admin".equals(role)) {
            // First create Staff record
            userRepository.saveStaff(userId);
            // Then create SystemAdmin record
            userRepository.saveSystemAdmin(userId);
        }

        // Return the user with the generated ID
        user.setUserId(userId);
        return user;
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}