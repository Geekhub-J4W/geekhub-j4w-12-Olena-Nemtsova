function loadAdminPages() {
    let itemsNames = document.getElementsByClassName("name");
    let items = document.getElementsByClassName("left_element");

    for (let i = 1; i < items.length; i++) {
        items[i].onclick = function () {
            let name = itemsNames[i - 1].innerText.toLowerCase();
            if (name.includes(" ")) {
                name = name.split(" ").reduce((s, c) => s
                    + (c.charAt(0).toUpperCase() + c.slice(1)));
            }

            window.location.replace('/admin/' + name);
        }
    }
}

function checkName() {
    let regExp = /\b([A-Z][a-z]+)/;
    let name = document.getElementById("name").value;
    if (!regExp.test(name)) {
        document.getElementById("error_name").innerHTML = "Please enter valid name";
        return false;
    }
    if (name.length > 50 || name.length < 2) {
        document.getElementById("error_name").innerHTML = "Wrong name length";
        return false;
    }
    return true;
}

function checkCalories() {
    let regExp = /^\d+$/;
    let calories = document.getElementById("calories").value;
    if (!regExp.test(calories)) {
        document.getElementById("error_calories").innerHTML = "Please enter valid calories";
        return false;
    }
    if (Number(calories) < 0) {
        document.getElementById("error_calories").innerHTML = "Calories can't be less than 0";
        return false;
    }
    if (Number(calories) > 2000) {
        document.getElementById("error_calories").innerHTML = "Calories can't be more than 2000";
        return false;
    }
    return true;
}

function checkImage() {
    let imageData = document.getElementById("icon").src;
    if (!imageData.includes("blob") && !imageData.includes("data:image/png;")) {
        document.getElementById("error_image").innerHTML = "Please choose image";
        return false;
    }
    return true;
}

function checkQuantity(quantity) {
    let regExp = /^\d+$/;
    if (!regExp.test(quantity)) {
        alert("Quantity can't be less than 0");
        return false;
    }
    if (Number(quantity) > 1000) {
        alert("Quantity can't be more than 1000");
        return false;
    }
    return true;
}
