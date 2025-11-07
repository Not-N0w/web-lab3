const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");
const tooltip = document.getElementById("tooltip");


var isInteractive = true;

const imagePartRatio = 0.7;
const letterHeight = 15;
const hatchLength = 10;

var localR, canvasHeight, canvasWidth;

function resizeCanvas() {
    const rect = canvas.getBoundingClientRect();
    const cssWidth = rect.width;
    const cssHeight = rect.height;
    const dpr = window.devicePixelRatio || 1;
    const displayWidth = Math.round(cssWidth * dpr);
    const displayHeight = Math.round(cssHeight * dpr);


    if (canvas.width !== displayWidth || canvas.height !== displayHeight) {
        canvas.width = displayWidth;
        canvas.height = displayHeight;
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.scale(dpr, dpr);

        localR = (canvas.clientWidth/2 * imagePartRatio);
        canvasWidth = canvas.clientWidth;
        canvasHeight = canvas.clientHeight;
    }
}

function drawLine(x1, y1, x2, y2) {
    ctx.moveTo(x1, y1);
    ctx.lineTo(x2, y2);
    ctx.stroke();
}

function updateRadiusAxisText() {
    const r = globalR;
    const isSet = (r != null);

    const rHalf = isSet ? (r / 2).toString() : "R/2";
    const rFull = isSet ? r.toString() : "R";
    const rNegHalf = isSet ? (-r / 2).toString() : "-R/2";
    const rNegFull = isSet ? (-r).toString() : "-R";

    ctx.fillText(rHalf, canvasWidth / 2 + localR / 2 - ctx.measureText(rHalf).width / 2, canvasHeight / 2 + hatchLength / 2 + letterHeight);
    ctx.fillText(rFull, canvasWidth / 2 + localR - ctx.measureText(rFull).width / 2, canvasHeight / 2 + hatchLength / 2 + letterHeight);
    ctx.fillText(rNegHalf, canvasWidth / 2 - localR / 2 - ctx.measureText(rNegHalf).width / 2, canvasHeight / 2 + hatchLength / 2 + letterHeight);
    ctx.fillText(rNegFull, canvasWidth / 2 - localR - ctx.measureText(rNegFull).width / 2, canvasHeight / 2 + hatchLength / 2 + letterHeight);

    ctx.fillText(rHalf, canvasWidth / 2 - hatchLength / 2 - ctx.measureText(rHalf).width - 4, canvasHeight / 2 - localR / 2 + letterHeight / 2 - 2);
    ctx.fillText(rNegHalf, canvasWidth / 2 - hatchLength / 2 - ctx.measureText(rNegHalf).width - 4, canvasHeight / 2 + localR / 2 + letterHeight / 2 - 2);
    ctx.fillText(rFull, canvasWidth / 2 - hatchLength / 2 - ctx.measureText(rFull).width - 4, canvasHeight / 2 - localR + letterHeight / 2 - 2);
    ctx.fillText(rNegFull, canvasWidth / 2 - hatchLength / 2 - ctx.measureText(rNegFull).width - 4, canvasHeight / 2 + localR + letterHeight / 2 - 2);
}


function drawAxis() {
    ctx.strokeStyle = color_light;
    ctx.lineWidth = 1;
    ctx.fillStyle = color_light;
    ctx.font = `${letterHeight}px "Courier New", Courier, monospace`;

    ctx.beginPath();
    drawLine(0, canvasHeight / 2, canvasWidth, canvasHeight / 2);
    drawLine(canvasWidth / 2 + localR / 2, canvasHeight / 2 - hatchLength / 2, canvasWidth / 2 + localR / 2, canvasHeight / 2 + hatchLength / 2);
    drawLine(canvasWidth / 2 + localR, canvasHeight / 2 - hatchLength / 2, canvasWidth / 2 + localR, canvasHeight / 2 + hatchLength / 2);
    drawLine(canvasWidth / 2 - localR / 2, canvasHeight / 2 - hatchLength / 2, canvasWidth / 2 - localR / 2, canvasHeight / 2 + hatchLength / 2);
    drawLine(canvasWidth / 2 - localR, canvasHeight / 2 - hatchLength / 2, canvasWidth / 2 - localR, canvasHeight / 2 + hatchLength / 2);

    ctx.beginPath();
    ctx.moveTo(canvasWidth - hatchLength, canvasHeight / 2 - hatchLength / 2);
    ctx.lineTo(canvasWidth, canvasHeight / 2);
    ctx.lineTo(canvasWidth - hatchLength, canvasHeight / 2 + hatchLength / 2);
    ctx.closePath();
    ctx.fill();

    ctx.fillText("X", canvasWidth - ctx.measureText("X").width - 5, canvasHeight / 2 + hatchLength / 2 + letterHeight);

    drawLine(canvasWidth / 2, 0, canvasWidth / 2, canvasHeight);
    drawLine(canvasWidth / 2 - hatchLength / 2, canvasHeight / 2 - localR / 2, canvasWidth / 2 + hatchLength / 2, canvasHeight / 2 - localR / 2);
    drawLine(canvasWidth / 2 - hatchLength / 2, canvasHeight / 2 + localR / 2, canvasWidth / 2 + hatchLength / 2, canvasHeight / 2 + localR / 2);
    drawLine(canvasWidth / 2 - hatchLength / 2, canvasHeight / 2 - localR, canvasWidth / 2 + hatchLength / 2, canvasHeight / 2 - localR);
    drawLine(canvasWidth / 2 - hatchLength / 2, canvasHeight / 2 + localR, canvasWidth / 2 + hatchLength / 2, canvasHeight / 2 + localR);

    updateRadiusAxisText();

    ctx.beginPath();
    ctx.moveTo(canvasWidth / 2 - hatchLength / 2, hatchLength);
    ctx.lineTo(canvasWidth / 2, 0);
    ctx.lineTo(canvasWidth / 2 + hatchLength / 2, hatchLength);
    ctx.closePath();
    ctx.fill();

    ctx.fillText("Y", canvasWidth / 2 - ctx.measureText("Y").width / 2 - hatchLength / 2 - 10, letterHeight + 2);
    ctx.closePath();
}

function drawHitPoint() {
    if (globalX == null || globalY == null || globalR == null) return;
    ctx.beginPath();

    const y = canvasHeight / 2 - (localR / globalR * globalY);
    for(var i = 0; i < globalX.length; ++i) {
        const x = (localR / globalR * globalX[i]) + canvasWidth / 2;
        ctx.strokeStyle = 'green';
        ctx.lineWidth = 3;
        const size = 10;

        drawLine(x - size/2, y, x + size/2, y)
        drawLine(x, y - size/2, x, y + size/2)
    }
    ctx.closePath();
}


function drawFigure() {
    ctx.beginPath();
    ctx.moveTo(canvasWidth/2, canvasHeight/2);
    ctx.arc(canvasWidth/2, canvasHeight/2, localR/2, 0, -0.5*Math.PI, true);

    ctx.moveTo(canvasWidth/2 + localR/2, canvasHeight/2);
    ctx.lineTo(canvasWidth/2, canvasHeight/2 + localR);
    ctx.lineTo(canvasWidth/2, canvasHeight/2 + localR/2);

    ctx.lineTo(canvasWidth/2 - localR, canvasHeight/2 + localR/2);
    ctx.lineTo(canvasWidth/2 - localR, canvasHeight/2);
    ctx.lineTo(canvasWidth/2 , canvasHeight/2);

    ctx.closePath();
    ctx.fillStyle = color_red_bright;
    ctx.fill();

}


function drawHistory() {
    console.log(hits);
    if(hits==null) return;

    for(var i = 0; i < hits.length; ++i) {
        if(Number(hits[i].r) !== Number(globalR)) continue;

        const y = canvasHeight / 2 - (localR / globalR * Number(hits[i].y));
        const x = (localR / globalR * Number(hits[i].x)) + canvasWidth / 2;

        ctx.beginPath();
        ctx.arc(x, y, 3, 0, Math.PI * 2);
        ctx.fillStyle = (hits[i].isHit ? "#3ece4aff" : "#f57138ff");
        ctx.fill();
    }
}

function fillCanvas() {
    ctx.clearRect(0, 0, canvasWidth, canvasHeight);
    resizeCanvas();
    drawFigure();
    drawAxis();
    drawHistory();
    drawHitPoint();
}
function sendPoint(x, y, r) {
    console.log("sending")
    PrimeFaces.ab({
        s: 'hit-form:canvasHandler',
        f: 'hit-form',
        p: '@this',
        u: 'hits-table',
        pa: [
            {name: 'x', value: x},
            {name: 'y', value: y},
            {name: 'r', value: r}
        ],
        oncomplete: function() {
            fillCanvas()
        }
    });
}

canvas.addEventListener("mousemove", (event) => {
    if(!isInteractive || globalR == null) return;
    const rect = canvas.getBoundingClientRect();

    const x = (event.clientX - rect.left - canvasWidth / 2) * (globalR / localR);
    const y = (canvasHeight / 2 - (event.clientY - rect.top)) * (globalR / localR);

    const roundedX = Math.round(x * 100000) / 100000;
    const roundedY = Math.round(y * 100000) / 100000;

    tooltip.classList.remove("hidden");
    tooltip.style.whiteSpace = "pre";

    const xText = roundedX.toString().padEnd(8, " ");
    const yText = roundedY.toString().padEnd(8, " ");

    tooltip.textContent = `X: ${xText} Y: ${yText}`;

    tooltip.style.left = `${event.pageX + 10}px`;
    tooltip.style.top  = `${event.pageY}px`;
});

canvas.addEventListener(
    "mouseleave",
    () => {
        tooltip.classList.add("hidden")
    }
);

canvas.addEventListener("click", async (event) => {
    if(!isInteractive) return;
    if (globalR == null) {
        dropDialog("error", "R must not be null!");
        return;
    }

    const rect = canvas.getBoundingClientRect();
    const x = (event.clientX - rect.left - canvasWidth / 2) * (globalR / localR);
    const y = (canvasHeight / 2 - (event.clientY - rect.top)) * (globalR / localR);

     sendPoint(x, y, globalR);
});

window.addEventListener('resize', function() {
    fillCanvas()}
);



document.addEventListener("updateGlobalValues", function(event){
    if(event.detail){
        globalX = event.detail.x;
        globalR = event.detail.r;
        globalY = event.detail.y;
        fillCanvas();
        console.log("Updated globals:", globalR, globalX, globalY);
    }
});

document.addEventListener("updateHits", function(event){
    if(event.detail){
        hits = event.detail.hits;
    }
    fillCanvas();
});


fillCanvas()