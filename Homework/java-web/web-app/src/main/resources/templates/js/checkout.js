function load() {
    loadOrderDetails();
    loadCategories();
}

function loadOrderDetails() {
    let request = initRequest();
    request.open("GET", "users/info");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;
            if (user === null) {
                return;
            }
            document.getElementById("firstName").value = user.firstName;
            document.getElementById("lastName").value = user.lastName;
            document.getElementById("email").value = user.email;
        }
    }
    request.send();
}

function submit() {
    if (!checked()) {
        return;
    }

    let request = initRequest();
    request.open("GET", "users/info");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;
            if (user !== null) {
                checkout(false);
            } else {
                let tempUser = {
                    firstName: document.getElementById("firstName").value,
                    lastName: document.getElementById("lastName").value,
                    email: document.getElementById("email").value,
                    password: "Temporary1"
                };
                let tempUserRequest = initRequest();
                tempUserRequest.open("POST", "users/newTemp");
                tempUserRequest.setRequestHeader("Accept", "application/json");
                tempUserRequest.setRequestHeader("Content-Type", "application/json");
                tempUserRequest.responseType = "json";
                tempUserRequest.onreadystatechange = function () {
                    if (tempUserRequest.readyState === 4 && tempUserRequest.status === 200) {
                        let newUser = tempUserRequest.response;
                        checkout(true, newUser.id);
                    }
                }
                tempUserRequest.send(JSON.stringify(tempUser));
            }
        }
    }
    request.send();
}

function checkout(isTempUser, userId = -1) {
    let customerName = document.getElementById("firstName").value
        + " "
        + document.getElementById("lastName").value;

    if (!isTempUser) {
        let order = {
            dateTime: getNow(),
            address: document.getElementById("address").value,
            customerName: customerName
        };
        let request = initRequest();
        request.open("POST", "bucket/checkout");
        request.setRequestHeader("Accept", "application/json");
        request.setRequestHeader("Content-Type", "application/json");
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                alert("Order successfully completed!");
                window.location.replace("/mainUser");
            }
        }
        request.send(JSON.stringify(order));
    } else {
        let order = {
            dateTime: getNow(),
            userId: userId,
            address: document.getElementById("address").value,
            customerName: customerName
        };

        let newOrderRequest = initRequest();
        newOrderRequest.open("POST", "orders/new");
        newOrderRequest.setRequestHeader("Accept", "application/json");
        newOrderRequest.setRequestHeader("Content-Type", "application/json");
        newOrderRequest.responseType = "json";
        newOrderRequest.onreadystatechange = function () {
            if (newOrderRequest.readyState === 4 && newOrderRequest.status === 200) {
                let newOrder = newOrderRequest.response;
                let products = [];
                addOrderProducts(newOrder.id, 0, products);
            }
        }
        console.log(JSON.stringify(order));
        newOrderRequest.send(JSON.stringify(order));
    }
}

function addOrderProducts(orderId, index, products) {
    if (getCookie("bucketProducts") !== undefined) {
        let bucketProducts = JSON.parse(getCookie("bucketProducts"));

        if (index === bucketProducts.length) {
            let request = initRequest();
            request.open("POST", "orders/products/" + orderId);
            request.setRequestHeader("Accept", "application/json");
            request.setRequestHeader("Content-Type", "application/json");
            request.onreadystatechange = function () {
                if (request.readyState === 4 && request.status === 200) {
                    alert("Order successfully completed!");
                    removeCookie("bucketProducts");
                    window.location.replace("/mainUser");
                }
            }
            request.send(JSON.stringify(products));
            return;
        }

        let request = initRequest();
        request.open("GET", "products/" + bucketProducts[index].id);
        request.responseType = "json";
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let product = request.response;
                for (let i = 1; i <= bucketProducts[index].quantity; i++) {
                    products.push(product);
                }
                addOrderProducts(orderId, index + 1, products);
            }
        }
        request.send();
    }
}

function checked() {
    return checkFirstName()
        && checkLastName()
        && checkEmail()
        && checkAddress()
}
