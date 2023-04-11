function submit() {
    if (!checked()) {
        return;
    }
    let user = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        password: document.getElementById("password").value,
        email: document.getElementById("email").value
    };

    let request = initRequest();

    request.open("POST", "users/new");

    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            alert("register successful!");
            window.location.replace("/mainUser");
        }
    }
    request.send(JSON.stringify(user));
}

function checked() {
    return checkFirstName()
        && checkLastName()
        && checkEmail()
        && checkPassword()
}

