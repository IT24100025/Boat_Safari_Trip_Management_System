# Test script for Boat Safari Salary System - Audit Logs
Write-Host "=== BOAT SAFARI SALARY SYSTEM - AUDIT LOG TEST ===" -ForegroundColor Green

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

# Create some salary calculations to generate audit logs
Write-Host "`n2. Creating Salary Calculations (to generate audit logs):" -ForegroundColor Yellow

$testCalculations = @(
    @{StaffId=1; AdminId=1; Month=10; Year=2025},
    @{StaffId=2; AdminId=1; Month=10; Year=2025},
    @{StaffId=1; AdminId=2; Month=11; Year=2025}
)

foreach ($calc in $testCalculations) {
    Write-Host "`nCalculating salary for Staff ID $($calc.StaffId) by Admin ID $($calc.AdminId):" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/calculate/$($calc.StaffId)?adminId=$($calc.AdminId)&periodMonth=$($calc.Month)&periodYear=$($calc.Year)"
        $response = Invoke-WebRequest $url | ConvertFrom-Json
        
        Write-Host "  ✅ Salary calculated successfully" -ForegroundColor Green
        Write-Host "  Salary ID: $($response.salaryId)" -ForegroundColor White
        Write-Host "  Total Amount: `$$($response.totalAmount)" -ForegroundColor White
    } catch {
        Write-Host "  ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test audit log endpoints
Write-Host "`n3. Testing Audit Log Endpoints:" -ForegroundColor Yellow

# Test 1: Get all audit logs
Write-Host "`n3.1. Getting all audit logs:" -ForegroundColor Cyan
try {
    $allAudits = Invoke-WebRequest "http://localhost:8080/api/salary-audits" | ConvertFrom-Json
    Write-Host "  Total audit logs: $($allAudits.Count)" -ForegroundColor White
    if ($allAudits.Count -gt 0) {
        Write-Host "  Latest audit: $($allAudits[0].actionType) on $($allAudits[0].actionDate)" -ForegroundColor White
    }
} catch {
    Write-Host "  ❌ Error getting all audits: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Get audit logs by Salary ID
Write-Host "`n3.2. Getting audit logs by Salary ID (1):" -ForegroundColor Cyan
try {
    $salaryAudits = Invoke-WebRequest "http://localhost:8080/api/salary-audits/1" | ConvertFrom-Json
    Write-Host "  Audit logs for Salary ID 1: $($salaryAudits.Count)" -ForegroundColor White
    foreach ($audit in $salaryAudits) {
        Write-Host "    - $($audit.actionType): $($audit.changes)" -ForegroundColor Gray
    }
} catch {
    Write-Host "  ❌ Error getting salary audits: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Get audit logs by Salary ID and Admin ID
Write-Host "`n3.3. Getting audit logs by Salary ID (1) and Admin ID (1):" -ForegroundColor Cyan
try {
    $filteredAudits = Invoke-WebRequest "http://localhost:8080/api/salary-audits/1/admin/1" | ConvertFrom-Json
    Write-Host "  Audit logs for Salary ID 1 by Admin ID 1: $($filteredAudits.Count)" -ForegroundColor White
    foreach ($audit in $filteredAudits) {
        Write-Host "    - $($audit.actionType): $($audit.changes)" -ForegroundColor Gray
    }
} catch {
    Write-Host "  ❌ Error getting filtered audits: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n4. Available Audit Log Endpoints:" -ForegroundColor Yellow
Write-Host "Get all audit logs: http://localhost:8080/api/salary-audits" -ForegroundColor Cyan
Write-Host "Get by Salary ID: http://localhost:8080/api/salary-audits/{salaryId}" -ForegroundColor Cyan
Write-Host "Get by Salary ID & Admin ID: http://localhost:8080/api/salary-audits/{salaryId}/admin/{adminId}" -ForegroundColor Cyan
Write-Host "Delete audit log: http://localhost:8080/api/salary-audits/{auditId}?adminId={adminId}" -ForegroundColor Cyan

Write-Host "`n=== AUDIT LOG TEST COMPLETE ===" -ForegroundColor Green
