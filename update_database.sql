-- Update Payment Database with Salary Data
-- Run this in SQL Server Management Studio

USE Payment;

-- First, let's check what's currently in the Staff table
SELECT 'Current Staff Data:' as Info;
SELECT StaffId, LaneNumber, City, Availability, Salary FROM Staff;

-- Update existing staff with salary data
UPDATE Staff SET Salary = 75000.00 WHERE StaffId = 1;
UPDATE Staff SET Salary = 65000.00 WHERE StaffId = 2;  
UPDATE Staff SET Salary = 55000.00 WHERE StaffId = 3;

-- Add more staff if needed (optional)
INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary) 
VALUES 
    (4, '456 Ocean Drive', 'Galle', 1, 80000.00),
    (5, '789 Harbor Street', 'Negombo', 1, 60000.00)
ON DUPLICATE KEY UPDATE Salary = VALUES(Salary);

-- Verify the updated data
SELECT 'Updated Staff Data:' as Info;
SELECT StaffId, LaneNumber, City, Availability, Salary FROM Staff ORDER BY StaffId;

-- Show salary summary
SELECT 'Salary Summary:' as Info;
SELECT 
    COUNT(*) as TotalStaff,
    AVG(Salary) as AverageSalary,
    MIN(Salary) as MinSalary,
    MAX(Salary) as MaxSalary
FROM Staff 
WHERE Salary IS NOT NULL;
