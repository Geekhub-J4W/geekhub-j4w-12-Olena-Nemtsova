function loadProducts() {
    allergicProducts();
    loadPages();
}

function allergicProducts() {
    let request = initRequest();
    request.open("GET", "/products/allergic");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let products = request.response;
            cleanContainer();

            if (products.length === 0) {
                document.getElementById("container_title").style.visibility = "collapse";
                let empty = document.createElement("div");
                empty.id = "empty";
                empty.innerText = "Empty";
                document.getElementById("container").appendChild(empty);
                return;
            }

            for (let i = 0; i < products.length; i++) {
                let container_element = document.createElement("div");
                container_element.className = "container_element";
                document.getElementById("container").appendChild(container_element);

                let name = document.createElement("span");
                name.innerText = products[i].name;
                let calories = document.createElement("span");
                calories.innerText = products[i].calories;
                let delAllergicBtn = document.createElement("button");
                delAllergicBtn.innerText = "Delete allergic ❌";
                delAllergicBtn.onclick = function () {
                    deleteAllergic(products[i].id);
                }

                createTableRow([name, calories, delAllergicBtn], container_element);
            }
        }
    }
    request.send();
}

function deleteAllergic(productId) {
    let request = initRequest();
    request.open("DELETE", "/allergic/" + productId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            allergicProducts();
        }
    }
    request.send();
}
