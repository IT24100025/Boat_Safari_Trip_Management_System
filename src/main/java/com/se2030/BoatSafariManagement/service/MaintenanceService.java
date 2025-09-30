package com.se2030.BoatSafariManagement.service;

import com.se2030.BoatSafariManagement.model.MaintenanceLog;
import com.se2030.BoatSafariManagement.repository.MaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public List<MaintenanceLog> getAllMaintenanceLogs() {
        return maintenanceRepository.findAll();
    }

    public void updateMaintenanceStatus(int id, String status) {
        maintenanceRepository.updateStatus(id, status);
    }
}