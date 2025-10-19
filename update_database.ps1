# PowerShell script to update database with salary data
# This script will connect to your SQL Server and add salary data

Write-Host "Adding salary data to Payment database..." -ForegroundColor Green

# SQL commands to execute
$sqlCommands = @"
USE Payment;

-- Add salary data to existing staff
UPDATE Staff SET Salary = 75000.00 WHERE StaffId = 1;  -- Captain/Manager
UPDATE Staff SET Salary = 65000.00 WHERE StaffId = 2;  -- Senior Staff  
UPDATE Staff SET Salary = 55000.00 WHERE StaffId = 3;  -- Regular Staff

-- Verify the data
SELECT StaffId, LaneNumber, City, Availability, Salary FROM Staff;
"@

Write-Host "SQL Commands to execute:" -ForegroundColor Yellow
Write-Host $sqlCommands -ForegroundColor Cyan

Write-Host "`nPlease run these commands in SQL Server Management Studio:" -ForegroundColor Red
Write-Host "1. Open SQL Server Management Studio" -ForegroundColor White
Write-Host "2. Connect to localhost with sa/123" -ForegroundColor White
Write-Host "3. Copy and paste the SQL commands above" -ForegroundColor White
Write-Host "4. Execute the commands" -ForegroundColor White

Write-Host "`nAfter adding the salary data, test your system with:" -ForegroundColor Green
Write-Host "http://localhost:8080/api/salaries/preview/1?adminId=1&periodMonth=10&periodYear=2025" -ForegroundColor Cyan
Write-Host "http://localhost:8080/api/salaries/preview/2?adminId=1&periodMonth=10&periodYear=2025" -ForegroundColor Cyan
Write-Host "http://localhost:8080/api/salaries/preview/3?adminId=1&periodMonth=10&periodYear=2025" -ForegroundColor Cyan
