# Complete Test for Salary Audit Log System
Write-Host "=== SALARY AUDIT LOG SYSTEM - COMPLETE TEST ===" -ForegroundColor Green

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

# Test 2: Create Salary Calculations to Generate Audit Logs
Write-Host "`n2. CREATING SALARY CALCULATIONS (to generate audit logs)" -ForegroundColor Yellow
Write-Host "=======================================================" -ForegroundColor Yellow

$testCases = @(
    @{StaffId=1; AdminId=1; Month=10; Year=2025; Description="Staff 1 by Admin 1"},
    @{StaffId=2; AdminId=1; Month=10; Year=2025; Description="Staff 2 by Admin 1"},
    @{StaffId=1; AdminId=2; Month=11; Year=2025; Description="Staff 1 by Admin 2"},
    @{StaffId=3; AdminId=1; Month=12; Year=2025; Description="Staff 3 by Admin 1"}
)

$createdSalaries = @()

foreach ($test in $testCases) {
    Write-Host "`n   Creating: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/calculate/$($test.StaffId)?adminId=$($test.AdminId)&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method POST
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success - Salary ID: $($salaryData.salaryId)" -ForegroundColor Green
        Write-Host "   💰 Amount: `$$($salaryData.totalAmount)" -ForegroundColor White
        
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

# Test 3: Check All Audit Logs
Write-Host "`n3. TESTING AUDIT LOG RETRIEVAL" -ForegroundColor Yellow
Write-Host "===============================" -ForegroundColor Yellow

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

# Test 4: Check Audit Logs by Salary ID
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

# Test 5: Check Audit Logs by Salary ID and Admin ID
if ($createdSalaries.Count -gt 0) {
    $testSalaryId = $createdSalaries[0].SalaryId
    $testAdminId = $createdSalaries[0].AdminId
    Write-Host "`n   Testing: Get Audit Logs by Salary ID ($testSalaryId) and Admin ID ($testAdminId)" -ForegroundColor Cyan
    try {
        $filteredAudits = Invoke-WebRequest "http://localhost:8080/api/salary-audits/$testSalaryId/admin/$testAdminId" -Method GET | ConvertFrom-Json
        Write-Host "   ✅ Found $($filteredAudits.Count) audit logs for Salary ID $testSalaryId by Admin ID $testAdminId" -ForegroundColor Green
    } catch {
        Write-Host "   ❌ Error getting filtered audits: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 6: HTML Interface Test
Write-Host "`n4. HTML INTERFACE TEST" -ForegroundColor Yellow
Write-Host "=======================" -ForegroundColor Yellow
Write-Host "`n   🌐 Open your browser and go to:" -ForegroundColor Cyan
Write-Host "   http://localhost:8080/static/index.html" -ForegroundColor White
Write-Host "`n   📋 Test Steps:" -ForegroundColor Cyan
Write-Host "   1. Click on 'Salary Audit Log' tab" -ForegroundColor White
Write-Host "   2. Enter Salary ID: $($createdSalaries[0].SalaryId)" -ForegroundColor White
Write-Host "   3. Enter Admin ID: $($createdSalaries[0].AdminId)" -ForegroundColor White
Write-Host "   4. Click 'View Audit Logs'" -ForegroundColor White
Write-Host "   5. You should see audit logs displayed" -ForegroundColor White

# Test 7: Summary
Write-Host "`n5. TEST SUMMARY" -ForegroundColor Yellow
Write-Host "================" -ForegroundColor Yellow
Write-Host "✅ Salary calculations created: $($createdSalaries.Count)" -ForegroundColor Green
Write-Host "✅ Audit logs should be automatically generated" -ForegroundColor Green
Write-Host "✅ Audit endpoints should be working" -ForegroundColor Green
Write-Host "✅ HTML interface should display audit logs" -ForegroundColor Green

Write-Host "`n📋 MANUAL TESTING URLs:" -ForegroundColor Yellow
Write-Host "=======================" -ForegroundColor Yellow
Write-Host "Database Status: http://localhost:8080/api/test-db" -ForegroundColor Cyan
Write-Host "All Audit Logs: http://localhost:8080/api/salary-audits" -ForegroundColor Cyan
Write-Host "HTML Interface: http://localhost:8080/static/index.html" -ForegroundColor Cyan

Write-Host "`n=== TEST COMPLETE ===" -ForegroundColor Green
