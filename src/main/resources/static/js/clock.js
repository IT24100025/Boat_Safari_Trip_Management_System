document.addEventListener('DOMContentLoaded', () => {
    const el = document.getElementById('currentDateTime');
    function updateClock() {
        const now = new Date();
        const opts = {
            year: 'numeric', month: 'short', day: '2-digit',
            hour: '2-digit', minute: '2-digit', second: '2-digit'
        };
        el.textContent = now.toLocaleDateString(undefined, opts);
    }
    updateClock();
    setInterval(updateClock, 1000);
});
