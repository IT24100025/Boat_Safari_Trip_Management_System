-- Comprehensive Test Data for Boat Safari Salary Calculator
USE Payment;

-- 1. Add Name and Role columns to Staff table if they don't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Staff' AND COLUMN_NAME = 'Name')
BEGIN
    ALTER TABLE Staff ADD Name NVARCHAR(100);
END

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'Staff' AND COLUMN_NAME = 'Role')
BEGIN
    ALTER TABLE Staff ADD Role NVARCHAR(50);
END

-- 2. Add more Staff members with different roles and salaries
IF NOT EXISTS (SELECT 1 FROM Staff WHERE StaffId = 4)
BEGIN
    INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary, Name, Role) VALUES (4, '404 Marina Blvd', 'Trincomalee', 1, 80000.00, 'Manager One', 'Manager');
END

IF NOT EXISTS (SELECT 1 FROM Staff WHERE StaffId = 5)
BEGIN
    INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary, Name, Role) VALUES (5, '505 Port Rd', 'Kalpitiya', 1, 70000.00, 'Senior Staff Two', 'Senior Staff');
END

IF NOT EXISTS (SELECT 1 FROM Staff WHERE StaffId = 6)
BEGIN
    INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary, Name, Role) VALUES (6, '606 Harbor View', 'Batticaloa', 0, 60000.00, 'Regular Staff Three', 'Regular Staff');
END

IF NOT EXISTS (SELECT 1 FROM Staff WHERE StaffId = 7)
BEGIN
    INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary, Name, Role) VALUES (7, '707 Coastal Dr', 'Jaffna', 1, 85000.00, 'Senior Manager One', 'Senior Manager');
END

IF NOT EXISTS (SELECT 1 FROM Staff WHERE StaffId = 8)
BEGIN
    INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary, Name, Role) VALUES (8, '808 Bay St', 'Hambantota', 1, 72000.00, 'Coordinator One', 'Coordinator');
END

-- Update existing staff with names and roles
UPDATE Staff SET Name = 'Captain One', Role = 'Captain' WHERE StaffId = 1;
UPDATE Staff SET Name = 'Senior Staff One', Role = 'Senior Staff' WHERE StaffId = 2;
UPDATE Staff SET Name = 'Regular Staff One', Role = 'Regular Staff' WHERE StaffId = 3;

-- 2. Add more Admin users
IF NOT EXISTS (SELECT 1 FROM Admin WHERE AdminID = 3)
BEGIN
    INSERT INTO Admin (AdminID, Name, Email, Role) VALUES (3, 'Admin Three', 'admin3@example.com', 'Senior Admin');
END

IF NOT EXISTS (SELECT 1 FROM Admin WHERE AdminID = 4)
BEGIN
    INSERT INTO Admin (AdminID, Name, Email, Role) VALUES (4, 'Admin Four', 'admin4@example.com', 'HR Manager');
END

-- 3. Add sample salary payments for different periods
-- Clear existing salary payments for clean test
DELETE FROM SalaryPayment;

-- Add salary payments for different staff and periods
INSERT INTO SalaryPayment (StaffId, periodMonth, periodYear, baseSalary, tripIncentives, weatherAllowances, totalAmount, paymentDate, status, authorized_by) VALUES
(1, 9, 2025, 75000.00, 7500.00, 2000.00, 84500.00, '2025-09-30 10:00:00', 'Paid', 1),
(2, 9, 2025, 65000.00, 6500.00, 1500.00, 73000.00, '2025-09-30 10:15:00', 'Paid', 1),
(3, 9, 2025, 55000.00, 5500.00, 1000.00, 61500.00, '2025-09-30 10:30:00', 'Paid', 2),
(1, 10, 2025, 75000.00, 8000.00, 2500.00, 85500.00, '2025-10-31 10:00:00', 'Pending', 1),
(2, 10, 2025, 65000.00, 7000.00, 2000.00, 74000.00, '2025-10-31 10:15:00', 'Pending', 1),
(4, 10, 2025, 80000.00, 8500.00, 3000.00, 91500.00, '2025-10-31 10:30:00', 'Pending', 2),
(5, 10, 2025, 70000.00, 7500.00, 2200.00, 79700.00, '2025-10-31 10:45:00', 'Pending', 2),
(1, 11, 2025, 75000.00, 9000.00, 3000.00, 87000.00, '2025-11-30 10:00:00', 'Calculated', 3),
(2, 11, 2025, 65000.00, 8000.00, 2500.00, 75500.00, '2025-11-30 10:15:00', 'Calculated', 3),
(6, 11, 2025, 60000.00, 6000.00, 1500.00, 67500.00, '2025-11-30 10:30:00', 'Calculated', 4),
(7, 11, 2025, 85000.00, 9500.00, 3500.00, 98000.00, '2025-11-30 10:45:00', 'Calculated', 4),
(8, 11, 2025, 72000.00, 8000.00, 2800.00, 82800.00, '2025-11-30 11:00:00', 'Calculated', 3);

-- 4. Add sample audit logs
-- Clear existing audit logs for clean test
DELETE FROM SalaryAudit;

-- Add audit logs for the salary payments
INSERT INTO SalaryAudit (SalaryID, ActionType, ActionDate, PerformedBy, Changes) VALUES
(1, 'SALARY_CALCULATED', '2025-09-30 10:00:00', 1, 'Salary calculated for Staff ID: 1, Period: 9/2025, Amount: $84500.00'),
(2, 'SALARY_CALCULATED', '2025-09-30 10:15:00', 1, 'Salary calculated for Staff ID: 2, Period: 9/2025, Amount: $73000.00'),
(3, 'SALARY_CALCULATED', '2025-09-30 10:30:00', 2, 'Salary calculated for Staff ID: 3, Period: 9/2025, Amount: $61500.00'),
(4, 'SALARY_CALCULATED', '2025-10-31 10:00:00', 1, 'Salary calculated for Staff ID: 1, Period: 10/2025, Amount: $85500.00'),
(5, 'SALARY_CALCULATED', '2025-10-31 10:15:00', 1, 'Salary calculated for Staff ID: 2, Period: 10/2025, Amount: $74000.00'),
(6, 'SALARY_CALCULATED', '2025-10-31 10:30:00', 2, 'Salary calculated for Staff ID: 4, Period: 10/2025, Amount: $91500.00'),
(7, 'SALARY_CALCULATED', '2025-10-31 10:45:00', 2, 'Salary calculated for Staff ID: 5, Period: 10/2025, Amount: $79700.00'),
(8, 'SALARY_CALCULATED', '2025-11-30 10:00:00', 3, 'Salary calculated for Staff ID: 1, Period: 11/2025, Amount: $87000.00'),
(9, 'SALARY_CALCULATED', '2025-11-30 10:15:00', 3, 'Salary calculated for Staff ID: 2, Period: 11/2025, Amount: $75500.00'),
(10, 'SALARY_CALCULATED', '2025-11-30 10:30:00', 4, 'Salary calculated for Staff ID: 6, Period: 11/2025, Amount: $67500.00'),
(11, 'SALARY_CALCULATED', '2025-11-30 10:45:00', 4, 'Salary calculated for Staff ID: 7, Period: 11/2025, Amount: $98000.00'),
(12, 'SALARY_CALCULATED', '2025-11-30 11:00:00', 3, 'Salary calculated for Staff ID: 8, Period: 11/2025, Amount: $82800.00');

-- 5. Add some additional audit actions
INSERT INTO SalaryAudit (SalaryID, ActionType, ActionDate, PerformedBy, Changes) VALUES
(1, 'SALARY_APPROVED', '2025-09-30 11:00:00', 1, 'Salary approved for Staff ID: 1, Amount: $84500.00'),
(2, 'SALARY_APPROVED', '2025-09-30 11:15:00', 1, 'Salary approved for Staff ID: 2, Amount: $73000.00'),
(4, 'SALARY_REVIEWED', '2025-10-31 11:00:00', 2, 'Salary reviewed for Staff ID: 1, Amount: $85500.00'),
(5, 'SALARY_REVIEWED', '2025-10-31 11:15:00', 2, 'Salary reviewed for Staff ID: 2, Amount: $74000.00'),
(8, 'SALARY_PENDING_APPROVAL', '2025-11-30 11:00:00', 3, 'Salary pending approval for Staff ID: 1, Amount: $87000.00'),
(9, 'SALARY_PENDING_APPROVAL', '2025-11-30 11:15:00', 3, 'Salary pending approval for Staff ID: 2, Amount: $75500.00');

-- 6. Verify the data
SELECT '=== VERIFICATION ===' as Status;

SELECT 'Staff Table:' as TableName;
SELECT StaffId, LaneNumber, City, Availability, Salary FROM Staff ORDER BY StaffId;

SELECT 'Admin Table:' as TableName;
SELECT * FROM Admin ORDER BY AdminID;

SELECT 'SalaryPayment Table:' as TableName;
SELECT SalaryID, StaffId, periodMonth, periodYear, baseSalary, tripIncentives, weatherAllowances, totalAmount, status, authorized_by FROM SalaryPayment ORDER BY SalaryID;

SELECT 'SalaryAudit Table:' as TableName;
SELECT AuditID, SalaryID, ActionType, ActionDate, PerformedBy, Changes FROM SalaryAudit ORDER BY AuditID;

PRINT '=== COMPREHENSIVE TEST DATA ADDED ===';
PRINT 'Now you have:';
PRINT '- 8 Staff members with different salaries';
PRINT '- 4 Admin users';
PRINT '- 12 Salary payments across different periods';
PRINT '- 18 Audit log entries';
PRINT 'Test your system with this rich data!';
