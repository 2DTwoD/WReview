function main() {
    const pathname = window.location.pathname;
    const pageTitle = document.getElementById("pageTitle");
    switch (pathname){
        case "/":
            pageTitle.innerText = "Рейтинг рабочих";
            break;
        case "/persons/edit":
            pageTitle.innerText = "Мои настройки";
            break;
        case "/persons/workers":
            pageTitle.innerText = "Список рабочих";
            break;
        case "/persons":
            pageTitle.innerText = "Список пользователей";
            break;
        case "/reviews":
            pageTitle.innerText = "Все отзывы";
            break;
        case "/reviews/my":
            pageTitle.innerText = "Мои отзывы";
            break;
        case "/reviews/add":
            pageTitle.innerText = "Добавить отзыв";
            break;
        case "/reviews/on_me":
            pageTitle.innerText = "Отзывы на меня";
            break;
        case "/login":
            pageTitle.innerText = "Введите требуемые данные";
            break;
        case "/registration":
            pageTitle.innerText = "Регистрация";
            break;
        default:
            pageTitle.innerText = "Подробнее";
    }
}
window.addEventListener('load', main);