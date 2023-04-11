function loadOrder() {
    const urlParams = new URLSearchParams(window.location.search);
    let orderId = urlParams.get("orderId");

    let request = initRequest();
    request.open("GET", "orders/" + orderId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let order = request.response;

            document.getElementById("date").value = order.dateTime;
            document.getElementById("totalPrice").value = order.totalPrice;
            document.getElementById("status").value = order.status;
            document.getElementById("customerName").value = order.customerName;
            document.getElementById("address").value = order.address;
        }
    }
    request.send();
}

function submit() {
    if (!checked()) {
        return;
    }
    const urlParams = new URLSearchParams(window.location.search);
    let orderId = urlParams.get("orderId");

    let order = {
        date: document.getElementById("date").value,
        totalPrice: Number(document.getElementById("totalPrice").value),
        status: document.getElementById("status").value,
        customerName: document.getElementById("customerName").value,
        address: document.getElementById("address").value
    };

    let request = initRequest();

    request.open("POST", "orders/" + orderId);

    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            message = "order successfully edited!";
            alert(message);
            allOrders();
        }
    }
    request.send(JSON.stringify(order));
}

function checked() {
    return checkAddress()
        && checkCustomerName();
}

