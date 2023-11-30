function main() {
    const filterButton = document.getElementById("filterButton");
    const clearButton = document.getElementById("clearButton");
    const textField = document.getElementById("filterTextField");
    const pathname = window.location.pathname;
    textField.addEventListener("keypress", (e) => {
        if (e.key === 'Enter' || e.keyCode === 13) {
            window.location.href = pathname + '?filter=' + textField.value
        }
    });
    filterButton.addEventListener("click", function () {
        window.location.href = pathname + '?filter=' + textField.value;
    }, false);
    clearButton.addEventListener("click", function () {
        if(confirm("Очистить фильтр?")) {
            window.location.href = pathname;
        }
    }, false);
}
window.addEventListener('load', main);