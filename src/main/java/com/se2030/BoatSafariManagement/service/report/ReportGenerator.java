package com.se2030.BoatSafariManagement.service.report;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportGenerator {
    byte[] generateReport(List<Map<String, Object>> data, String reportType,
                          LocalDate startDate, LocalDate endDate) throws Exception;
    String getSupportedFormat();
}

