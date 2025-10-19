-- Simple script to add salary data to existing Staff table
-- This script will work with whatever columns exist in your Staff table

USE Payment;

-- First, let's see what the Staff table actually looks like
PRINT 'Staff table structure:';
SELECT 
    COLUMN_NAME,
    DATA_TYPE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Staff' 
AND TABLE_SCHEMA = 'dbo'
ORDER BY ORDINAL_POSITION;

-- Let's see what data is currently in the Staff table
PRINT 'Current Staff data:';
SELECT * FROM Staff;

-- If your Staff table has a different structure, we need to work with what exists
-- Let's try to add salary data to existing staff records

-- Option 1: If there's a Salary column, update it
-- UPDATE Staff SET Salary = 75000.00 WHERE StaffId = 1;
-- UPDATE Staff SET Salary = 65000.00 WHERE StaffId = 2;  
-- UPDATE Staff SET Salary = 55000.00 WHERE StaffId = 3;

-- Option 2: If there's no Salary column, we might need to add it first
-- ALTER TABLE Staff ADD Salary DECIMAL(10,2);

-- Option 3: If the table structure is completely different, we'll work with what exists
-- and modify the Java code to use the actual database structure

PRINT 'Please run this script first to see your actual table structure, then we can create the appropriate update script.';
