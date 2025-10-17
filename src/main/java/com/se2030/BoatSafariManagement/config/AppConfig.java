package com.se2030.BoatSafariManagement.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    private static AppConfig instance;

    @Value("${app.report.default-days:30}")
    private int defaultReportDays;

    @Value("${app.pagination.page-size:10}")
    private int pageSize;

    @Value("${app.security.session-timeout:1800}") // 30 minutes in seconds
    private int sessionTimeout;

    @Value("${app.feature.reports.enabled:true}")
    private boolean reportsEnabled;

    @Value("${app.feature.maintenance.enabled:true}")
    private boolean maintenanceEnabled;

    // Private constructor to prevent instantiation
    private AppConfig() {
        // Singleton pattern
    }

    // Static method to get instance (Spring will manage this as singleton)
    public static AppConfig getInstance() {
        return instance;
    }

    @PostConstruct
    private void init() {
        instance = this;
    }

    // Getters
    public int getDefaultReportDays() {
        return defaultReportDays;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public boolean isReportsEnabled() {
        return reportsEnabled;
    }

    public boolean isMaintenanceEnabled() {
        return maintenanceEnabled;
    }

    // Utility methods
    public boolean isFeatureEnabled(String feature) {
        switch (feature.toLowerCase()) {
            case "reports": return reportsEnabled;
            case "maintenance": return maintenanceEnabled;
            default: return true;
        }
    }
}


