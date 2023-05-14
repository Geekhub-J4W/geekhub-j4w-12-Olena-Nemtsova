function loadProducts() {
    document.getElementById("show").onclick = function () {
        searchProducts(
            document.getElementById("limit_select").value,
            1,
            document.getElementById("search_input").value
        );
    }

    document.getElementById("addNew").onclick = function () {
        let url = new URL(window.location.protocol + "/" + window.location.host + "/admin/product");
        url.searchParams.set('productId', 'new');
        window.location.replace(url);
    }

    loadAdminPages();
    searchProducts();
}

function searchProducts(limit = 10, currentPage = 1, input = "") {
    input = input === "" ? null : input;
    cleanContainer();

    let request = initRequest();
    request.open("GET", "/products/" + limit + "/" + currentPage + "/" + input);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let products = request.response;
            for (let i = 0; i < products.length; i++) {
                let container_element = document.createElement("div");
                container_element.className = "container_element";
                document.getElementById("container").appendChild(container_element);

                let name = document.createElement("span");
                name.innerText = products[i].name;
                let calories = document.createElement("span");
                calories.innerText = products[i].calories;

                let editBtn = document.createElement("button");
                editBtn.innerText = "✏️";
                editBtn.onclick = function () {
                    editProduct(products[i].id);
                }
                let removeBtn = document.createElement("button");
                removeBtn.innerText = "❌";
                removeBtn.onclick = function () {
                    removeProduct(products[i].id, limit, currentPage, input);
                }
                let actionsDiv = document.createElement("div");
                actionsDiv.append(editBtn, removeBtn);

                createTableRow([name, calories, actionsDiv], container_element);
                if (i === products.length - 1) {
                    loadProductsPages(currentPage, limit, input);
                }
            }
        }
    }
    request.send();
}

function editProduct(productId) {
    let url = new URL(window.location.protocol + "/" + window.location.host + "/admin/product");
    url.searchParams.set('productId', productId);
    window.location.replace(url);
}

function removeProduct(productId, limit, currentPage, input) {
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    let requestDel = initRequest();
    requestDel.open("DELETE", "/products/" + productId);
    requestDel.setRequestHeader(header, token);
    requestDel.onreadystatechange = function () {
        if (requestDel.readyState === 4 && requestDel.status === 200) {
            searchProducts(limit, currentPage, input);
        }
    }
    requestDel.send();
}

function loadProduct() {
    loadAdminPages();

    const urlParams = new URLSearchParams(window.location.search);
    let productId = urlParams.get("productId");
    if (productId === "new") {
        return;
    }

    document.getElementById("headline").innerHTML = "Edit product";
    document.getElementById("submit").innerHTML = "Edit";

    let request = initRequest();
    request.open("GET", "/products/" + productId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let product = request.response;

            document.getElementById("name").value = product.name;
            document.getElementById("calories").valueAsNumber = product.calories;
        }
    }
    request.send();
}

function submit() {
    if (!checked()) {
        return;
    }
    let token = document.querySelector('meta[name="_csrf"]').content;
    let header = document.querySelector('meta[name="_csrf_header"]').content;

    const urlParams = new URLSearchParams(window.location.search);
    let productId = urlParams.get("productId");

    let product = {
        name: document.getElementById("name").value,
        calories: document.getElementById("calories").valueAsNumber
    };

    let request = initRequest();
    if (productId === "new") {
        request.open("POST", "/products");
    } else {
        request.open("PUT", "/products/" + productId);
    }
    request.setRequestHeader(header, token);
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            if (request.response === "") {
                document.getElementById("error_name").innerText = "Product with the same name already exists";
                return;
            }

            let message = productId === "new" ? "product successfully added!" : "product successfully edited!";
            alert(message);
            window.location.replace("/admin/products");
        }
    }
    request.send(JSON.stringify(product));
}

function checked() {
    return checkName()
        & checkCalories();
}
