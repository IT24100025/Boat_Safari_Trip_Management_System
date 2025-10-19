-- Check the actual structure of the Staff table
USE Payment;

-- First, let's see what columns actually exist in the Staff table
SELECT 
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'Staff' 
AND TABLE_SCHEMA = 'dbo'
ORDER BY ORDINAL_POSITION;

-- Let's also see what data is currently in the Staff table
SELECT TOP 10 * FROM Staff;
