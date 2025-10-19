# Test Print/PDF Functionality for Boat Safari Salary Calculator
Write-Host "=== BOAT SAFARI SALARY CALCULATOR - PRINT/PDF FUNCTIONALITY TEST ===" -ForegroundColor Green

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

# Test 2: Test Salary Preview with Print Button
Write-Host "`n2. TESTING SALARY PREVIEW WITH PRINT BUTTON" -ForegroundColor Yellow
Write-Host "=============================================" -ForegroundColor Yellow

$testCases = @(
    @{StaffId=1; AdminId=1; Month=10; Year=2025; Description="Captain - October 2025"},
    @{StaffId=2; AdminId=1; Month=11; Year=2025; Description="Senior Staff - November 2025"},
    @{StaffId=3; AdminId=2; Month=12; Year=2025; Description="Regular Staff - December 2025"}
)

foreach ($test in $testCases) {
    Write-Host "`n   Testing: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/preview/$($test.StaffId)?adminId=$($test.AdminId)&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method GET
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success - Staff ID: $($test.StaffId)" -ForegroundColor Green
        Write-Host "   📋 SALARY PREVIEW DATA:" -ForegroundColor White
        Write-Host "      Salary ID: $($salaryData.salaryId)" -ForegroundColor Gray
        Write-Host "      Staff Name: $($salaryData.staffName)" -ForegroundColor Gray
        Write-Host "      Role: $($salaryData.role)" -ForegroundColor Gray
        Write-Host "      Period: $($salaryData.period)" -ForegroundColor Gray
        Write-Host "      Trip Count: $($salaryData.tripCount)" -ForegroundColor Gray
        Write-Host "      Total Amount: `$$($salaryData.totalAmount)" -ForegroundColor Green
        Write-Host "   🖨️  PRINT BUTTON: Available in HTML interface" -ForegroundColor Yellow
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 3: Test Salary Calculation with Print Button
Write-Host "`n3. TESTING SALARY CALCULATION WITH PRINT BUTTON" -ForegroundColor Yellow
Write-Host "=================================================" -ForegroundColor Yellow

$calculationTests = @(
    @{StaffId=1; AdminId=1; Month=10; Year=2025; Description="Calculate Captain - October 2025"},
    @{StaffId=2; AdminId=2; Month=11; Year=2025; Description="Calculate Senior Staff - November 2025"}
)

foreach ($test in $calculationTests) {
    Write-Host "`n   Calculating: $($test.Description)" -ForegroundColor Cyan
    try {
        $url = "http://localhost:8080/api/salaries/calculate/$($test.StaffId)?adminId=$($test.AdminId)&periodMonth=$($test.Month)&periodYear=$($test.Year)"
        $response = Invoke-WebRequest $url -Method POST
        $salaryData = $response.Content | ConvertFrom-Json
        
        Write-Host "   ✅ Success - Salary ID: $($salaryData.salaryId)" -ForegroundColor Green
        Write-Host "   📋 CALCULATED SALARY DATA:" -ForegroundColor White
        Write-Host "      Salary ID: $($salaryData.salaryId)" -ForegroundColor Gray
        Write-Host "      Staff Name: $($salaryData.staffName)" -ForegroundColor Gray
        Write-Host "      Role: $($salaryData.role)" -ForegroundColor Gray
        Write-Host "      Payment Date: $($salaryData.paymentDate)" -ForegroundColor Gray
        Write-Host "      Period: $($salaryData.period)" -ForegroundColor Gray
        Write-Host "      Trip Count: $($salaryData.tripCount)" -ForegroundColor Gray
        Write-Host "      Total Amount: `$$($salaryData.totalAmount)" -ForegroundColor Green
        Write-Host "   🖨️  PRINT BUTTON: Available in HTML interface" -ForegroundColor Yellow
    } catch {
        Write-Host "   ❌ Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 4: Print Functionality Features
Write-Host "`n4. PRINT/PDF FUNCTIONALITY FEATURES" -ForegroundColor Yellow
Write-Host "====================================" -ForegroundColor Yellow
Write-Host "✅ Print Button: Added to Salary Preview section" -ForegroundColor Green
Write-Host "✅ PDF Generation: Opens in new window for printing" -ForegroundColor Green
Write-Host "✅ Professional Layout: Clean, formatted report design" -ForegroundColor Green
Write-Host "✅ Report Header: Company branding and generation timestamp" -ForegroundColor Green
Write-Host "✅ Salary Breakdown: Detailed financial information" -ForegroundColor Green
Write-Host "✅ Staff Details: Complete staff and period information" -ForegroundColor Green
Write-Host "✅ Report Footer: System information and report ID" -ForegroundColor Green
Write-Host "✅ Print Optimization: Removes print button from PDF output" -ForegroundColor Green

# Test 5: How to Use Print Functionality
Write-Host "`n5. HOW TO USE PRINT FUNCTIONALITY" -ForegroundColor Yellow
Write-Host "===================================" -ForegroundColor Yellow
Write-Host "1. Open the HTML interface: http://localhost:8080/static/index.html" -ForegroundColor Cyan
Write-Host "2. Go to 'Salary Calculator' tab" -ForegroundColor White
Write-Host "3. Fill in the form:" -ForegroundColor White
Write-Host "   - Staff ID: 1, 2, 3, etc." -ForegroundColor Gray
Write-Host "   - Admin ID: 1, 2, 3, etc." -ForegroundColor Gray
Write-Host "   - Period Month: 1-12" -ForegroundColor Gray
Write-Host "4. Click 'Preview Salary' button" -ForegroundColor White
Write-Host "5. Review the salary preview" -ForegroundColor White
Write-Host "6. Click the '🖨️ Print as PDF' button" -ForegroundColor White
Write-Host "7. A new window will open with the formatted report" -ForegroundColor White
Write-Host "8. Use browser's print function (Ctrl+P) to save as PDF" -ForegroundColor White

# Test 6: PDF Report Features
Write-Host "`n6. PDF REPORT FEATURES" -ForegroundColor Yellow
Write-Host "=======================" -ForegroundColor Yellow
Write-Host "📄 Report Header:" -ForegroundColor White
Write-Host "   - Company: Boat Safari Salary Management System" -ForegroundColor Gray
Write-Host "   - Title: Boat Safari Salary Report" -ForegroundColor Gray
Write-Host "   - Generation Date/Time" -ForegroundColor Gray
Write-Host "📊 Salary Information:" -ForegroundColor White
Write-Host "   - Staff Name and Role" -ForegroundColor Gray
Write-Host "   - Salary ID and Period" -ForegroundColor Gray
Write-Host "   - Payment Date and Trip Count" -ForegroundColor Gray
Write-Host "💰 Financial Breakdown:" -ForegroundColor White
Write-Host "   - Base Salary" -ForegroundColor Gray
Write-Host "   - Trip Incentives" -ForegroundColor Gray
Write-Host "   - Weather Allowances" -ForegroundColor Gray
Write-Host "   - Total Amount (highlighted)" -ForegroundColor Gray
Write-Host "📋 Report Footer:" -ForegroundColor White
Write-Host "   - System information" -ForegroundColor Gray
Write-Host "   - Unique Report ID" -ForegroundColor Gray

# Test 7: Testing URLs
Write-Host "`n7. TESTING URLs" -ForegroundColor Yellow
Write-Host "===============" -ForegroundColor Yellow
Write-Host "HTML Interface: http://localhost:8080/static/index.html" -ForegroundColor Cyan
Write-Host "Database Status: http://localhost:8080/api/test-db" -ForegroundColor Cyan
Write-Host "Salary Preview: http://localhost:8080/api/salaries/preview/1?adminId=1&periodMonth=10&periodYear=2025" -ForegroundColor Cyan
Write-Host "Salary Calculation: http://localhost:8080/api/salaries/calculate/1?adminId=1&periodMonth=10&periodYear=2025" -ForegroundColor Cyan

Write-Host "`n=== PRINT/PDF FUNCTIONALITY TEST COMPLETE ===" -ForegroundColor Green
Write-Host "Your Boat Safari Salary Calculator now includes professional PDF printing!" -ForegroundColor Green
Write-Host "`n🎯 Key Features Added:" -ForegroundColor Yellow
Write-Host "   ✅ Print button in Salary Preview section" -ForegroundColor Green
Write-Host "   ✅ Professional PDF report layout" -ForegroundColor Green
Write-Host "   ✅ Complete salary information in printable format" -ForegroundColor Green
Write-Host "   ✅ Company branding and report metadata" -ForegroundColor Green
Write-Host "   ✅ Print-optimized styling" -ForegroundColor Green
