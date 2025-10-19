-- Test Data for Salary Audit Log System
USE Payment;

-- 1. Create SalaryAudit table if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SalaryAudit')
BEGIN
    CREATE TABLE SalaryAudit (
        AuditID INT IDENTITY(1,1) PRIMARY KEY,
        SalaryID INT NOT NULL,
        ActionType NVARCHAR(50) NOT NULL,
        ActionDate DATETIME2 NOT NULL,
        PerformedBy INT,
        Changes NVARCHAR(MAX),
        FOREIGN KEY (SalaryID) REFERENCES SalaryPayment(SalaryID)
    );
    PRINT 'SalaryAudit table created successfully';
END
ELSE
BEGIN
    PRINT 'SalaryAudit table already exists';
END

-- 2. Insert sample Admin data if not exists
IF NOT EXISTS (SELECT 1 FROM Admin WHERE AdminID = 1)
BEGIN
    INSERT INTO Admin (AdminID, Name, Email, Role) VALUES (1, 'Admin One', 'admin1@example.com', 'Admin');
    PRINT 'Admin One created';
END

IF NOT EXISTS (SELECT 1 FROM Admin WHERE AdminID = 2)
BEGIN
    INSERT INTO Admin (AdminID, Name, Email, Role) VALUES (2, 'Admin Two', 'admin2@example.com', 'Admin');
    PRINT 'Admin Two created';
END

-- 3. Insert sample Staff data if not exists
IF NOT EXISTS (SELECT 1 FROM Staff WHERE StaffId = 1)
BEGIN
    INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary) VALUES (1, '101 Ocean Dr', 'Colombo', 1, 75000.00);
    PRINT 'Staff 1 created';
END

IF NOT EXISTS (SELECT 1 FROM Staff WHERE StaffId = 2)
BEGIN
    INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary) VALUES (2, '202 Beach Rd', 'Galle', 1, 65000.00);
    PRINT 'Staff 2 created';
END

IF NOT EXISTS (SELECT 1 FROM Staff WHERE StaffId = 3)
BEGIN
    INSERT INTO Staff (StaffId, LaneNumber, City, Availability, Salary) VALUES (3, '303 Harbor St', 'Negombo', 0, 55000.00);
    PRINT 'Staff 3 created';
END

-- 4. Clear existing SalaryPayment and SalaryAudit data for clean test
DELETE FROM SalaryAudit;
DELETE FROM SalaryPayment;
PRINT 'Cleared existing salary and audit data';

-- 5. Verify the setup
SELECT '=== VERIFICATION ===' as Status;
SELECT 'Admin Table:' as TableName;
SELECT * FROM Admin;

SELECT 'Staff Table:' as TableName;
SELECT * FROM Staff;

SELECT 'SalaryAudit Table (should be empty):' as TableName;
SELECT * FROM SalaryAudit;

SELECT 'SalaryPayment Table (should be empty):' as TableName;
SELECT * FROM SalaryPayment;

PRINT '=== SETUP COMPLETE ===';
PRINT 'Now test the system using the URLs provided in the test guide.';
