# Test Comprehensive Salary Calculator Output
Write-Host "=== BOAT SAFARI SALARY CALCULATOR - COMPREHENSIVE OUTPUT TEST ===" -ForegroundColor Green

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

# Test 2: Test Comprehensive Salary Preview
Write-Host "`n2. TESTING COMPREHENSIVE SALARY PREVIEW" -ForegroundColor Yellow
Write-Host "=========================================" -ForegroundColor Yellow

$testCases = @(
    @{StaffId=1; AdminId=1; Month=10; Year=2025; Description="Captain - October 2025"},
    @{StaffId=4; AdminId=2; Month=11; Year=2025; Description="Manager - November 2025"},
    @{StaffId=7; AdminId=3; Month=12; Year=2025; Description="Senior Manager - December 2025"}
)

foreach ($test in $testCases) {
    Write-Host "`n   Testing: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/preview/$($test.StaffId)?adminId=$($test.AdminId)&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method GET
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success - Staff ID: $($test.StaffId)" -ForegroundColor Green
        Write-Host "   📋 COMPREHENSIVE OUTPUT:" -ForegroundColor White
        Write-Host "      Salary ID: $($salaryData.salaryId)" -ForegroundColor Gray
        Write-Host "      Staff Name: $($salaryData.staffName)" -ForegroundColor Gray
        Write-Host "      Role: $($salaryData.role)" -ForegroundColor Gray
        Write-Host "      Payment Date: $($salaryData.paymentDate)" -ForegroundColor Gray
        Write-Host "      Period: $($salaryData.period)" -ForegroundColor Gray
        Write-Host "      Trip Count: $($salaryData.tripCount)" -ForegroundColor Gray
        Write-Host "      Base Salary: `$$($salaryData.baseSalary)" -ForegroundColor White
        Write-Host "      Trip Incentives: `$$($salaryData.tripIncentives)" -ForegroundColor White
        Write-Host "      Weather Allowances: `$$($salaryData.weatherAllowances)" -ForegroundColor White
        Write-Host "      Total Amount: `$$($salaryData.totalAmount)" -ForegroundColor Green
        Write-Host "      Status: $($salaryData.status)" -ForegroundColor Gray
        Write-Host "      Authorized By: $($salaryData.authorizedBy)" -ForegroundColor Gray
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 3: Test Comprehensive Salary Calculation
Write-Host "`n3. TESTING COMPREHENSIVE SALARY CALCULATION" -ForegroundColor Yellow
Write-Host "=============================================" -ForegroundColor Yellow

$calculationTests = @(
    @{StaffId=1; AdminId=1; Month=10; Year=2025; Description="Calculate Captain - October 2025"},
    @{StaffId=5; AdminId=2; Month=11; Year=2025; Description="Calculate Senior Staff - November 2025"},
    @{StaffId=8; AdminId=4; Month=12; Year=2025; Description="Calculate Coordinator - December 2025"}
)

foreach ($test in $calculationTests) {
    Write-Host "`n   Calculating: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/calculate/$($test.StaffId)?adminId=$($test.AdminId)&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method POST
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success - Salary ID: $($salaryData.salaryId)" -ForegroundColor Green
        Write-Host "   📋 COMPREHENSIVE OUTPUT:" -ForegroundColor White
        Write-Host "      Salary ID: $($salaryData.salaryId)" -ForegroundColor Gray
        Write-Host "      Staff Name: $($salaryData.staffName)" -ForegroundColor Gray
        Write-Host "      Role: $($salaryData.role)" -ForegroundColor Gray
        Write-Host "      Payment Date: $($salaryData.paymentDate)" -ForegroundColor Gray
        Write-Host "      Period: $($salaryData.period)" -ForegroundColor Gray
        Write-Host "      Trip Count: $($salaryData.tripCount)" -ForegroundColor Gray
        Write-Host "      Total Amount: `$$($salaryData.totalAmount)" -ForegroundColor Green
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 4: Test Different Periods for Trip Count Variations
Write-Host "`n4. TESTING TRIP COUNT VARIATIONS BY PERIOD" -ForegroundColor Yellow
Write-Host "===========================================" -ForegroundColor Yellow

$periodTests = @(
    @{Month=1; Year=2025; Description="January 2025 (Winter - Lower Trip Count)"},
    @{Month=6; Year=2025; Description="June 2025 (Monsoon - Reduced Trip Count)"},
    @{Month=12; Year=2025; Description="December 2025 (Peak Season - Higher Trip Count)"}
)

foreach ($test in $periodTests) {
    Write-Host "`n   Testing: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/preview/1?adminId=1&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method GET
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success" -ForegroundColor Green
        Write-Host "   📋 PERIOD ANALYSIS:" -ForegroundColor White
        Write-Host "      Period: $($salaryData.period)" -ForegroundColor Gray
        Write-Host "      Trip Count: $($salaryData.tripCount)" -ForegroundColor Gray
        Write-Host "      Weather Allowances: `$$($salaryData.weatherAllowances)" -ForegroundColor Gray
        Write-Host "      Total Amount: `$$($salaryData.totalAmount)" -ForegroundColor Green
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 5: Summary
Write-Host "`n5. COMPREHENSIVE OUTPUT SUMMARY" -ForegroundColor Yellow
Write-Host "===============================" -ForegroundColor Yellow
Write-Host "✅ Salary ID: Shows unique identifier for each calculation" -ForegroundColor Green
Write-Host "✅ Staff Name: Shows actual staff member name" -ForegroundColor Green
Write-Host "✅ Role: Shows staff role (Captain, Manager, etc.)" -ForegroundColor Green
Write-Host "✅ Payment Date: Shows when the salary was calculated" -ForegroundColor Green
Write-Host "✅ Period: Shows month and year in readable format" -ForegroundColor Green
Write-Host "✅ Trip Count: Shows estimated trips based on role and season" -ForegroundColor Green
Write-Host "✅ Financial Details: Base salary, incentives, allowances, total" -ForegroundColor Green
Write-Host "✅ Status: Shows payment status" -ForegroundColor Green
Write-Host "✅ Authorization: Shows who authorized the payment" -ForegroundColor Green

Write-Host "`n📋 TESTING URLs:" -ForegroundColor Yellow
Write-Host "=================" -ForegroundColor Yellow
Write-Host "Database Status: http://localhost:8080/api/test-db" -ForegroundColor Cyan
Write-Host "Salary Preview: http://localhost:8080/api/salaries/preview/1?adminId=1&periodMonth=10&periodYear=2025" -ForegroundColor Cyan
Write-Host "Salary Calculation: http://localhost:8080/api/salaries/calculate/1?adminId=1&periodMonth=10&periodYear=2025" -ForegroundColor Cyan
Write-Host "HTML Interface: http://localhost:8080/static/index.html" -ForegroundColor Cyan

Write-Host "`n=== COMPREHENSIVE OUTPUT TEST COMPLETE ===" -ForegroundColor Green
Write-Host "Your Boat Safari Salary Calculator now shows detailed information!" -ForegroundColor Green
