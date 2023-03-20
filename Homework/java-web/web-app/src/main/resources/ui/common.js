function logIn() {
    let request;
    if (window.XMLHttpRequest) {
        request = new XMLHttpRequest();
    } else {
        request = new ActiveXObject("Microsoft.XMLHTTP");
    }
    let email = document.getElementById("email").value;
    let pass = document.getElementById("pass").value;
    request.open("GET", "users/" + email + "/" + pass);
    request.responseType = "json";
    request.onload = function () {
        if (request.status === 404) {
            alert("Wrong data!");
            window.location.replace("login.html");
        }
    }
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;
            document.cookie = "userId=" + user.id;
            if (user.admin) {
                window.location.replace("/mainAdmin");
            } else {
                document.cookie = "categoryId=" + -1;
                window.location.replace("/mainUser");
            }
        }
    }
    request.send();
}

function logOut() {
    removeCookie("userId");
    removeCookie("categoryId");

    window.location.replace("/");
}

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
