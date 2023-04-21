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
    if (!regExp.test(document.getElementById("name").value)) {
        document.getElementById("error_name").innerHTML = "Please enter valid name";
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
    return true;
}

function checkImage() {
    if (!document.getElementById("icon").src.includes("blob")) {
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
    return true;
}
