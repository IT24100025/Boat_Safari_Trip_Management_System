package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.User;
import com.se2030.BoatSafariManagement.service.ReportService;
import com.se2030.BoatSafariManagement.service.report.ReportGenerator;
import com.se2030.BoatSafariManagement.service.report.ReportGeneratorFactory;
import com.se2030.BoatSafariManagement.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportGeneratorFactory reportGeneratorFactory;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/reports")
    public String reportsPage(@SessionAttribute(name = "user", required = false) User user, Model model) {
        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        // Check if reports feature is enabled
        if (!appConfig.isReportsEnabled()) {
            model.addAttribute("error", "Reports feature is currently disabled.");
            return "reports";
        }

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(appConfig.getDefaultReportDays()); // Use config

        model.addAttribute("startDate", startDate.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("endDate", endDate.format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("pageTitle", "Generate Reports");

        return "reports";
    }

    @PostMapping("/reports/generate")
    public String generateReportPreview(
            @SessionAttribute(name = "user", required = false) User user,
            @RequestParam String reportType,
            @RequestParam String startDate,
            @RequestParam String endDate,
            Model model) {

        if (user == null || !"Admin".equals(user.getRole())) {
            return "redirect:/login";
        }

        // Check if reports feature is enabled
        if (!appConfig.isReportsEnabled()) {
            model.addAttribute("error", "Reports feature is currently disabled.");
            return "reports";
        }

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Map<String, Object>> reportData = null;
        String reportTitle = "";

        switch (reportType) {
            case "business":
                reportData = getBusinessPerformanceReport(start, end);
                reportTitle = "Business Performance Report";
                break;
            case "financial":
                reportData = getFinancialReport(start, end);
                reportTitle = "Financial Report";
                break;
            case "maintenance":
                reportData = getMaintenanceReport(start, end);
                reportTitle = "Maintenance Report";
                break;
        }

        model.addAttribute("reportData", reportData);
        model.addAttribute("reportTitle", reportTitle);
        model.addAttribute("reportType", reportType);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("generated", true);

        return "reports";
    }

    @PostMapping("/reports/export")
    public ResponseEntity<ByteArrayResource> exportReport(
            @SessionAttribute(name = "user", required = false) User user,
            @RequestParam String reportType,
            @RequestParam String format,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        if (user == null || !"Admin".equals(user.getRole())) {
            return ResponseEntity.badRequest().build();
        }

        // Check if reports feature is enabled
        if (!appConfig.isReportsEnabled()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            List<Map<String, Object>> reportData = getReportData(reportType, start, end);
            String fileName = getFileName(reportType);

            // Use Factory Pattern to get the appropriate generator
            ReportGenerator generator = reportGeneratorFactory.getGenerator(format);
            byte[] reportBytes = generator.generateReport(reportData, reportType, start, end);

            String finalFileName = String.format("%s_%s_to_%s.%s",
                    fileName,
                    start.format(DateTimeFormatter.BASIC_ISO_DATE),
                    end.format(DateTimeFormatter.BASIC_ISO_DATE),
                    format);

            ByteArrayResource resource = new ByteArrayResource(reportBytes);

            return ResponseEntity.ok()
                    .contentType(getContentType(format))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + finalFileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Helper methods
    private List<Map<String, Object>> getReportData(String reportType, LocalDate start, LocalDate end) {
        switch (reportType) {
            case "business":
                return getBusinessPerformanceReport(start, end);
            case "financial":
                return getFinancialReport(start, end);
            case "maintenance":
                return getMaintenanceReport(start, end);
            default:
                throw new IllegalArgumentException("Invalid report type: " + reportType);
        }
    }

    // Add these methods to ReportController
    private List<Map<String, Object>> getBusinessPerformanceReport(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT 
                t.TripName,
                t.DepartureTime,
                t.Destinations,
                COUNT(b.BookingId) as TotalBookings,
                SUM(p.Amount) as TotalRevenue,
                AVG(f.Rating) as AverageRating
            FROM Trip t
            LEFT JOIN Booking b ON t.TripId = b.TripId 
                AND b.BookingDate BETWEEN ? AND ?
            LEFT JOIN Payment p ON b.BookingId = p.BookingId 
                AND p.Status = 'Successful'
            LEFT JOIN Feedback f ON b.BookingId = f.BookingId
            WHERE t.DepartureTime BETWEEN ? AND ?
            GROUP BY t.TripId, t.TripName, t.DepartureTime, t.Destinations
            ORDER BY t.DepartureTime
            """;

        return jdbcTemplate.queryForList(sql, startDate, endDate, startDate, endDate);
    }

    private List<Map<String, Object>> getFinancialReport(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT 
                p.PaymentDate,
                p.Amount,
                p.PaymentMethod,
                p.Status,
                t.TripName,
                b.NumOfGuests
            FROM Payment p
            JOIN Booking b ON p.BookingId = b.BookingId
            JOIN Trip t ON b.TripId = t.TripId
            WHERE p.PaymentDate BETWEEN ? AND ?
            ORDER BY p.PaymentDate
            """;

        return jdbcTemplate.queryForList(sql, startDate, endDate);
    }

    private List<Map<String, Object>> getMaintenanceReport(LocalDate startDate, LocalDate endDate) {
        // FIXED: Include time component and use proper date range
        String sql = """
            SELECT 
                m.MaintenanceId,
                b.BoatName,
                m.IssueDescription,
                m.DateReported,
                m.DateResolved,
                m.Cost,
                m.Status,
                u.FirstName + ' ' + u.LastName as ReportedBy
            FROM MaintenanceLog m
            JOIN Boat b ON m.BoatId = b.BoatId
            JOIN [User] u ON m.ReportedBy_AdminId = u.UserId
            WHERE m.DateReported >= ? AND m.DateReported < ?
            ORDER BY m.DateReported DESC
            """;

        // Convert to include time for proper range comparison
        LocalDate endDatePlusOne = endDate.plusDays(1);

        return jdbcTemplate.queryForList(sql, startDate.atStartOfDay(), endDatePlusOne.atStartOfDay());
    }

    private String getFileName(String reportType) {
        switch (reportType) {
            case "business": return "business_performance";
            case "financial": return "financial";
            case "maintenance": return "maintenance";
            default: return "report";
        }
    }

    private MediaType getContentType(String format) {
        switch (format.toLowerCase()) {
            case "csv": return MediaType.parseMediaType("text/csv");
            case "excel": return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            default: return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}

