function loadDish() {
    const urlParams = new URLSearchParams(window.location.search);
    let dishId = urlParams.get("dishId");

    loadCalories(dishId);
    loadWeight(dishId);
    loadIngredients(dishId);
    loadPages();

    let request = initRequest();
    request.open("GET", "/dishes/" + dishId);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let dish = request.response;
            document.getElementById("headline").innerText = dish.name;
            document.getElementById("image").src = "data:image/png;base64," + dish.image;
        }
    }
    request.send();
}

function loadCalories(dishId) {
    let request = initRequest();
    request.open("GET", "/dishes/calories/" + dishId);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            document.getElementById("calories").innerText = request.responseText;
        }
    }
    request.send();
}

function loadWeight(dishId) {
    let request = initRequest();
    request.open("GET", "/dishes/weight/" + dishId);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            document.getElementById("weight").innerText = request.responseText;
        }
    }
    request.send();
}

function loadIngredients(dishId) {
    let request = initRequest();
    request.open("GET", "/products/dish/" + dishId);
    request.responseType = "json";

    let nameTitle = document.createElement("span");
    nameTitle.innerText = "Name";
    let quantityTitle = document.createElement("span");
    quantityTitle.innerText = "Quantity";
    let caloriesTitle = document.createElement("span");
    caloriesTitle.innerText = "Calories/100g";

    let container_title = document.getElementById("container_title");
    createTableRow([nameTitle, quantityTitle, caloriesTitle], container_title);

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let products = request.response;

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

            let container_element = document.createElement("div");
            container_element.className = "container_element";
            document.getElementById("container").appendChild(container_element);
            let name = document.createElement("span");
            name.innerText = product.name;
            let calories = document.createElement("span");
            calories.innerText = product.calories;
            let quantity = document.createElement("span");
            quantity.innerText = relation.productQuantity;

            createTableRow([name, quantity, calories], container_element);
        }
    }
    request.send();
}
