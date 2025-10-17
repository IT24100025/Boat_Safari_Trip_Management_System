-- Create feedback table for SQL Server
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='feedback' AND xtype='U')
BEGIN
    CREATE TABLE feedback (
        feedback_id INT IDENTITY(1,1) PRIMARY KEY,
        customer_id INT NOT NULL,
        trip_id INT NOT NULL,
        booking_id INT NOT NULL,
        rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
        comment NVARCHAR(MAX),
        city NVARCHAR(100),
        country NVARCHAR(100),
        submitted_date DATETIME2 DEFAULT GETDATE()
    );
END
GO

-- Create indexes for better performance
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_feedback_customer_id')
BEGIN
    CREATE INDEX IX_feedback_customer_id ON feedback(customer_id);
END
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_feedback_trip_id')
BEGIN
    CREATE INDEX IX_feedback_trip_id ON feedback(trip_id);
END
GO

IF NOT EXISTS (SELECT * FROM sys.indexes WHERE name = 'IX_feedback_rating')
BEGIN
    CREATE INDEX IX_feedback_rating ON feedback(rating);
END
GO

-- Insert sample data only if table is empty
IF NOT EXISTS (SELECT 1 FROM feedback)
BEGIN
    INSERT INTO feedback (customer_id, trip_id, booking_id, rating, comment, city, country, submitted_date) VALUES
    (1, 101, 1001, 5, 'Amazing boat safari experience! The guide was knowledgeable and we saw many dolphins.', 'Colombo', 'Sri Lanka', '2024-01-15 10:30:00'),
    (2, 102, 1002, 4, 'Great trip overall, but the boat was a bit crowded.', 'Galle', 'Sri Lanka', '2024-01-16 14:20:00'),
    (3, 101, 1003, 5, 'Perfect weather and excellent service. Highly recommended!', 'Negombo', 'Sri Lanka', '2024-01-17 09:15:00'),
    (1, 103, 1004, 3, 'Average experience. The boat was old and noisy.', 'Trincomalee', 'Sri Lanka', '2024-01-18 16:45:00'),
    (4, 102, 1005, 5, 'Outstanding! We saw whales and dolphins. Unforgettable!', 'Mirissa', 'Sri Lanka', '2024-01-19 11:30:00');
END
GO
