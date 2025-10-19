-- Add Salary Data to Payment Database
-- This script will add salary information to your existing staff

USE Payment;

-- First, let's see what's currently in the Staff table
PRINT 'Current Staff Data:';
SELECT StaffId, LaneNumber, City, Availability, Salary FROM Staff;

-- Add salary data to existing staff
UPDATE Staff SET Salary = 75000.00 WHERE StaffId = 1;  -- Captain/Manager
UPDATE Staff SET Salary = 65000.00 WHERE StaffId = 2;  -- Senior Staff  
UPDATE Staff SET Salary = 55000.00 WHERE StaffId = 3;  -- Regular Staff

-- Add more staff with salary data if needed
INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary) 
VALUES 
    (4, '456 Ocean Drive', 'Galle', 1, 80000.00),
    (5, '789 Harbor Street', 'Negombo', 1, 60000.00)
ON DUPLICATE KEY UPDATE Salary = VALUES(Salary);

-- Verify the updated data
PRINT 'Updated Staff Data:';
SELECT StaffId, LaneNumber, City, Availability, Salary FROM Staff ORDER BY StaffId;

-- Show salary summary
PRINT 'Salary Summary:';
SELECT 
    COUNT(*) as TotalStaff,
    AVG(Salary) as AverageSalary,
    MIN(Salary) as MinSalary,
    MAX(Salary) as MaxSalary
FROM Staff 
WHERE Salary IS NOT NULL;
