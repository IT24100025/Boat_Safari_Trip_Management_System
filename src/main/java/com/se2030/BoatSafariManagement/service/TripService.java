package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.Trip;
import com.se2030.BoatSafariManagement.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public void cancelTrip(int id, String reason) {
        tripRepository.cancelTrip(id, reason);
    }

    // New method to delete trip
    public void deleteTrip(int id) {
        tripRepository.deleteTrip(id);
    }
}