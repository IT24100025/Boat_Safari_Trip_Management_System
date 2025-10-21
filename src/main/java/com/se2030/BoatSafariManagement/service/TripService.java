package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.*;
import com.se2030.BoatSafariManagement.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final BoatRepository boatRepository;
    private final StaffRepository staffRepository;
    private final MaintenanceRepository maintenanceRepository;

    public TripService(TripRepository tripRepository, BoatRepository boatRepository,
                       StaffRepository staffRepository, MaintenanceRepository maintenanceRepository) {
        this.tripRepository = tripRepository;
        this.boatRepository = boatRepository;
        this.staffRepository = staffRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    
    // Add this cancelTrip method
    public boolean cancelTrip(int tripId, String reason) {
        Trip trip = tripRepository.findById(tripId);
        if (trip == null) {
            throw new RuntimeException("Trip not found with id: " + tripId);
        }

        // Check if trip is already completed or cancelled
        if ("Completed".equals(trip.getStatus()) || "Cancelled".equals(trip.getStatus())) {
            throw new RuntimeException("Cannot cancel a trip that is already " + trip.getStatus());
        }

        // Check if trip has already started
        if (trip.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot cancel a trip that has already started");
        }

        return tripRepository.cancelTrip(tripId, reason);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTripById(Integer tripId) {
        return tripRepository.findById(tripId);
    }

    public Trip createTrip(Trip trip) {
        // Validate duration
        if (trip.getDuration() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero.");
        }

        // Validate departure time is in the future
        if (trip.getDepartureTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Departure time must be in the future.");
        }

        // Check for scheduling conflicts
        if (hasSchedulingConflict(trip)) {
            throw new IllegalArgumentException("Scheduling conflict detected. Please choose a different time.");
        }

        return tripRepository.save(trip);
    }

    public Trip updateTrip(Trip trip) {
        // Validate duration
        if (trip.getDuration() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero.");
        }

        // Check for scheduling conflicts (excluding current trip)
        if (hasSchedulingConflict(trip, trip.getTripId())) {
            throw new IllegalArgumentException("Scheduling conflict detected. Please choose a different time.");
        }

        return tripRepository.update(trip);
    }

    public boolean deleteTrip(Integer tripId) {
        return tripRepository.delete(tripId);
    }

    public List<Boat> getAvailableBoatsForTrip(LocalDateTime departureTime, Integer duration) {
        return boatRepository.findAvailableBoats(departureTime, duration);
    }

    public List<Staff> getAvailableStaffForTrip(LocalDateTime departureTime, Integer duration, String role) {
        return staffRepository.findAvailableStaff(departureTime, duration, role);
    }

    public boolean isBoatAvailable(Integer boatId, LocalDateTime departureTime, Integer duration) {
        // Check if boat is under maintenance during the trip time
        LocalDateTime tripEnd = departureTime.plusMinutes(duration);
        LocalDateTime checkTime = departureTime.plusMinutes(duration / 2); // Check middle of trip

        return !maintenanceRepository.isBoatUnderMaintenance(boatId, checkTime);
    }

    public List<Trip> getTripsByDateRange(LocalDateTime start, LocalDateTime end) {
        return tripRepository.findTripsByDateRange(start, end);
    }

    public List<Staff> getStaffByRole(String role) {
        return staffRepository.findByRole(role);
    }

    public List<Boat> getAllBoats() {
        return boatRepository.findAll();
    }

    // New method to check for scheduling conflicts
    public boolean hasSchedulingConflict(Trip trip) {
        return hasSchedulingConflict(trip, null);
    }

    public boolean hasSchedulingConflict(Trip trip, Integer excludeTripId) {
        List<Trip> existingTrips = getAllTrips();
        LocalDateTime newTripStart = trip.getDepartureTime();
        LocalDateTime newTripEnd = newTripStart.plusMinutes(trip.getDuration());

        for (Trip existingTrip : existingTrips) {
            // Skip the trip we're updating - use primitive int comparison
            if (excludeTripId != null && existingTrip.getTripId() == excludeTripId) {
                continue;
            }

            LocalDateTime existingTripStart = existingTrip.getDepartureTime();
            LocalDateTime existingTripEnd = existingTripStart.plusMinutes(existingTrip.getDuration());

            // Check for time overlap
            if (newTripStart.isBefore(existingTripEnd) && existingTripStart.isBefore(newTripEnd)) {
                return true;
            }
        }

        return false;
    }

    // Method to get trips at the same time (for conflict detection)
    public List<Trip> getTripsAtSameTime(Trip trip) {
        LocalDateTime tripStart = trip.getDepartureTime();
        LocalDateTime tripEnd = tripStart.plusMinutes(trip.getDuration());

        return getAllTrips().stream()
                .filter(t -> t.getTripId() != trip.getTripId()) // Use != for primitive int comparison
                .filter(t -> {
                    LocalDateTime otherStart = t.getDepartureTime();
                    LocalDateTime otherEnd = otherStart.plusMinutes(t.getDuration());
                    return tripStart.isBefore(otherEnd) && otherStart.isBefore(tripEnd);
                })
                .collect(Collectors.toList());
    }
}