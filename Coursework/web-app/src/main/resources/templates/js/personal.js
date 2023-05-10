function loadUser() {
    loadPages();

    let request = initRequest();
    request.open("GET", "/users");
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;

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
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let user = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value
    };
    if (document.getElementById("password").value !== "") {
        user.password = document.getElementById("password").value;
    }

    let request = initRequest();
    request.open("PUT", "/users");

    request.setRequestHeader(header, token);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            if (request.response === "") {
                alert("Something went wrong :(");
                return;
            }

            alert("Changes successfully saved");
        }
    }
    request.send(JSON.stringify(user));
}

function checked() {
    return document.getElementById("password").value !== ""
        ? checkFirstName()
        && checkLastName()
        && checkEmail()
        && checkPassword()
        && checkPasswordConfirm()

        : checkFirstName()
        && checkLastName()
        && checkEmail();
}
