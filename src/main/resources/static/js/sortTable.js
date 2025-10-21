document.addEventListener('DOMContentLoaded', () => {
    const getCellValue = (tr, idx) =>
        tr.children[idx].innerText || tr.children[idx].textContent;

    const comparer = (idx, asc) => (a, b) =>
        ((v1, v2) =>
                !isNaN(v1) && !isNaN(v2)
                    ? v1 - v2
                    : v1.toString().localeCompare(v2)
        )(getCellValue(asc ? a : b, idx), getCellValue(asc ? b : a, idx));

    document.querySelectorAll('th.sortable').forEach(th =>
        th.addEventListener('click', function () {
            const table = th.closest('table');
            Array.from(table.querySelectorAll('tbody tr'))
                .sort(comparer(Array.from(th.parentNode.children).indexOf(th),
                    (this.asc = !this.asc)))
                .forEach(tr => table.querySelector('tbody').appendChild(tr));
        })
    );

    // Row click navigation
    document.querySelectorAll('.booking-table tbody tr').forEach(tr => {
        tr.style.cursor = 'pointer';
        tr.addEventListener('click', () => {
            const url = tr.getAttribute('data-href');
            if (url) window.location.href = url;
        });
    });
});