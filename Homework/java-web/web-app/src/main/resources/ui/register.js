function submit() {
    if (checked()) {
        let user = {
            id: "",
            firstName: document.getElementById("name").value,
            lastName: document.getElementById("lastName").value,
            password: document.getElementById("password").value,
            email: document.getElementById("email").value,
            admin: false
        };

        let request = initRequest();

        request.open("POST", "users/newUser");

        request.setRequestHeader("Accept", "application/json");
        request.setRequestHeader("Content-Type", "application/json");
        request.responseType = "json";

        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let newUser = request.response;
                let message = "register successful!";
                document.cookie = "userId=" + newUser.id;
                document.cookie = "categoryId=" + -1;
                alert(message);
                window.location.replace("/mainUser");
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
