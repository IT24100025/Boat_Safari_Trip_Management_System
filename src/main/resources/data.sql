-- Seed minimal data for H2 so demo endpoints work

-- Admin (let IDENTITY auto-generate)
INSERT INTO admin (name, email, role)
VALUES ('Admin One', 'admin1@example.com', 'manager');

-- Staff (let IDENTITY auto-generate). Column names are snake_case in H2 per Hibernate naming
INSERT INTO staff (name, role, hire_date, email, phone, status, bank_details, availability_status)
VALUES ('John Crew', 'crew', CURRENT_DATE, 'john.crew@example.com', '555-0101', 'active', 'AC-123', 'available');

-- PayStructure (let IDENTITY auto-generate). Use snake_case column names
INSERT INTO pay_structure (component_type, amount, effective_date, role)
VALUES ('fixed_monthly', 500.00, CURRENT_DATE, NULL);

-- Note: LeaveRequest not seeded to avoid FK dependency on generated staff/admin IDs
