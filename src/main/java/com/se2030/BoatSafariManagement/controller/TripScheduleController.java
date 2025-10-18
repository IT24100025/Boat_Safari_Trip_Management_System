package com.se2030.BoatSafariManagement.controller;

import com.se2030.BoatSafariManagement.model.Trip;
import com.se2030.BoatSafariManagement.service.TripService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/trip-schedule")
public class TripScheduleController {
    private final TripService tripService;

    public TripScheduleController(TripService tripService) {
        this.tripService = tripService;
    }

    // Enhanced CalendarDay class with conflict detection
    public static class CalendarDay {
        private int dayOfMonth;
        private boolean otherMonth;
        private boolean today;
        private List<Trip> trips;
        private boolean hasConflicts;

        public CalendarDay() {}

        public CalendarDay(int dayOfMonth, boolean otherMonth, boolean today, List<Trip> trips) {
            this.dayOfMonth = dayOfMonth;
            this.otherMonth = otherMonth;
            this.today = today;
            this.trips = trips != null ? trips : new ArrayList<>();
            this.hasConflicts = calculateHasConflicts();
        }

        private boolean calculateHasConflicts() {
            if (trips == null || trips.isEmpty()) return false;

            // Enhanced conflict detection considering trip duration
            for (int i = 0; i < trips.size(); i++) {
                for (int j = i + 1; j < trips.size(); j++) {
                    Trip trip1 = trips.get(i);
                    Trip trip2 = trips.get(j);

                    LocalDateTime start1 = trip1.getDepartureTime();
                    LocalDateTime end1 = start1.plusMinutes(trip1.getDuration());
                    LocalDateTime start2 = trip2.getDepartureTime();
                    LocalDateTime end2 = start2.plusMinutes(trip2.getDuration());

                    // Check if trips overlap
                    if (start1.isBefore(end2) && start2.isBefore(end1)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public int getDayOfMonth() {
            return dayOfMonth;
        }
        public void setDayOfMonth(int dayOfMonth) {
            this.dayOfMonth = dayOfMonth;
        }

        public boolean isOtherMonth() {
            return otherMonth;
        }
        public void setOtherMonth(boolean otherMonth) {
            this.otherMonth = otherMonth;
        }

        public boolean isToday() {
            return today;
        }
        public void setToday(boolean today) {
            this.today = today;
        }

        public List<Trip> getTrips() {
            return trips;
        }
        public void setTrips(List<Trip> trips) {
            this.trips = trips;
            this.hasConflicts = calculateHasConflicts();
        }

        public boolean isHasConflicts() {
            return hasConflicts;
        }
        public void setHasConflicts(boolean hasConflicts) {
            this.hasConflicts = hasConflicts;
        }
    }

    // Conflict DTO for displaying conflict information
    public static class ConflictInfo {
        private String description;
        private LocalDate date;
        private List<Integer> tripIds;

        public ConflictInfo(String description, LocalDate date, List<Integer> tripIds) {
            this.description = description;
            this.date = date;
            this.tripIds = tripIds;
        }

        // Getters and setters
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDate getDate() {
            return date;
        }
        public void setDate(LocalDate date) {
            this.date = date;
        }

        public List<Integer> getTripIds() {
            return tripIds;
        }
        public void setTripIds(List<Integer> tripIds) {
            this.tripIds = tripIds;
        }
    }

    // Enhanced Trip DTO for displaying additional information
    public static class EnhancedTrip {
        private Trip trip;
        private boolean hasConflict;
        private String capacityStatus;
        private String conflictDetails;
        private String status; // NEW: Added status field

        public EnhancedTrip(Trip trip, boolean hasConflict, String capacityStatus, String conflictDetails, String status) {
            this.trip = trip;
            this.hasConflict = hasConflict;
            this.capacityStatus = capacityStatus;
            this.conflictDetails = conflictDetails;
            this.status = status;
        }

        // Delegate methods to the underlying trip
        public Integer getTripId() {
            return trip.getTripId();
        }
        public String getTripName() {
            return trip.getTripName();
        }
        public LocalDateTime getDepartureTime() {
            return trip.getDepartureTime();
        }
        public Integer getDuration() {
            return trip.getDuration();
        }
        public String getDestinations() {
            return trip.getDestinations();
        }
        public String getDescription() {
            return trip.getDescription();
        }
        public Integer getAvailability() {
            return trip.getAvailability();
        }
        public Double getBasePrice() {
            return trip.getBasePrice();
        }

        // Enhanced properties
        public boolean isHasConflict() {
            return hasConflict;
        }
        public String getCapacityStatus() {
            return capacityStatus;
        }
        public String getConflictDetails() {
            return conflictDetails;
        }
        public String getStatus() {
            return status;
        }
    }

    @GetMapping
    public String showSchedule(Model model) {
        List<Trip> trips = tripService.getAllTrips();

        // Calculate status for each trip
        for (Trip trip : trips) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime tripTime = trip.getDepartureTime();

            if (tripTime.toLocalDate().isEqual(now.toLocalDate())) {
                trip.setStatus("TODAY");
            } else if (tripTime.isBefore(now)) {
                trip.setStatus("PAST");
            } else {
                trip.setStatus("UPCOMING");
            }
        }

        // Enhance trips with conflict and capacity information
        List<EnhancedTrip> enhancedTrips = enhanceTripsWithConflictInfo(trips);

        // Calculate statistics
        int upcomingTrips = calculateUpcomingTrips(trips);
        int conflictCount = detectConflicts(trips).size();
        String occupancyRate = calculateOccupancyRate(trips);
        int totalTrips = trips.size();
        int availableBoats = tripService.getAllBoats().size();
        int scheduledStaff = calculateScheduledStaff(trips);

        // Calculate today and past trips count
        int todayTrips = (int) trips.stream().filter(t -> "TODAY".equals(t.getStatus())).count();
        int pastTrips = (int) trips.stream().filter(t -> "PAST".equals(t.getStatus())).count();

        // Add all attributes to model
        model.addAttribute("trips", enhancedTrips);
        model.addAttribute("upcomingTrips", upcomingTrips);
        model.addAttribute("conflictCount", conflictCount);
        model.addAttribute("occupancyRate", occupancyRate);
        model.addAttribute("totalTrips", totalTrips);
        model.addAttribute("availableBoats", availableBoats);
        model.addAttribute("scheduledStaff", scheduledStaff);
        model.addAttribute("todayTrips", todayTrips); //  Added today trips count
        model.addAttribute("pastTrips", pastTrips);   //  Added past trips count

        return "update-schedule";
    }

    @GetMapping("/test")
    public String testPage() {
        return "test";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/trip-schedule";
    }

    @GetMapping("/calendar")
    public String showCalendar(@RequestParam(required = false) String month, Model model) {
        try {
            // Parse month parameter or use current month
            YearMonth currentYearMonth;
            if (month != null && !month.isEmpty()) {
                currentYearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyy-MM"));
            } else {
                currentYearMonth = YearMonth.now();
            }

            // Get trips for the month
            LocalDateTime startOfMonth = currentYearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = currentYearMonth.atEndOfMonth().atTime(23, 59, 59);

            List<Trip> monthlyTrips = tripService.getTripsByDateRange(startOfMonth, endOfMonth);

            // Calculate status for each trip
            for (Trip trip : monthlyTrips) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime tripTime = trip.getDepartureTime();

                if (tripTime.toLocalDate().isEqual(now.toLocalDate())) {
                    trip.setStatus("TODAY");
                } else if (tripTime.isBefore(now)) {
                    trip.setStatus("PAST");
                } else {
                    trip.setStatus("UPCOMING");
                }
            }

            // Build calendar structure with trips
            List<List<CalendarDay>> calendarWeeks = buildCalendar(currentYearMonth, monthlyTrips);

            // Format month name for better display
            String monthName = currentYearMonth.getMonth().toString();
            monthName = monthName.charAt(0) + monthName.substring(1).toLowerCase();

            // Calculate statistics
            int totalTrips = monthlyTrips.size();
            int availableBoats = tripService.getAllBoats().size();
            int scheduledStaff = calculateScheduledStaff(monthlyTrips);
            List<ConflictInfo> conflicts = detectConflicts(monthlyTrips);
            int conflictCount = conflicts.size();
            String occupancyRate = calculateOccupancyRate(monthlyTrips);

            // NEW: Calculate today and past trips count for calendar view
            int todayTrips = (int) monthlyTrips.stream().filter(t -> "TODAY".equals(t.getStatus())).count();
            int pastTrips = (int) monthlyTrips.stream().filter(t -> "PAST".equals(t.getStatus())).count();

            // Add all required model attributes
            model.addAttribute("calendarWeeks", calendarWeeks);
            model.addAttribute("currentMonth", monthName);
            model.addAttribute("currentYear", currentYearMonth.getYear());
            model.addAttribute("prevMonth", currentYearMonth.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM")));
            model.addAttribute("nextMonth", currentYearMonth.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM")));

            // Add conflict and statistics attributes
            model.addAttribute("totalTrips", totalTrips);
            model.addAttribute("availableBoats", availableBoats);
            model.addAttribute("scheduledStaff", scheduledStaff);
            model.addAttribute("conflictCount", conflictCount);
            model.addAttribute("conflicts", conflicts);
            model.addAttribute("occupancyRate", occupancyRate);
            model.addAttribute("todayTrips", todayTrips); // NEW: Added for calendar
            model.addAttribute("pastTrips", pastTrips);   // NEW: Added for calendar

            return "trip-calendar";

        } catch (Exception e) {
            e.printStackTrace(); // For debugging
            model.addAttribute("errorMessage", "Error loading calendar: " + e.getMessage());
            // Fallback to current month
            YearMonth currentYearMonth = YearMonth.now();
            String monthName = currentYearMonth.getMonth().toString();
            monthName = monthName.charAt(0) + monthName.substring(1).toLowerCase();

            model.addAttribute("calendarWeeks", new ArrayList<>());
            model.addAttribute("currentMonth", monthName);
            model.addAttribute("currentYear", currentYearMonth.getYear());
            model.addAttribute("prevMonth", currentYearMonth.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM")));
            model.addAttribute("nextMonth", currentYearMonth.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM")));

            // Default values for statistics
            model.addAttribute("totalTrips", 0);
            model.addAttribute("availableBoats", 0);
            model.addAttribute("scheduledStaff", 0);
            model.addAttribute("conflictCount", 0);
            model.addAttribute("conflicts", new ArrayList<>());
            model.addAttribute("occupancyRate", "0%");
            model.addAttribute("todayTrips", 0); // NEW: Default for calendar
            model.addAttribute("pastTrips", 0);  // NEW: Default for calendar

            return "trip-calendar";
        }
    }

    private List<EnhancedTrip> enhanceTripsWithConflictInfo(List<Trip> trips) {
        List<EnhancedTrip> enhancedTrips = new ArrayList<>();

        // Group trips by date to detect conflicts
        Map<LocalDate, List<Trip>> dailyTrips = new HashMap<>();
        for (Trip trip : trips) {
            LocalDate date = trip.getDepartureTime().toLocalDate();
            dailyTrips.computeIfAbsent(date, k -> new ArrayList<>()).add(trip);
        }

        // Enhance each trip with conflict information
        for (Trip trip : trips) {
            LocalDate date = trip.getDepartureTime().toLocalDate();
            List<Trip> sameDayTrips = dailyTrips.get(date);

            boolean hasConflict = false;
            String conflictDetails = null;

            // Check for conflicts with other trips on the same day
            if (sameDayTrips != null && sameDayTrips.size() > 1) {
                for (Trip otherTrip : sameDayTrips) {
                    if (!otherTrip.getTripId().equals(trip.getTripId())) {
                        LocalDateTime start1 = trip.getDepartureTime();
                        LocalDateTime end1 = start1.plusMinutes(trip.getDuration());
                        LocalDateTime start2 = otherTrip.getDepartureTime();
                        LocalDateTime end2 = start2.plusMinutes(otherTrip.getDuration());

                        // Check if trips overlap
                        if (start1.isBefore(end2) && start2.isBefore(end1)) {
                            hasConflict = true;
                            conflictDetails = "Conflict with: " + otherTrip.getTripName() +
                                    " at " + otherTrip.getDepartureTime().toLocalTime();
                            break;
                        }
                    }
                }
            }

            // Calculate capacity status
            String capacityStatus = calculateCapacityStatus(trip.getAvailability());

            // Get the status that was calculated earlier
            String status = trip.getStatus();

            enhancedTrips.add(new EnhancedTrip(trip, hasConflict, capacityStatus, conflictDetails, status));
        }

        return enhancedTrips;
    }

    private String calculateCapacityStatus(int availability) {
        if (availability <= 0) return "FULL";
        if (availability <= 5) return "WARNING";
        return "AVAILABLE";
    }

    private List<ConflictInfo> detectConflicts(List<Trip> trips) {
        List<ConflictInfo> conflicts = new ArrayList<>();
        Map<LocalDate, List<Trip>> dailyTrips = new HashMap<>();

        // Group trips by date
        for (Trip trip : trips) {
            LocalDate date = trip.getDepartureTime().toLocalDate();
            dailyTrips.computeIfAbsent(date, k -> new ArrayList<>()).add(trip);
        }

        // Detect conflicts
        for (Map.Entry<LocalDate, List<Trip>> entry : dailyTrips.entrySet()) {
            LocalDate date = entry.getKey();
            List<Trip> dayTrips = entry.getValue();

            for (int i = 0; i < dayTrips.size(); i++) {
                for (int j = i + 1; j < dayTrips.size(); j++) {
                    Trip trip1 = dayTrips.get(i);
                    Trip trip2 = dayTrips.get(j);

                    LocalDateTime start1 = trip1.getDepartureTime();
                    LocalDateTime end1 = start1.plusMinutes(trip1.getDuration());
                    LocalDateTime start2 = trip2.getDepartureTime();
                    LocalDateTime end2 = start2.plusMinutes(trip2.getDuration());

                    // Check if trips overlap
                    if (start1.isBefore(end2) && start2.isBefore(end1)) {
                        String description = "Time conflict: " + trip1.getTripName() +
                                " and " + trip2.getTripName() +
                                " overlap on " + date;
                        List<Integer> tripIds = List.of(trip1.getTripId(), trip2.getTripId());
                        conflicts.add(new ConflictInfo(description, date, tripIds));
                    }
                }
            }
        }

        return conflicts;
    }

    private int calculateUpcomingTrips(List<Trip> trips) {
        LocalDateTime now = LocalDateTime.now();
        return (int) trips.stream()
                .filter(trip -> trip.getDepartureTime().isAfter(now))
                .count();
    }

    private int calculateScheduledStaff(List<Trip> trips) {
        // This is a simplified calculation  adjust based on your actual staff assignment logic
        return trips.stream()
                .mapToInt(trip -> 2) // Assuming 2 staff per trip (driver + guide)
                .sum();
    }

    private String calculateOccupancyRate(List<Trip> trips) {
        if (trips.isEmpty()) return "0%";

        // Simplified calculation - adjust based on your actual occupancy logic
        double totalCapacity = trips.stream().mapToInt(trip -> 20).sum(); // Assuming 20 capacity per trip
        double totalBooked = trips.stream().mapToInt(trip -> 20 - trip.getAvailability()).sum();

        int occupancyRate = (int) ((totalBooked / totalCapacity) * 100);
        return occupancyRate + "%";
    }

    private List<List<CalendarDay>> buildCalendar(YearMonth yearMonth, List<Trip> trips) {
        List<List<CalendarDay>> weeks = new ArrayList<>();
        List<CalendarDay> currentWeek = new ArrayList<>();

        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();
        LocalDate today = LocalDate.now();

        // Add days from previous month to fill the first week
        DayOfWeek firstDayOfWeek = firstDay.getDayOfWeek();
        int daysFromPreviousMonth = firstDayOfWeek.getValue() % 7; // Sunday = 0, Monday = 1, etc.

        if (daysFromPreviousMonth > 0) {
            YearMonth previousYearMonth = yearMonth.minusMonths(1);
            LocalDate previousMonthLastDay = previousYearMonth.atEndOfMonth();

            for (int i = daysFromPreviousMonth - 1; i >= 0; i--) {
                LocalDate date = previousMonthLastDay.minusDays(i);
                List<Trip> dayTrips = filterTripsByDate(trips, date);
                currentWeek.add(new CalendarDay(date.getDayOfMonth(), true, false, dayTrips));
            }
        }

        // Add current month days
        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            List<Trip> dayTrips = filterTripsByDate(trips, date);
            boolean isToday = date.equals(today);
            currentWeek.add(new CalendarDay(date.getDayOfMonth(), false, isToday, dayTrips));

            // Start new week when we reach Sunday
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                weeks.add(currentWeek);
                currentWeek = new ArrayList<>();
            }
        }

        // Add days from next month to complete the last week
        if (!currentWeek.isEmpty()) {
            YearMonth nextYearMonth = yearMonth.plusMonths(1);
            LocalDate nextMonthFirstDay = nextYearMonth.atDay(1);

            while (currentWeek.size() < 7) {
                List<Trip> dayTrips = filterTripsByDate(trips, nextMonthFirstDay);
                currentWeek.add(new CalendarDay(nextMonthFirstDay.getDayOfMonth(), true, false, dayTrips));
                nextMonthFirstDay = nextMonthFirstDay.plusDays(1);
            }
            weeks.add(currentWeek);
        }

        return weeks;
    }

    private List<Trip> filterTripsByDate(List<Trip> trips, LocalDate date) {
        return trips.stream()
                .filter(trip -> trip.getDepartureTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("trip", new Trip());
        model.addAttribute("availableBoats", tripService.getAllBoats());
        model.addAttribute("availableStaff", tripService.getStaffByRole("BoatDriver"));
        return "create-trip";
    }

    @PostMapping("/create")
    public String createTrip(@ModelAttribute Trip trip, RedirectAttributes redirectAttributes) {
        try {
            tripService.createTrip(trip);
            redirectAttributes.addFlashAttribute("successMessage", "Trip created successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/trip-schedule/create";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating trip: " + e.getMessage());
            return "redirect:/trip-schedule/create";
        }
        return "redirect:/trip-schedule";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Trip trip = tripService.getTripById(id);
        if (trip == null) {
            return "redirect:/trip-schedule";
        }

        model.addAttribute("trip", trip);
        model.addAttribute("availableBoats", tripService.getAllBoats());
        model.addAttribute("availableStaff", tripService.getStaffByRole("BoatDriver"));
        return "edit-trip";
    }

    @PostMapping("/edit/{id}")
    public String updateTrip(@PathVariable Integer id, @ModelAttribute Trip trip,
                             RedirectAttributes redirectAttributes) {
        try {
            trip.setTripId(id);
            tripService.updateTrip(trip);
            redirectAttributes.addFlashAttribute("successMessage", "Trip updated successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/trip-schedule/edit/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating trip: " + e.getMessage());
            return "redirect:/trip-schedule/edit/" + id;
        }
        return "redirect:/trip-schedule";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrip(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = tripService.deleteTrip(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Trip deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Trip not found or could not be deleted.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting trip: " + e.getMessage());
        }
        return "redirect:/trip-schedule";
    }

    @GetMapping("/check-availability")
    @ResponseBody
    public String checkBoatAvailability(@RequestParam Integer boatId,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime,
                                        @RequestParam Integer duration) {
        boolean isAvailable = tripService.isBoatAvailable(boatId, departureTime, duration);
        return isAvailable ? "Available" : "Not Available - Under Maintenance";
    }
}