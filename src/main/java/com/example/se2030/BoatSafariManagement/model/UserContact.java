package com.example.se2030.BoatSafariManagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContact {
    private Integer userId;    // Simple Integer instead of User object
    private String contactNo;

    // Simple composite key class without JPA
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserContactId implements Serializable {
        private Integer userId;    // Changed from 'user' to 'userId' for clarity
        private String contactNo;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserContactId that = (UserContactId) o;
            return Objects.equals(userId, that.userId) &&
                    Objects.equals(contactNo, that.contactNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, contactNo);
        }
    }
}