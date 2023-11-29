function main(){
    const selectPage = document.getElementById("selectPage");
    if(selectPage === null){
        return;
    }
    const pathname = window.location.pathname;
    const urlParams = new URLSearchParams(window.location.search);
    selectPage.value = urlParams.get("pageNum") == null? 1: Number(urlParams.get("pageNum")) + 1;
    const filt = urlParams.get("filter") == null? '': urlParams.get("filter");
    selectPage.addEventListener("change", () => {
        const pageNum = selectPage.value - 1;
        window.location.href = pathname + '?pageNum=' + pageNum + '&filter=' + filt;
    })

}
window.addEventListener('load', main);