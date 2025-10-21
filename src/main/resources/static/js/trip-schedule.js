// JavaScript for Trip Schedule Management
document.addEventListener('DOMContentLoaded', function() {
    // Form validation for create/edit forms
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            const duration = document.getElementById('duration');
            const departureTime = document.getElementById('departureTime');

            // Validate duration
            if (duration && (duration.value <= 0 || duration.value > 480)) {
                alert('Duration must be between 1 and 480 minutes.');
                event.preventDefault();
                return;
            }

            // Validate departure time
            if (departureTime && departureTime.value) {
                const selectedTime = new Date(departureTime.value);
                const now = new Date();

                if (selectedTime <= now) {
                    alert('Departure time must be in the future.');
                    event.preventDefault();
                    return;
                }
            }
        });
    });

    // Real-time validation for duration field
    const durationField = document.getElementById('duration');
    if (durationField) {
        durationField.addEventListener('input', function() {
            const value = parseInt(this.value);
            if (value <= 0) {
                this.classList.add('is-invalid');
            } else {
                this.classList.remove('is-invalid');
            }
        });
    }

    // Auto-calculate end time based on duration and departure time
    const departureTimeField = document.getElementById('departureTime');
    const durationFieldCalc = document.getElementById('duration');

    if (departureTimeField && durationFieldCalc) {
        const updateEndTime = function() {
            const departureTime = departureTimeField.value;
            const duration = parseInt(durationFieldCalc.value);

            if (departureTime && duration > 0) {
                const start = new Date(departureTime);
                const end = new Date(start.getTime() + duration * 60000);

                // Display end time (you could add this to the form if needed)
                console.log('Trip ends at:', end.toLocaleString());
            }
        };

        departureTimeField.addEventListener('change', updateEndTime);
        durationFieldCalc.addEventListener('input', updateEndTime);
    }

    // Confirmation for delete and cancel actions
    const confirmAction = function(message) {
        return confirm(message);
    };

    // Add confirmation to delete buttons
    const deleteButtons = document.querySelectorAll('form[action*="delete"] button[type="submit"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            if (!confirm('Are you sure you want to permanently delete this trip? This action cannot be undone.')) {
                event.preventDefault();
            }
        });
    });

    // Add confirmation to cancel buttons
    const cancelButtons = document.querySelectorAll('form[action*="cancel"] button[type="submit"]');
    cancelButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            const reason = document.getElementById('cancellationReason');
            if (reason && !reason.value.trim()) {
                alert('Please provide a cancellation reason.');
                event.preventDefault();
                return;
            }

            if (!confirm('Are you sure you want to cancel this trip?')) {
                event.preventDefault();
            }
        });
    });
});

// Utility function to format dates
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
}

// Function to check availability in real-time (could be enhanced with AJAX)
function checkAvailability(boatId, staffId, departureTime, duration, currentTripId = null) {
    // This would typically make an AJAX call to the server
    // For now, it's a placeholder for the actual implementation
    console.log('Checking availability for:', {boatId, staffId, departureTime, duration});

    // Return a promise that would resolve with availability status
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({available: true, conflicts: []});
        }, 100);
    });
}