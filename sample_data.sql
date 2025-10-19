-- Sample data for Boat Safari Salary System
-- Run this in your SQL Server Management Studio

USE Payment;

-- Update Staff table with sample salary data
UPDATE Staff SET Salary = 75000.00 WHERE StaffId = 1;
UPDATE Staff SET Salary = 65000.00 WHERE StaffId = 2;
UPDATE Staff SET Salary = 55000.00 WHERE StaffId = 3;

-- Add some sample data if needed
INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary) 
VALUES (4, '123 Main St', 'Colombo', 1, 80000.00)
ON DUPLICATE KEY UPDATE Salary = 80000.00;

-- Verify the data
SELECT StaffId, LaneNumber, City, Availability, Salary FROM Staff;
