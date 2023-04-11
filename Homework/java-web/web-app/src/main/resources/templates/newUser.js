function loadUser() {
    const urlParams = new URLSearchParams(window.location.search);
    let userId = urlParams.get("userId");

    if (userId !== "new") {
        document.getElementById("submit").innerHTML = "Edit";
        document.getElementById("legend").innerHTML = "edit user";

        let request = initRequest();
        request.open("GET", "users/" + userId);
        request.responseType = "json";
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let user = request.response;

                document.getElementById("firstName").value = user.firstName;
                document.getElementById("lastName").value = user.lastName;
                document.getElementById("email").value = user.email;
                document.getElementById("role").value = user.role;
            }
        }
        request.send();
    }
}

function submit() {
    if (!checked()) {
        return;
    }
    let user = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        password: document.getElementById("password").value,
        email: document.getElementById("email").value,
        role: document.getElementById("role").value
    };

    let request = initRequest();
    const urlParams = new URLSearchParams(window.location.search);
    let userId = urlParams.get("userId");
    if (userId === "new") {
        request.open("POST", "users");
    } else {
        request.open("POST", "users/" + userId);
    }
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {

            let message = "user successfully added!";
            if (userId !== "new") {
                message = "user successfully edited!";
            }
            alert(message);
            allUsers();
        }
    }
    request.send(JSON.stringify(user));
}

function checked() {
    return checkFirstName()
        && checkLastName()
        && checkEmail()
        && checkPassword();
}

