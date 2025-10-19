package com.example.boatsafarisalarysystem.repository;

import com.example.boatsafarisalarysystem.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
}