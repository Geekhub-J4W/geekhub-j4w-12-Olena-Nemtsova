function initRequest() {
    let request;
    if (window.XMLHttpRequest) {
        request = new XMLHttpRequest();
    } else {
        request = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return request;
}

function loadPages() {
    let itemsNames = document.getElementsByClassName("name");
    let items = document.getElementsByClassName("left_element");

    for (let i = 1; i < items.length; i++) {
        items[i].onclick = function () {
            let name = itemsNames[i - 1].innerText.toLowerCase();
            if (name.includes(" ")) {
                name = name.split(" ").reduce((s, c) => s
                    + (c.charAt(0).toUpperCase() + c.slice(1)));
            }

            window.location.replace('/main/' + name);
        }
    }
}

function createTableRow(elements = [], div) {
    let table = document.createElement("table");
    table.className = "paramsTable";
    let row = document.createElement("tr");
    table.appendChild(row);
    div.appendChild(table);
    for (let i = 0; i < elements.length; i++) {
        let col = document.createElement("td");
        col.appendChild(elements[i]);
        row.appendChild(col);
    }
}

function createPages(pagesCount, currentPage, limit, input, functionSearch, role = null) {
    let pagesDiv = document.createElement("div");
    pagesDiv.id = "pages";
    document.getElementById("container").appendChild(pagesDiv);

    for (let i = 1; i <= pagesCount; i++) {
        let page = document.createElement("button");
        pagesDiv.appendChild(page);
        if (i === currentPage) {
            page.style.backgroundColor = "rgb(32, 96, 94)";
        }
        page.innerText = i + "";
        page.onclick = function () {
            if (input === null) {
                functionSearch(limit, currentPage, document.getElementById("chat_select").value);
                return;
            }
            if (role === null) {
                functionSearch(limit, i, input);
            } else {
                functionSearch(role, limit, i, input);
            }
        }
    }
}

function resetErrorById(id) {
    document.getElementById(id).innerHTML = "";
}

function checkFirstName() {
    let regExp = /\b([A-Z][a-z]+)/;
    if (!regExp.test(document.getElementById("firstName").value)) {
        document.getElementById("error_firstName").innerHTML = "Please enter valid first name";
        return false;
    }
    return true;
}

function checkLastName() {
    let regExp = /\b([A-Z][a-z]+)/;
    if (!regExp.test(document.getElementById("lastName").value)) {
        document.getElementById("error_lastName").innerHTML = "Please enter valid last name";
        return false;
    }
    return true;
}

function checkEmail() {
    let regExp = /[a-zA-Z0-9-_.]{3,}@[a-z]{3,}\.[a-z]{3,}/;
    if (!regExp.test(document.getElementById("email").value)) {
        document.getElementById("error_email").innerHTML = "Please enter valid email";
        return false;
    }
    return true;
}

function checkPassword() {
    let regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{6,}$/;
    if (!regExp.test(document.getElementById("password").value)) {
        document.getElementById("error_pass").innerHTML = "Password is not secure";
        return false;
    }
    return true;
}

function checkPasswordConfirm() {
    let password = document.getElementById("password").value;
    let passwordConfirm = document.getElementById("passwordConfirm").value;
    if (password !== passwordConfirm) {
        document.getElementById("error_passConfirm").innerHTML = "Password mismatch";
        return false;
    }
    return true;
}

function googleLogin() {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let request = initRequest();
    request.open("POST", "users/google");
    request.setRequestHeader(header, token);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            window.location.replace("/main/products");
        }
    }
    request.send();
}

function loadProductsPages(currentPage, limit, input) {
    let request = initRequest();
    request.open("GET", "/products/pages/" + limit + "/" + input);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let pagesCount = Number(request.responseText);
            createPages(pagesCount, currentPage, limit, input, searchProducts);
        }
    }
    request.send();
}

function loadDishesPages(currentPage, limit, input) {
    let request = initRequest();
    request.open("GET", "/dishes/pages/" + limit + "/" + input);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let pagesCount = Number(request.responseText);
            createPages(pagesCount, currentPage, limit, input, searchDishes);
        }
    }
    request.send();
}

function cleanContainer() {
    let container_elements = document.getElementsByClassName("container_element");
    let container = document.getElementById("container");
    while (container_elements.length !== 0) {
        container.removeChild(container_elements[container_elements.length - 1]);
    }
    if (document.getElementById("empty") != null) {
        container.removeChild(document.getElementById("empty"));
    }
    if (document.getElementById("pages") !== null) {
        container.removeChild(document.getElementById("pages"));
    }
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


