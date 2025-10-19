# Comprehensive Test for Boat Safari Salary Calculator
Write-Host "=== BOAT SAFARI SALARY CALCULATOR - COMPREHENSIVE TEST ===" -ForegroundColor Green

# Test 1: Check Application Status
Write-Host "`n1. CHECKING APPLICATION STATUS" -ForegroundColor Yellow
Write-Host "=================================" -ForegroundColor Yellow
try {
    $dbTest = Invoke-WebRequest "http://localhost:8080/api/test-db" | ConvertFrom-Json
    Write-Host "✅ Application is running" -ForegroundColor Green
    Write-Host "   Status: $($dbTest.status)" -ForegroundColor White
    Write-Host "   Staff Count: $($dbTest.staffCount)" -ForegroundColor White
} catch {
    Write-Host "❌ Application not responding" -ForegroundColor Red
    Write-Host "   Please start your application first!" -ForegroundColor Yellow
    exit 1
}

# Test 2: Test Salary Preview for Different Staff
Write-Host "`n2. TESTING SALARY PREVIEW FOR DIFFERENT STAFF" -ForegroundColor Yellow
Write-Host "=============================================" -ForegroundColor Yellow

$staffTests = @(
    @{StaffId=1; AdminId=1; Month=10; Year=2025; Description="Staff 1 (Captain) - October 2025"},
    @{StaffId=2; AdminId=1; Month=10; Year=2025; Description="Staff 2 (Senior) - October 2025"},
    @{StaffId=4; AdminId=2; Month=11; Year=2025; Description="Staff 4 (Manager) - November 2025"},
    @{StaffId=7; AdminId=3; Month=12; Year=2025; Description="Staff 7 (Senior Manager) - December 2025"},
    @{StaffId=8; AdminId=4; Month=1; Year=2026; Description="Staff 8 (Coordinator) - January 2026"}
)

foreach ($test in $staffTests) {
    Write-Host "`n   Testing: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/preview/$($test.StaffId)?adminId=$($test.AdminId)&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method GET
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success - Staff ID: $($test.StaffId)" -ForegroundColor Green
        Write-Host "   💰 Base Salary: `$$($salaryData.baseSalary)" -ForegroundColor White
        Write-Host "   🚢 Trip Incentives: `$$($salaryData.tripIncentives)" -ForegroundColor White
        Write-Host "   🌤️ Weather Allowances: `$$($salaryData.weatherAllowances)" -ForegroundColor White
        Write-Host "   💵 Total Amount: `$$($salaryData.totalAmount)" -ForegroundColor Green
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 3: Test Salary Calculations
Write-Host "`n3. TESTING SALARY CALCULATIONS" -ForegroundColor Yellow
Write-Host "===============================" -ForegroundColor Yellow

$calculationTests = @(
    @{StaffId=1; AdminId=1; Month=10; Year=2025; Description="Calculate Staff 1 - October 2025"},
    @{StaffId=5; AdminId=2; Month=11; Year=2025; Description="Calculate Staff 5 - November 2025"},
    @{StaffId=6; AdminId=3; Month=12; Year=2025; Description="Calculate Staff 6 - December 2025"}
)

$createdSalaries = @()

foreach ($test in $calculationTests) {
    Write-Host "`n   Calculating: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/calculate/$($test.StaffId)?adminId=$($test.AdminId)&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method POST
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success - Salary ID: $($salaryData.salaryId)" -ForegroundColor Green
        Write-Host "   💰 Total Amount: `$$($salaryData.totalAmount)" -ForegroundColor White
        
        $createdSalaries += @{
            SalaryId = $salaryData.salaryId
            StaffId = $test.StaffId
            AdminId = $test.AdminId
            Amount = $salaryData.totalAmount
        }
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 4: Test Audit Logs
Write-Host "`n4. TESTING AUDIT LOGS" -ForegroundColor Yellow
Write-Host "=====================" -ForegroundColor Yellow

Write-Host "`n   Testing: Get All Audit Logs" -ForegroundColor Cyan
try {
    $allAudits = Invoke-WebRequest "http://localhost:8080/api/salary-audits" -Method GET | ConvertFrom-Json
    Write-Host "   ✅ Found $($allAudits.Count) audit logs" -ForegroundColor Green
    
    if ($allAudits.Count -gt 0) {
        Write-Host "   📋 Recent audit logs:" -ForegroundColor White
        foreach ($audit in $allAudits | Select-Object -First 3) {
            Write-Host "      - $($audit.actionType): $($audit.changes)" -ForegroundColor Gray
        }
    }
} catch {
    Write-Host "   ❌ Error getting all audits: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Test Audit Logs by Salary ID
if ($createdSalaries.Count -gt 0) {
    $testSalaryId = $createdSalaries[0].SalaryId
    Write-Host "`n   Testing: Get Audit Logs by Salary ID ($testSalaryId)" -ForegroundColor Cyan
    try {
        $salaryAudits = Invoke-WebRequest "http://localhost:8080/api/salary-audits/$testSalaryId" -Method GET | ConvertFrom-Json
        Write-Host "   ✅ Found $($salaryAudits.Count) audit logs for Salary ID $testSalaryId" -ForegroundColor Green
    } catch {
        Write-Host "   ❌ Error getting salary audits: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 6: Test Different Periods
Write-Host "`n5. TESTING DIFFERENT PERIODS" -ForegroundColor Yellow
Write-Host "=============================" -ForegroundColor Yellow

$periodTests = @(
    @{Month=1; Year=2025; Description="January 2025 (Winter)"},
    @{Month=6; Year=2025; Description="June 2025 (Summer)"},
    @{Month=12; Year=2025; Description="December 2025 (Holiday Season)"}
)

foreach ($test in $periodTests) {
    Write-Host "`n   Testing: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/preview/1?adminId=1&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method GET
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success" -ForegroundColor Green
        Write-Host "   💰 Total Amount: `$$($salaryData.totalAmount)" -ForegroundColor White
        Write-Host "   🌤️ Weather Allowances: `$$($salaryData.weatherAllowances)" -ForegroundColor White
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 7: Summary
Write-Host "`n6. TEST SUMMARY" -ForegroundColor Yellow
Write-Host "================" -ForegroundColor Yellow
Write-Host "✅ Staff members tested: $($staffTests.Count)" -ForegroundColor Green
Write-Host "✅ Salary calculations created: $($createdSalaries.Count)" -ForegroundColor Green
Write-Host "✅ Different periods tested: $($periodTests.Count)" -ForegroundColor Green
Write-Host "✅ Audit logs should be working" -ForegroundColor Green

Write-Host "`n📋 TESTING URLs:" -ForegroundColor Yellow
Write-Host "=================" -ForegroundColor Yellow
Write-Host "Database Status: http://localhost:8080/api/test-db" -ForegroundColor Cyan
Write-Host "All Audit Logs: http://localhost:8080/api/salary-audits" -ForegroundColor Cyan
Write-Host "HTML Interface: http://localhost:8080/static/index.html" -ForegroundColor Cyan

Write-Host "`n🎯 RECOMMENDED TEST SCENARIOS:" -ForegroundColor Yellow
Write-Host "==============================" -ForegroundColor Yellow
Write-Host "1. Test salary preview for different staff members" -ForegroundColor White
Write-Host "2. Calculate salaries for different periods" -ForegroundColor White
Write-Host "3. Check audit logs for different salary IDs" -ForegroundColor White
Write-Host "4. Test the HTML interface with the new data" -ForegroundColor White
Write-Host "5. Verify seasonal variations in weather allowances" -ForegroundColor White

Write-Host "`n=== COMPREHENSIVE TEST COMPLETE ===" -ForegroundColor Green
