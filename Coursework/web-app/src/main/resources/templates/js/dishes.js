document.getElementById("show").onclick = function () {
    searchDishes(
        document.getElementById("limit_select").value,
        1,
        document.getElementById("search_input").value
    );
}

function loadDishes() {
    searchDishes();
    loadPages();
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

                loadDishCalories(dishes[i], container_element);
                if (i === dishes.length - 1) {
                    loadDishesPages(currentPage, limit, input);
                }
            }
        }
    }
    request.send();
}

function loadDishCalories(dish, container_element) {
    let request = initRequest();
    request.open("GET", "/dishes/calories/" + dish.id);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let image = document.createElement("img");
            image.src = "data:image/png;base64," + dish.image;

            let name = document.createElement("span");
            name.innerText = dish.name;
            name.className = "link";
            let url = new URL(window.location.protocol + "/" + window.location.host + "/main/dish");
            url.searchParams.set('dishId', dish.id);
            name.onclick = function () {
                window.location.replace(url);
            };

            let calories = document.createElement("span");
            calories.innerText = request.responseText;

            createTableRow([image, name, calories], container_element);
        }
    }
    request.send();
}
