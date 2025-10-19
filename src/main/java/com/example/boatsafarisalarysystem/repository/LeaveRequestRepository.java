package com.example.boatsafarisalarysystem.repository;

import com.example.boatsafarisalarysystem.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
}