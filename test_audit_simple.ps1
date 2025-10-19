# Simple Test for Audit Log System
Write-Host "=== AUDIT LOG SYSTEM TEST ===" -ForegroundColor Green

# Step 1: Check if application is running
Write-Host "`n1. Checking application status..." -ForegroundColor Yellow
try {
    $dbTest = Invoke-WebRequest "http://localhost:8080/api/test-db" | ConvertFrom-Json
    Write-Host "✅ Application is running" -ForegroundColor Green
    Write-Host "   Status: $($dbTest.status)" -ForegroundColor White
} catch {
    Write-Host "❌ Application not running. Please start it first!" -ForegroundColor Red
    exit 1
}

# Step 2: Create a salary calculation to generate audit log
Write-Host "`n2. Creating salary calculation..." -ForegroundColor Yellow
try {
    $salaryResponse = Invoke-WebRequest "http://localhost:8080/api/salaries/calculate/1?adminId=1&periodMonth=10&periodYear=2025" -Method POST
    $salaryData = $salaryResponse.Content | ConvertFrom-Json
    Write-Host "✅ Salary created - ID: $($salaryData.salaryId)" -ForegroundColor Green
    Write-Host "💰 Amount: `$$($salaryData.totalAmount)" -ForegroundColor White
} catch {
    Write-Host "❌ Error creating salary: $($_.Exception.Message)" -ForegroundColor Red
}

# Step 3: Test audit logs
Write-Host "`n3. Testing audit logs..." -ForegroundColor Yellow
try {
    $auditResponse = Invoke-WebRequest "http://localhost:8080/api/salary-audits" -Method GET
    Write-Host "✅ Audit endpoint working!" -ForegroundColor Green
    Write-Host "Status: $($auditResponse.StatusCode)" -ForegroundColor White
    
    if ($auditResponse.Content -ne "[]" -and $auditResponse.Content -ne "null") {
        $audits = $auditResponse.Content | ConvertFrom-Json
        Write-Host "📋 Found $($audits.Count) audit logs" -ForegroundColor White
        if ($audits.Count -gt 0) {
            Write-Host "   Recent audit: $($audits[0].actionType)" -ForegroundColor Gray
        }
    } else {
        Write-Host "⚠️ No audit logs found (this might be normal if table was just created)" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Error getting audit logs: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "💡 This usually means the SalaryAudit table doesn't exist in your database" -ForegroundColor Yellow
    Write-Host "💡 Please run the fix_audit_table.sql script in SQL Server Management Studio" -ForegroundColor Yellow
}

Write-Host "`n=== TEST COMPLETE ===" -ForegroundColor Green
Write-Host "If you see errors, run the fix_audit_table.sql script first!" -ForegroundColor Yellow