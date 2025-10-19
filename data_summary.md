# Boat Safari Salary Calculator - Test Data Summary

## 📊 **Available Test Data**

### **Staff Members (8 total)**
| Staff ID | Location | City | Availability | Base Salary | Role |
|----------|----------|------|--------------|-------------|------|
| 1 | 101 Ocean Dr | Colombo | Available | $75,000 | Captain |
| 2 | 202 Beach Rd | Galle | Available | $65,000 | Senior Staff |
| 3 | 303 Harbor St | Negombo | Unavailable | $55,000 | Regular Staff |
| 4 | 404 Marina Blvd | Trincomalee | Available | $80,000 | Manager |
| 5 | 505 Port Rd | Kalpitiya | Available | $70,000 | Senior Staff |
| 6 | 606 Harbor View | Batticaloa | Unavailable | $60,000 | Regular Staff |
| 7 | 707 Coastal Dr | Jaffna | Available | $85,000 | Senior Manager |
| 8 | 808 Bay St | Hambantota | Available | $72,000 | Coordinator |

### **Admin Users (4 total)**
| Admin ID | Name | Email | Role |
|----------|------|-------|------|
| 1 | Admin One | admin1@example.com | Admin |
| 2 | Admin Two | admin2@example.com | Admin |
| 3 | Admin Three | admin3@example.com | Senior Admin |
| 4 | Admin Four | admin4@example.com | HR Manager |

### **Sample Salary Payments (12 total)**
- **September 2025**: 3 payments (Staff 1, 2, 3) - Status: Paid
- **October 2025**: 4 payments (Staff 1, 2, 4, 5) - Status: Pending
- **November 2025**: 5 payments (Staff 1, 2, 6, 7, 8) - Status: Calculated

### **Audit Logs (18 total)**
- **Salary Calculations**: 12 entries
- **Salary Approvals**: 2 entries
- **Salary Reviews**: 2 entries
- **Pending Approvals**: 2 entries

## 🧪 **Test Scenarios**

### **1. Salary Preview Tests**
```
Staff 1 (Captain) - October 2025: ~$84,250
Staff 4 (Manager) - November 2025: ~$91,500
Staff 7 (Senior Manager) - December 2025: ~$98,000
```

### **2. Different Periods**
- **January 2025**: Winter season (lower weather allowances)
- **June 2025**: Summer season (higher weather allowances)
- **December 2025**: Holiday season (highest weather allowances)

### **3. Audit Log Tests**
- **All Audit Logs**: `/api/salary-audits`
- **By Salary ID**: `/api/salary-audits/{salaryId}`
- **By Salary ID and Admin**: `/api/salary-audits/{salaryId}/admin/{adminId}`

## 🎯 **Recommended Test URLs**

### **Database Status**
```
http://localhost:8080/api/test-db
```

### **Salary Previews**
```
http://localhost:8080/api/salaries/preview/1?adminId=1&periodMonth=10&periodYear=2025
http://localhost:8080/api/salaries/preview/4?adminId=2&periodMonth=11&periodYear=2025
http://localhost:8080/api/salaries/preview/7?adminId=3&periodMonth=12&periodYear=2025
```

### **Salary Calculations**
```
http://localhost:8080/api/salaries/calculate/1?adminId=1&periodMonth=10&periodYear=2025
http://localhost:8080/api/salaries/calculate/5?adminId=2&periodMonth=11&periodYear=2025
http://localhost:8080/api/salaries/calculate/8?adminId=4&periodMonth=1&periodYear=2026
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

## 🔧 **Setup Instructions**

1. **Run the SQL script** to add comprehensive test data:
   ```sql
   -- Run: add_comprehensive_test_data.sql
   ```

2. **Test the system** with the PowerShell script:
   ```powershell
   .\test_comprehensive_data.ps1
   ```

3. **Use the HTML interface** to test all functionalities:
   - Salary Preview
   - Salary Calculation
   - Audit Log Viewing

## 📈 **Expected Results**

- **8 Staff members** with different salary levels
- **4 Admin users** for authorization
- **12 Salary payments** across different periods
- **18 Audit log entries** for tracking
- **Seasonal variations** in weather allowances
- **Different statuses** (Paid, Pending, Calculated)

This comprehensive test data will allow you to thoroughly test all aspects of the Boat Safari Salary Calculator system!
