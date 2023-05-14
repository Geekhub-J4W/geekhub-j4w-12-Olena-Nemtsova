function loadDishes() {
    document.getElementById("show").onclick = function () {
        searchDishes(
            document.getElementById("limit_select").value,
            1,
            document.getElementById("search_input").value
        );
    }

    document.getElementById("addNew").onclick = function () {
        let url = new URL(window.location.protocol + "/" + window.location.host + "/admin/dish");
        url.searchParams.set('dishId', 'new');
        window.location.replace(url);
    }

    loadAdminPages();
    searchDishes();
}

function searchDishes(limit = 10, currentPage = 1, input = "") {
    input = input === "" ? null : input;
    cleanContainer();

    let request = initRequest();
    request.open("GET", "/dishes/" + limit + "/" + currentPage + "/" + input);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let dishes = request.response;
            for (let i = 0; i < dishes.length; i++) {
                let container_element = document.createElement("div");
                container_element.className = "container_element";
                document.getElementById("container").appendChild(container_element);

                let image = document.createElement("img");
                image.src = "data:image/png;base64," + dishes[i].image;

                let name = document.createElement("span");
                name.innerText = dishes[i].name;

                let calories = document.createElement("span");

                let editBtn = document.createElement("button");
                editBtn.innerText = "✏️";
                editBtn.onclick = function () {
                    editDish(dishes[i].id);
                }
                let removeBtn = document.createElement("button");
                removeBtn.innerText = "❌";
                removeBtn.onclick = function () {
                    removeDish(dishes[i].id, limit, currentPage, input);
                }
                let actionsDiv = document.createElement("div");
                actionsDiv.append(editBtn, removeBtn);

                createTableRow([image, name, calories, actionsDiv], container_element);
                loadDishCalories(dishes[i].id, calories);
                if (i === dishes.length - 1) {
                    loadDishesPages(currentPage, limit, input);
                }
            }
        }
    }
    request.send();
}

function editDish(dishId) {
    let url = new URL(window.location.protocol + "/" + window.location.host + "/admin/dish");
    url.searchParams.set('dishId', dishId);
    window.location.replace(url);
}

function removeDish(dishId, limit, currentPage, input) {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let requestDel = initRequest();
    requestDel.open("DELETE", "/dishes/" + dishId);
    requestDel.setRequestHeader(header, token);
    requestDel.onreadystatechange = function () {
        if (requestDel.readyState === 4 && requestDel.status === 200) {
            searchDishes(limit, currentPage, input);
        }
    }
    requestDel.send();
}

function loadDishCalories(dishId, calories) {
    let request = initRequest();
    request.open("GET", "/dishes/calories/" + dishId);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            calories.innerText = request.responseText;
        }
    }
    request.send();
}

function loadDish() {
    loadAdminPages();
    document.getElementById("image").addEventListener("change", handleFile, false);

    const urlParams = new URLSearchParams(window.location.search);
    let dishId = urlParams.get("dishId");
    if (dishId === "new") {
        document.getElementById("addIngredientsDiv").style.display = "none";
        document.getElementById("ingredientsDiv").style.display = "none";
        return;
    }

    document.getElementById("searchProduct").oninput = function () {
        searchProducts();
    }
    searchProducts();
    document.getElementById("headline").innerHTML = "Edit dish";
    document.getElementById("submit").innerHTML = "Edit";

    let request = initRequest();
    request.open("GET", "/dishes/" + dishId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let dish = request.response;

            document.getElementById("icon").src = "data:image/png;base64," + dish.image;
            document.getElementById("name").value = dish.name;
            loadDishIngredients(dish.id);
        }
    }
    request.send();
}

function handleFile() {
    const file = this.files[0];
    document.getElementById("icon").src = URL.createObjectURL(file);
    resetErrorById('error_image');
}

function loadDishIngredients(dishId) {
    loadDishCalories(dishId, document.getElementById("calories"));
    let ingredientsDiv = document.getElementById("ingredients");
    while (ingredientsDiv.lastChild) {
        ingredientsDiv.removeChild(ingredientsDiv.lastChild);
    }
    let request = initRequest();
    request.open("GET", "/products/dish/" + dishId);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let products = request.response;
            if (products.length === 0) {
                let empty = document.createElement("div");
                empty.id = "empty";
                empty.innerText = "Empty";
                document.getElementById("ingredients").appendChild(empty);
                return;
            }
            for (let i = 0; i < products.length; i++) {
                loadQuantity(products[i], dishId);
            }
        }
    }
    request.send();
}

function loadQuantity(product, dishId) {
    let request = initRequest();
    request.open("GET", "/productsDishes/" + product.id + "/" + dishId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let relation = request.response;

            let productDiv = document.createElement("div");
            document.getElementById("ingredients").appendChild(productDiv);
            let name = document.createElement("span");
            name.innerText = product.name;

            let quantity = document.createElement("input");
            quantity.type = "number";
            quantity.valueAsNumber = relation.productQuantity;
            quantity.oninput = function () {
                quantity.style.color = "black";
            }

            let editBtn = document.createElement("button");
            editBtn.innerText = "✏️";
            editBtn.onclick = function () {
                if (!checkQuantity(quantity.value)) {
                    quantity.style.color = "red";
                    return;
                }
                editIngredient(product.id, dishId, quantity.valueAsNumber);
            }
            let removeBtn = document.createElement("button");
            removeBtn.innerText = "❌";
            removeBtn.onclick = function () {
                removeIngredient(product.id, dishId, quantity.valueAsNumber);
            }
            let actionsDiv = document.createElement("div");
            actionsDiv.append(editBtn, removeBtn);

            createTableRow([name, quantity, actionsDiv], productDiv);
        }
    }
    request.send();
}

function submit() {
    if (!checked()) {
        return;
    }
    const urlParams = new URLSearchParams(window.location.search);
    let dishId = urlParams.get("dishId");

    let dish = {
        name: document.getElementById("name").value
    };

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let request = initRequest();
    if (dishId === "new") {
        request.open("POST", "/dishes");
    } else {
        request.open("PUT", "/dishes/" + dishId);
    }
    request.setRequestHeader(header, token);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let dish = request.response;
            if (request.response === null) {
                document.getElementById("error_name").innerText = "Dish with the same name already exists";
                return;
            }
            let requestImg = initRequest();
            requestImg.open("PUT", "/dishes/image/" + dish.id);
            requestImg.setRequestHeader(header, token);

            let formData = new FormData();
            fetch(document.getElementById("icon").src)
                .then(res => res.blob())
                .then(blob => {
                        const file = new File([blob], 'file', {type: blob.type});
                        formData.append("file", file);
                        requestImg.send(formData);
                    }
                )
            let message = dishId === "new" ? "dish successfully added!" : "dish successfully edited!";

            alert(message);
            let url = new URL(window.location.protocol + "/" + window.location.host + "/admin/dish");
            url.searchParams.set('dishId', dish.id);
            window.location.replace(url);
        }
    }
    request.send(JSON.stringify(dish));
}

function checked() {
    return checkImage()
        & checkName();
}

function searchProducts() {
    let div = document.getElementById("product");
    let products = document.getElementsByClassName("line");
    while (products.length !== 0) {
        div.removeChild(products[products.length - 1]);
    }
    let request = initRequest();
    let input = document.getElementById("searchProduct").value;
    input = input === "" ? null : input;
    request.open("GET", "/products/5/1/" + input);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let products = request.response;
            for (let i = 0; i < products.length; i++) {
                let product = document.createElement("div");
                product.className = "line";
                let name = document.createElement("span");
                name.innerText = products[i].name;

                let quantity = document.createElement("input");
                quantity.type = "number";
                quantity.oninput = function () {
                    quantity.style.color = "black";
                }

                let addBtn = document.createElement("button");
                addBtn.innerText = "➕";
                addBtn.onclick = function () {
                    if (!checkQuantity(quantity.value)) {
                        quantity.style.color = "red";
                        return;
                    }
                    addIngredient(products[i].id, quantity.valueAsNumber);
                }
                createTableRow([name, quantity, addBtn], product);

                div.appendChild(product);
            }
        }
    }
    request.send();
}

function editIngredient(productId, dishId, quantity) {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let request = initRequest();
    request.open("PUT", "/productsDishes");
    request.setRequestHeader(header, token);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            if (request.response === "") {
                alert("Ingredient wasn't updated");
                return;
            }
            loadDishIngredients(dishId);
        }
    }
    let relation = {
        productId: productId,
        dishId: dishId,
        productQuantity: quantity
    }
    request.send(JSON.stringify(relation));
}

function removeIngredient(productId, dishId) {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let request = initRequest();
    request.open("DELETE", "/productsDishes/" + productId + "/" + dishId);
    request.setRequestHeader(header, token);

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            loadDishIngredients(dishId);
        }
    }
    request.send();
}

function addIngredient(productId, quantity) {
    const urlParams = new URLSearchParams(window.location.search);
    let dishId = urlParams.get("dishId");

    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let request = initRequest();
    request.open("POST", "/productsDishes");
    request.setRequestHeader(header, token);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            if (request.response === "") {
                alert("Ingredient wasn't added");
                return;
            }
            loadDishIngredients(dishId);
        }
    }
    let relation = {
        productId: productId,
        dishId: Number(dishId),
        productQuantity: quantity
    }
    request.send(JSON.stringify(relation));
}
