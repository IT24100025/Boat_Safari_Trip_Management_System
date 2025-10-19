package com.example.boatsafarisalarysystem.repository;

import com.example.boatsafarisalarysystem.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
}