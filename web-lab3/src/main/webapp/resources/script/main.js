document.getElementById("sessionIdText").addEventListener("click", function () {
    const text = this.textContent;
    navigator.clipboard.writeText(text);

    const copied = document.getElementById("copied");
    copied.style.opacity = "1";

    setTimeout(() => {
        copied.style.opacity = "0";
    }, 2000);
});