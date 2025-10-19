# 🚀 Step-by-Step Guide: Adding Comprehensive Test Data

## **Step 1: Open SQL Server Management Studio**
1. Open SQL Server Management Studio (SSMS)
2. Connect to your local SQL Server instance
3. Make sure you're connected to the `Payment` database

## **Step 2: Run the SQL Script**
1. Open the file: `add_comprehensive_test_data.sql`
2. Copy the entire contents
3. Paste into a new query window in SSMS
4. Click **Execute** (or press F5)

## **Step 3: Verify the Data**
After running the script, you should see:
- ✅ **8 Staff members** (Staff ID 1-8)
- ✅ **4 Admin users** (Admin ID 1-4)  
- ✅ **12 Salary payments** across different periods
- ✅ **18 Audit log entries**

## **Step 4: Test the System**
Run these test URLs in your browser:

### **Database Status**
```
http://localhost:8080/api/test-db
```
*Expected: `{"status":"success","staffCount":8}`*

### **Salary Previews**
```
http://localhost:8080/api/salaries/preview/1?adminId=1&periodMonth=10&periodYear=2025
http://localhost:8080/api/salaries/preview/4?adminId=2&periodMonth=11&periodYear=2025
http://localhost:8080/api/salaries/preview/7?adminId=3&periodMonth=12&periodYear=2025
```

### **Audit Logs**
```
http://localhost:8080/api/salary-audits
http://localhost:8080/api/salary-audits/1
http://localhost:8080/api/salary-audits/1/admin/1
```

### **HTML Interface**
```
http://localhost:8080/static/index.html
```

## **Step 5: Expected Results**

### **Staff Members (8 total)**
| Staff ID | Role | Base Salary | Location |
|----------|------|--------------|----------|
| 1 | Captain | $75,000 | Colombo |
| 2 | Senior Staff | $65,000 | Galle |
| 3 | Regular Staff | $55,000 | Negombo |
| 4 | Manager | $80,000 | Trincomalee |
| 5 | Senior Staff | $70,000 | Kalpitiya |
| 6 | Regular Staff | $60,000 | Batticaloa |
| 7 | Senior Manager | $85,000 | Jaffna |
| 8 | Coordinator | $72,000 | Hambantota |

### **Sample Salary Calculations**
- **Staff 1 (Captain)**: ~$84,250 total
- **Staff 4 (Manager)**: ~$91,500 total  
- **Staff 7 (Senior Manager)**: ~$98,000 total

### **Audit Logs**
- **12 Salary calculations**
- **2 Salary approvals**
- **2 Salary reviews**
- **2 Pending approvals**

## **Step 6: Troubleshooting**

**If you get errors:**
1. Make sure you're connected to the `Payment` database
2. Check that the `SalaryAudit` table exists (run the `fix_audit_table.sql` first if needed)
3. Verify your SQL Server is running
4. Check the application logs for any errors

**If the application shows errors:**
1. Restart your Spring Boot application
2. Check the database connection
3. Verify all tables exist

## **Step 7: Test Scenarios**

### **Test 1: Different Staff Members**
- Test salary preview for Staff 1, 4, 7 (different salary levels)
- Test salary calculation for different staff

### **Test 2: Different Periods**
- January 2025 (Winter - lower weather allowances)
- June 2025 (Summer - higher weather allowances)
- December 2025 (Holiday season - highest weather allowances)

### **Test 3: Audit Logs**
- View all audit logs
- Filter by specific salary ID
- Filter by salary ID and admin ID

### **Test 4: HTML Interface**
- Use the web interface to test all functionalities
- Try different staff members and periods
- Test the audit log viewing

## **🎯 Success Indicators**

✅ **Database Status**: Shows 8 staff members  
✅ **Salary Previews**: Working for all staff members  
✅ **Salary Calculations**: Creating new records  
✅ **Audit Logs**: Showing historical data  
✅ **HTML Interface**: All tabs working properly  

**You're all set! Your Boat Safari Salary Calculator now has comprehensive test data to work with.**
