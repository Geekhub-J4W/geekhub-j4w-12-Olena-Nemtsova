document.getElementById("show").onclick = function () {
    searchProducts(
        document.getElementById("limit_select").value,
        1,
        document.getElementById("search_input").value
    );
}

function loadProducts() {
    searchProducts();
    loadPages();
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
                let addAllergicBtn = document.createElement("button");
                addAllergicBtn.innerText = "Add to allergic";
                addAllergicBtn.onclick = function () {
                    addToAllergic(products[i].id);
                }

                createTableRow([name, calories, addAllergicBtn], container_element);
                if (i === products.length - 1) {
                    loadProductsPages(currentPage, limit, input);
                }
            }
        }
    }
    request.send();
}

function addToAllergic(productId) {
    let request = initRequest();
    request.open("POST", "/allergic");
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let relation = request.responseText;
            if (relation.includes("login")) {
                window.location.replace("/login");
                return;
            }
            alert("Allergic product successfully added!");
        }
    }
    let relation = {
        productId: productId
    };

    request.send(JSON.stringify(relation));
}
