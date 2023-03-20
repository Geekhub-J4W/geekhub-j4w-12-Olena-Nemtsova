function loadCategory() {

    let id = getCookie("categoryId");
    if (id !== "-1") {
        document.getElementById("submit").innerHTML = "Edit";
        document.getElementById("legend").innerHTML = "edit category";

        let request = initRequest();
        request.open("GET", "categories/" + id);
        request.responseType = "json";
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let category = request.response;

                document.getElementById("name").value = category.name;
            }
        }
        request.send();
    }
}

function submit() {
    if (checked()) {
        let category = {
            id: Number(getCookie("categoryId")),
            name: document.getElementById("name").value
        };

        let request = initRequest();
        let id = getCookie("categoryId");
        if (id === "-1") {
            request.open("POST", "categories/newCategory");
        } else {
            request.open("POST", "categories/editCategory/" + id);
        }
        request.setRequestHeader("Accept", "application/json");
        request.setRequestHeader("Content-Type", "application/json");

        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {

                let message = "category successfully added!";
                if (id !== "-1") {
                    message = "category successfully edited!";
                }
                alert(message);
                allCategories();
            }
        }
        let obj = JSON.stringify(category);

        request.send(obj);
    }
}

function checked() {
    let ok = true;

    let regExp = /\b([A-Z][a-z]+)/;
    if (!regExp.test(document.getElementById("name").value)) {
        ok = false;
        let name = document.getElementById("error_name");
        name.innerHTML = "Please enter valid name";
        name.style.color = "red";
    }

    return ok;
}

function resetErrorName() {
    document.getElementById("error_name").innerHTML = "";
}

