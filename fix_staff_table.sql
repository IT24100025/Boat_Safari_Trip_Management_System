-- Fix Staff table structure and add salary data
USE Payment;

-- First, let's check what columns exist in the Staff table
PRINT 'Current Staff table structure:';
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Staff' 
AND TABLE_SCHEMA = 'dbo'
ORDER BY ORDINAL_POSITION;

-- Let's see what data is currently in the Staff table
PRINT 'Current Staff data:';
SELECT TOP 10 * FROM Staff;

-- If the Staff table doesn't have the expected columns, we need to add them
-- Let's add the missing columns if they don't exist

-- Add Salary column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'Staff' AND COLUMN_NAME = 'Salary')
BEGIN
    ALTER TABLE Staff ADD Salary DECIMAL(10,2);
    PRINT 'Added Salary column to Staff table';
END

-- Add LaneNumber column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'Staff' AND COLUMN_NAME = 'LaneNumber')
BEGIN
    ALTER TABLE Staff ADD LaneNumber VARCHAR(100);
    PRINT 'Added LaneNumber column to Staff table';
END

-- Add City column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'Staff' AND COLUMN_NAME = 'City')
BEGIN
    ALTER TABLE Staff ADD City VARCHAR(50);
    PRINT 'Added City column to Staff table';
END

-- Add Availability column if it doesn't exist
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS 
               WHERE TABLE_NAME = 'Staff' AND COLUMN_NAME = 'Availability')
BEGIN
    ALTER TABLE Staff ADD Availability BIT;
    PRINT 'Added Availability column to Staff table';
END

-- Now let's add some sample data to the Staff table
-- First, let's see what StaffId values exist
PRINT 'Existing Staff IDs:';
SELECT StaffId FROM Staff ORDER BY StaffId;

-- Add salary data to existing staff (only if StaffId exists)
UPDATE Staff SET Salary = 75000.00 WHERE StaffId = 1;
UPDATE Staff SET Salary = 65000.00 WHERE StaffId = 2;  
UPDATE Staff SET Salary = 55000.00 WHERE StaffId = 3;

-- Add other data if columns exist
UPDATE Staff SET LaneNumber = '101 Ocean Drive' WHERE StaffId = 1;
UPDATE Staff SET LaneNumber = '202 Beach Road' WHERE StaffId = 2;
UPDATE Staff SET LaneNumber = '303 Harbor Street' WHERE StaffId = 3;

UPDATE Staff SET City = 'Colombo' WHERE StaffId = 1;
UPDATE Staff SET City = 'Galle' WHERE StaffId = 2;
UPDATE Staff SET City = 'Negombo' WHERE StaffId = 3;

UPDATE Staff SET Availability = 1 WHERE StaffId = 1;
UPDATE Staff SET Availability = 1 WHERE StaffId = 2;
UPDATE Staff SET Availability = 0 WHERE StaffId = 3;

-- Verify the updated data
PRINT 'Updated Staff data:';
SELECT StaffId, LaneNumber, City, Availability, Salary FROM Staff ORDER BY StaffId;
