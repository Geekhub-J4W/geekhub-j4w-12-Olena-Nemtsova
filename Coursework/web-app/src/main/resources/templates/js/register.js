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
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let request = initRequest();

    request.open("POST", "/users/register");
    request.setRequestHeader(header, token);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            if (request.response === null) {
                document.getElementById("error_email").innerHTML = "User with this email already exists";
                return;
            }
            alert("Register successful!");
            window.location.replace("/main/products");
        }
    }
    request.send(JSON.stringify(user));
}

function checked() {
    return checkFirstName()
        & checkLastName()
        & checkEmail()
        & checkPassword()
        & checkPasswordConfirm();
}

