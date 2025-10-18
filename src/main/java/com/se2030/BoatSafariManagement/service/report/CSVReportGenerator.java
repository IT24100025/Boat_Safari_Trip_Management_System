package com.se2030.BoatSafariManagement.service.report;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class CSVReportGenerator implements ReportGenerator {

    @Override
    public byte[] generateReport(List<Map<String, Object>> data, String reportType,
                                 LocalDate startDate, LocalDate endDate) throws Exception {
        StringBuilder csvContent = new StringBuilder();

        // Header information
        csvContent.append("Boat Safari Management - ").append(getReportTypeName(reportType)).append("\n");
        csvContent.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");

        if (data != null && !data.isEmpty()) {
            // Column headers
            boolean first = true;
            for (String header : data.get(0).keySet()) {
                if (!first) csvContent.append(",");
                csvContent.append(escapeCsv(header));
                first = false;
            }
            csvContent.append("\n");

            // Data rows
            for (Map<String, Object> row : data) {
                first = true;
                for (Object value : row.values()) {
                    if (!first) csvContent.append(",");
                    String cellValue = value != null ? value.toString() : "";
                    csvContent.append(escapeCsv(cellValue));
                    first = false;
                }
                csvContent.append("\n");
            }
        } else {
            csvContent.append("No data available for the selected period.\n");
        }

        return csvContent.toString().getBytes();
    }

    @Override
    public String getSupportedFormat() {
        return "csv";
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String getReportTypeName(String reportType) {
        switch (reportType) {
            case "business": return "Business Performance Report";
            case "financial": return "Financial Report";
            case "maintenance": return "Maintenance Report";
            default: return "Report";
        }
    }
}


