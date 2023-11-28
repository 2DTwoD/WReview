function main(){
    const pathname = window.location.pathname;
    const selectPage = document.getElementById("selectPage");
    const urlParams = new URLSearchParams(window.location.search);
    selectPage.value = urlParams.get("pageNum") === null? 1: urlParams.get("pageNum") + 1;
    const filt = urlParams.get("filter") === null? '': urlParams.get("filter");
    selectPage.addEventListener("change", () =>{
        const pageNum = selectPage.value - 1;
        window.location.href = pathname + '?pageNum=' + pageNum + '&filter=' + filt;
        console.log(111)
    })

}
window.addEventListener('load', main);