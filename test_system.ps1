# Test script for Boat Safari Salary System
Write-Host "=== BOAT SAFARI SALARY SYSTEM TEST ===" -ForegroundColor Green

# Test database connection
Write-Host "`n1. Testing Database Connection:" -ForegroundColor Yellow
try {
    $dbTest = Invoke-WebRequest "http://localhost:8080/api/test-db" | ConvertFrom-Json
    Write-Host "Status: $($dbTest.status)" -ForegroundColor Green
    Write-Host "Staff Count: $($dbTest.staffCount)" -ForegroundColor Green
} catch {
    Write-Host "Database connection failed!" -ForegroundColor Red
    exit 1
}

# Test salary previews for different staff
Write-Host "`n2. Testing Salary Previews:" -ForegroundColor Yellow

$testCases = @(
    @{StaffId=1; Role="Captain/Manager"; ExpectedBase=75000},
    @{StaffId=2; Role="Senior Staff"; ExpectedBase=65000},
    @{StaffId=3; Role="Regular Staff"; ExpectedBase=55000}
)

foreach ($test in $testCases) {
    Write-Host "`nTesting Staff ID $($test.StaffId) ($($test.Role)):" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/preview/$($test.StaffId)?adminId=1&periodMonth=10&periodYear=2025"
        $response = Invoke-WebRequest $url | ConvertFrom-Json
        
        Write-Host "  Base Salary: `$$($response.baseSalary)" -ForegroundColor White
        Write-Host "  Trip Incentives: `$$($response.tripIncentives)" -ForegroundColor White
        Write-Host "  Total Amount: `$$($response.totalAmount)" -ForegroundColor White
        
        # Check if values are different from default
        if ($response.baseSalary -ne 50000) {
            Write-Host "  ✅ Using database/role-based calculation!" -ForegroundColor Green
        } else {
            Write-Host "  ⚠️  Still using default values" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "  ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host "`n3. Test URLs for Manual Testing:" -ForegroundColor Yellow
Write-Host "Database Status: http://localhost:8080/api/test-db" -ForegroundColor Cyan
Write-Host "HTML Frontend: http://localhost:8080/static/index.html" -ForegroundColor Cyan

Write-Host "`n=== TEST COMPLETE ===" -ForegroundColor Green
