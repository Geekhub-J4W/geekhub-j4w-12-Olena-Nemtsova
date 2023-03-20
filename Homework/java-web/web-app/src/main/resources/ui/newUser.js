function loadUser() {

    let id = getCookie("newUserId");
    if (id !== "-1") {
        document.getElementById("submit").innerHTML = "Edit";
        document.getElementById("legend").innerHTML = "edit user";

        let request = initRequest();
        request.open("GET", "users/" + id);
        request.responseType = "json";
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let user = request.response;

                document.getElementById("name").value = user.firstName;
                document.getElementById("name").readOnly = true;
                document.getElementById("lastName").value = user.lastName;
                document.getElementById("lastName").readOnly = true;
                document.getElementById("email").value = user.email;
                document.getElementById("email").readOnly = true;
                document.getElementById("password").value = user.password;
                document.getElementById("password").readOnly = true;
                document.getElementById("role").value = user.admin;
                document.getElementById("role").disabled = false;
            }
        }
        request.send();
    }
}

function submit() {
    if (checked()) {
        let user = {
            id: getCookie("newUserId"),
            firstName: document.getElementById("name").value,
            lastName: document.getElementById("lastName").value,
            password: document.getElementById("password").value,
            email: document.getElementById("email").value,
            admin: document.getElementById("role").value
        };

        let request = initRequest();
        let id = getCookie("newUserId");
        if (id === "-1") {
            request.open("POST", "users/newUser");
        } else {
            request.open("POST", "users/editUser/" + id);
        }
        request.setRequestHeader("Accept", "application/json");
        request.setRequestHeader("Content-Type", "application/json");

        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {

                let message = "admin successfully added!";
                if (id !== "-1") {
                    message = "user successfully edited!";
                }
                alert(message);
                allUsers();
            }
        }
        let obj = JSON.stringify(user);

        request.send(obj);
    }
}

function checked() {
    let ok = true;
    let regExp = /[a-zA-Z0-9-_.]{3,}@[a-z]{3,}\.[a-z]{3,}/;
    if (!regExp.test(document.getElementById("email").value)) {
        ok = false;
        let email = document.getElementById("error_email");
        email.innerHTML = "Please enter valid email";
        email.style.color = "red";
    }
    regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{6,}$/;
    if (!regExp.test(document.getElementById("password").value)) {
        ok = false;
        let pass = document.getElementById("error_pass");
        pass.innerHTML = "Please enter valid password";
        pass.style.color = "red";
    }
    regExp = /\b([A-Z][a-z]+)/;
    if (!regExp.test(document.getElementById("name").value)) {
        ok = false;
        let name = document.getElementById("error_name");
        name.innerHTML = "Please enter valid name";
        name.style.color = "red";
    }
    if (!regExp.test(document.getElementById("lastName").value)) {
        ok = false;
        let lName = document.getElementById("error_lastName");
        lName.innerHTML = "Please enter valid last name";
        lName.style.color = "red";
    }

    return ok;
}

function resetErrorEmail() {
    document.getElementById("error_email").innerHTML = "";
}

function resetErrorPass() {
    document.getElementById("error_pass").innerHTML = "";
}

function resetErrorName() {
    document.getElementById("error_name").innerHTML = "";
}

function resetErrorLastName() {
    document.getElementById("error_lastName").innerHTML = "";
}

