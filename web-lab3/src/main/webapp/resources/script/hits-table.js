
let socket = new WebSocket("ws://localhost:18030/web-lab3-1.0-SNAPSHOT/hits");

socket.onmessage = function(event) {
    console.log("WS!")
    const data = JSON.parse(event.data);

    PrimeFaces.ab({
        s: 'hits-table-form:hits-table-btn',
        f: 'hits-table-form',
        u: 'hits-table-form:hits-table'
    });

    updateHits(data);
};
socket.onopen = function() {
    console.log("WebSocket connected!");
};

socket.onerror = function(error) {
    console.error("WebSocket error:", error);
};

socket.onclose = function(event) {
    console.log("WebSocket closed:", event);
};