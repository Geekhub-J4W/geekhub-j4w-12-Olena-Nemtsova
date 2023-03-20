function loadOrder() {
    let id = getCookie("orderId");

    let request = initRequest();
    request.open("GET", "orders/" + id);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let order = request.response;

            document.getElementById("date").value = order.dateTime;
            document.getElementById("totalPrice").value = order.totalPrice;
            document.getElementById("status").value = order.status;
        }
    }
    request.send();
}


function submit() {
    let id = getCookie("orderId");

    let order = {
        id: Number(id),
        date: document.getElementById("date").value,
        totalPrice: Number(document.getElementById("totalPrice").value),
        userId: getCookie("customerId"),
        status: document.getElementById("status").value
    };

    let request = initRequest();

    request.open("POST", "orders/editOrder/" + id);

    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            message = "order successfully edited!";
            alert(message);
            allOrders();
        }
    }
    let obj = JSON.stringify(order);

    request.send(obj);
}

