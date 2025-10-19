package com.example.boatsafarisalarysystem.repository;

import com.example.boatsafarisalarysystem.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Integer> {
}