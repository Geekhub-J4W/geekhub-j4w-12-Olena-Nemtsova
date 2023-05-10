let isNewParameters = false;

function loadParameters() {
    loadPages();

    let request = initRequest();
    request.open("GET", "/parameters");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let parameters = request.response;
            if (parameters == null) {
                isNewParameters = true;
                return;
            }
            document.getElementById("age").valueAsNumber = parameters.age;
            document.getElementById("weight").valueAsNumber = parameters.weight;
            document.getElementById("height").valueAsNumber = parameters.height;
            document.getElementById("gender").value = parameters.gender;
            document.getElementById("activity").value = parameters.activityLevel;
            document.getElementById("body").value = parameters.bodyType;
            document.getElementById("aim").value = parameters.aim;
        }
    }
    request.send();
}

function saveParameters() {
    if (!checked()) {
        return;
    }
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let request = initRequest();
    if (isNewParameters) {
        request.open("POST", "/parameters");
    } else {
        request.open("PUT", "/parameters");
    }
    request.setRequestHeader(header, token);

    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            alert("Changes saved!");
        }
    }
    let parameters = {
        age: document.getElementById("age").valueAsNumber,
        weight: document.getElementById("weight").valueAsNumber,
        height: document.getElementById("height").valueAsNumber,
        gender: document.getElementById("gender").value,
        activityLevel: document.getElementById("activity").value,
        bodyType: document.getElementById("body").value,
        aim: document.getElementById("aim").value
    };
    request.send(JSON.stringify(parameters));
}

function checked() {
    return checkAge()
        && checkWeight()
        && checkHeight();
}

function checkAge() {
    let regExp = /^\d+$/;
    let age = document.getElementById("age").value;
    if (!regExp.test(age)) {
        document.getElementById("error_age").innerHTML = "Please enter valid age";
        return false;
    }
    if (Number(age) < 16 || Number(age) > 100) {
        document.getElementById("error_age").innerHTML = "Age must be more than 15 and less than 100";
        return false;
    }
    return true;
}

function checkWeight() {
    let regExp = /^\d+$/;
    let weight = document.getElementById("weight").value;
    if (!regExp.test(weight)) {
        document.getElementById("error_weight").innerHTML = "Please enter valid weight";
        return false;
    }
    if (Number(weight) < 20 || Number(weight) > 250) {
        document.getElementById("error_weight").innerHTML = "Weight must be more than 20 and less than 250";
        return false;
    }
    return true;
}

function checkHeight() {
    let regExp = /^\d+$/;
    let height = document.getElementById("height").value;
    if (!regExp.test(height)) {
        document.getElementById("error_height").innerHTML = "Please enter valid height";
        return false;
    }
    if (Number(height) < 90 || Number(height) > 280) {
        document.getElementById("error_height").innerHTML = "Height must be more than 90 and less than 280";
        return false;
    }
    return true;
}
