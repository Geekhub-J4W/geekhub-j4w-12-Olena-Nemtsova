function loadProduct() {
    loadCategories();

    let id = getCookie("productId");
    if (id !== "-1") {
        document.getElementById("submit").innerHTML = "Edit";
        document.getElementById("legend").innerHTML = "edit product";

        let request = initRequest();
        request.open("GET", "products/" + id);
        request.responseType = "json";
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let product = request.response;

                document.getElementById("name").value = product.name;
                document.getElementById("price").value = product.price;
                document.getElementById("image").value = product.imagePath;
                document.getElementById("category").value = product.categoryId;
                document.getElementById("quantity").value = product.quantity;
            }
        }
        request.send();
    }
}

function loadCategories() {
    let request = initRequest();
    request.open("GET", "categories");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let categories = request.response;

            for (let i = 0; i < categories.length; i++) {
                let option = document.createElement("option");
                option.value = categories[i].id;
                option.innerText = categories[i].name;

                document.getElementById("category").appendChild(option);
            }
        }
    }
    request.send();
}

function submit() {
    if (checked()) {
        let product = {
            id: Number(getCookie("productId")),
            name: document.getElementById("name").value,
            price: Number(document.getElementById("price").value),
            categoryId: Number(document.getElementById("category").value),
            imagePath: document.getElementById("image").value,
            quantity: document.getElementById("quantity").value
        };

        let request = initRequest();
        let id = getCookie("productId");
        if (id === "-1") {
            request.open("POST", "products/newProduct");
        } else {
            request.open("POST", "products/editProduct/" + id);
        }
        request.setRequestHeader("Accept", "application/json");
        request.setRequestHeader("Content-Type", "application/json");

        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {

                let message = "product successfully added!";
                if (id !== "-1") {
                    message = "product successfully edited!";
                }
                alert(message);
                document.location.replace("/mainAdmin");
            }
        }
        let obj = JSON.stringify(product);

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
    regExp = /^\d+(\.\d{1,2})?$/;
    if (!regExp.test(document.getElementById("price").value)) {
        ok = false;
        let price = document.getElementById("error_price");
        price.innerHTML = "Please enter valid price";
        price.style.color = "red";
    }

    let request = new XMLHttpRequest();

    request.open("HEAD", document.getElementById("image").value, false);
    request.send();

    if (request.status === 404 || document.getElementById("image").value.length === 0) {
        ok = false;
        let img = document.getElementById("error_image");
        img.innerHTML = "Please enter valid image path";
        img.style.color = "red";
    }

    if (Number(document.getElementById("quantity").value) < 0) {
        ok = false;
        let quantity = document.getElementById("error_quantity");
        quantity.innerHTML = "Quantity can't be less than zero";
        quantity.style.color = "red";
    }

    return ok;
}

function resetErrorPrice() {
    document.getElementById("error_price").innerHTML = "";
}

function resetErrorName() {
    document.getElementById("error_name").innerHTML = "";
}

function resetErrorImage() {
    document.getElementById("error_image").innerHTML = "";
}

function resetErrorQuantity() {
    document.getElementById("error_quantity").innerText = "";
}

