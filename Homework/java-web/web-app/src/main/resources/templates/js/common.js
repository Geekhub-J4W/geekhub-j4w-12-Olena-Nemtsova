function removeCookie(cookieName) {
    document.cookie = cookieName + "=;"
        + ";expires=" + new Date(0).toUTCString();
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function initRequest() {
    let request;
    if (window.XMLHttpRequest) {
        request = new XMLHttpRequest();
    } else {
        request = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return request;
}


function checkFirstName() {
    let regExp = /\b([A-Z][a-z]+)/;
    if (!regExp.test(document.getElementById("firstName").value)) {
        let name = document.getElementById("error_firstName");
        name.innerHTML = "Please enter valid first name";
        name.style.color = "red";
        return false;
    }
    return true;
}

function checkLastName() {
    let regExp = /\b([A-Z][a-z]+)/;
    if (!regExp.test(document.getElementById("lastName").value)) {
        let lName = document.getElementById("error_lastName");
        lName.innerHTML = "Please enter valid last name";
        lName.style.color = "red";
        return false;
    }
    return true;
}

function checkName() {
    let regExp = /\b([A-Z][a-z]+)/;
    if (!regExp.test(document.getElementById("name").value)) {
        let lName = document.getElementById("error_name");
        lName.innerHTML = "Please enter valid name";
        lName.style.color = "red";
        return false;
    }
    return true;
}

function checkPrice() {
    let regExp = /^\d+(\.\d{1,2})?$/;
    if (!regExp.test(document.getElementById("price").value)) {
        let lName = document.getElementById("error_price");
        lName.innerHTML = "Please enter valid price";
        lName.style.color = "red";
        return false;
    }
    return true;
}

function checkQuantity() {
    let regExp = /^\d+$/;
    if (!regExp.test(document.getElementById("quantity").value)) {
        let quantity = document.getElementById("error_quantity");
        quantity.innerHTML = "Please enter valid quantity";
        quantity.style.color = "red";
        return false;
    }
    return true;
}

function checkImage() {
    if (!document.getElementById("icon").src.includes("blob")) {
        let img = document.getElementById("error_image");
        img.innerHTML = "Please choose image";
        img.style.color = "red";
        return false;
    }
    return true;
}


function checkEmail() {
    let regExp = /[a-zA-Z0-9-_.]{3,}@[a-z]{3,}\.[a-z]{3,}/;
    if (!regExp.test(document.getElementById("email").value)) {
        let email = document.getElementById("error_email");
        email.innerHTML = "Please enter valid email";
        email.style.color = "red";
        return false;
    }
    return true;
}

function checkPassword() {
    let regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{6,}$/;
    if (!regExp.test(document.getElementById("password").value)) {
        let pass = document.getElementById("error_pass");
        pass.innerHTML = "Please enter valid password";
        pass.style.color = "red";
        return false;
    }
    return true;
}

function checkAddress() {
    if (document.getElementById("address").value === "") {
        let pass = document.getElementById("error_address");
        pass.innerHTML = "Please enter valid address";
        pass.style.color = "red";
        return false;
    }
    return true;
}

function checkCustomerName(){
    let regExp= /^[a-zA-Z]+ [a-zA-Z]+$/;
    if (!regExp.test(document.getElementById("customerName").value)) {
        let email = document.getElementById("error_customerName");
        email.innerHTML = "Please enter valid customerName";
        email.style.color = "red";
        return false;
    }
    return true;
}

function resetErrorById(id) {
    document.getElementById(id).innerHTML = "";
}

function loadCategories() {
    let request = initRequest();

    request.open("GET", "categories");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let categories = request.response;
            for (let i = 0; i < categories.length; i++) {
                let tr = document.createElement("tr");

                let a_td = document.createElement("td");
                let a = document.createElement("a");
                a.onclick = function () {
                    const oneHour = new Date();
                    oneHour.setTime(Date.now() + (60 * 60 * 1000));
                    document.cookie = "categoryId=" + categories[i].id
                        + ";expires=" + oneHour.toUTCString();

                    window.location.replace('/mainUser');
                }
                a.innerHTML = categories[i].name;
                a.style.cursor = "pointer";
                a_td.appendChild(a);

                tr.appendChild(a_td);
                document.getElementById("categories").appendChild(tr);
            }
        }
    }
    request.send();
}

function products() {
    const oneHour = new Date();
    oneHour.setTime(Date.now() + (60 * 60 * 1000));
    document.cookie = "categoryId=-1;expires=" + oneHour.toUTCString();

    window.location.replace('/mainUser');
}

function bucket() {
    const oneHour = new Date();
    oneHour.setTime(Date.now() + (60 * 60 * 1000));
    document.cookie = "categoryId=bucket;expires=" + oneHour.toUTCString();

    window.location.replace('/mainUser');
}

function getNow() {
    let now = new Date(Date.now());
    let day = now.getDate();
    if (day < 10) {
        day = "0" + day;
    }
    let month = now.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    let hours = now.getHours();
    if (hours < 10) {
        hours = "0" + hours;
    }
    let minutes = now.getMinutes();
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    return day + "." + month + "." + now.getFullYear()
        + " " + hours + ":" + minutes;
}

function loadPieces(pieces_div, productId, ordersId) {
    let requestPieces = initRequest();
    requestPieces.open("GET", "orders/quantity/" + productId + "/" + ordersId);
    requestPieces.onreadystatechange = function () {
        if (requestPieces.readyState === 4 && requestPieces.status === 200) {
            let pieces = Number(requestPieces.responseText);
            if (pieces > 1) {
                pieces_div.innerText = pieces + " pieces";
            } else {
                pieces_div.innerText = pieces + " piece";
            }
        }
    }
    requestPieces.send();
}

function personalArea() {
    window.location.replace("/personal");
}
