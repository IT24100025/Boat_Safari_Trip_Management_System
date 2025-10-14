document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("table[data-clickable] tbody tr").forEach(row => {
        row.addEventListener("click", () => {
            const href = row.getAttribute("data-href");
            if (href) {
                window.location.href = href;
            }
        });
    });
});
