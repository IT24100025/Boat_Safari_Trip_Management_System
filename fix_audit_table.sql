-- Fix SalaryAudit Table - Run this in SQL Server Management Studio
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
        Changes NVARCHAR(MAX)
    );
    PRINT '✅ SalaryAudit table created successfully';
END
ELSE
BEGIN
    PRINT 'ℹ️ SalaryAudit table already exists';
END

-- 2. Add Admin data if not exists
IF NOT EXISTS (SELECT 1 FROM Admin WHERE AdminID = 1)
BEGIN
    INSERT INTO Admin (AdminID, Name, Email, Role) VALUES (1, 'Admin One', 'admin1@example.com', 'Admin');
    PRINT '✅ Admin One created';
END

IF NOT EXISTS (SELECT 1 FROM Admin WHERE AdminID = 2)
BEGIN
    INSERT INTO Admin (AdminID, Name, Email, Role) VALUES (2, 'Admin Two', 'admin2@example.com', 'Admin');
    PRINT '✅ Admin Two created';
END

-- 3. Verify the setup
SELECT '=== VERIFICATION ===' as Status;
SELECT 'Admin Table:' as TableName;
SELECT * FROM Admin;

SELECT 'SalaryAudit Table:' as TableName;
SELECT * FROM SalaryAudit;

PRINT '=== SETUP COMPLETE ===';
PRINT 'Now test your audit logs!';
