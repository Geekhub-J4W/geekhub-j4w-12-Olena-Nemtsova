document.getElementById("calculate").onclick = function () {
    if (!parametersExists) {
        document.getElementById("tooltip").style.visibility = "visible";
        return;
    }
    loadUserCalories();
    loadUserDishes();
}

let parametersExists = true;

function loadAll() {
    loadPages();
    let request = initRequest();
    request.open("GET", "/parameters");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let parameters = request.response;
            if (parameters === null) {
                parametersExists = false;
            }
        }
    }
    request.send();
}

function loadUserCalories() {
    loadCaloriesByTypeOfMeal("", document.getElementById("totalCalories"))
    loadCaloriesByTypeOfMeal("BREAKFAST", document.getElementById("breakfastCalories"));
    loadCaloriesByTypeOfMeal("DINNER", document.getElementById("dinnerCalories"));
    loadCaloriesByTypeOfMeal("LUNCH", document.getElementById("lunchCalories"));
    loadCaloriesByTypeOfMeal("SUPPER", document.getElementById("supperCalories"));
}

function loadCaloriesByTypeOfMeal(typeOfMeal, element) {
    let request = initRequest();
    if (typeOfMeal === "") {
        request.open("GET", "/parameters/calories");
    } else {
        request.open("GET", "/parameters/calories/" + typeOfMeal);
    }
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            element.innerText = request.responseText;
        }
    }
    request.send();
}

function loadUserDishes() {
    loadDishesByTypeOfMeal("BREAKFAST", document.getElementById("breakfastDishes"));
    loadDishesByTypeOfMeal("DINNER", document.getElementById("dinnerDishes"));
    loadDishesByTypeOfMeal("LUNCH", document.getElementById("lunchDishes"));
    loadDishesByTypeOfMeal("SUPPER", document.getElementById("supperDishes"));
}

function loadDishesByTypeOfMeal(typeOfMeal, element) {
    while (element.lastElementChild) {
        element.removeChild(element.lastElementChild);
    }

    let request = initRequest();
    request.open("GET", "/dishes/typeOfMeal/" + typeOfMeal);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let dishes = request.response;
            if (dishes == null) {
                return;
            }

            let buttonPrev = document.createElement("button");
            buttonPrev.className = "carousel_arrow";
            buttonPrev.innerText = "❮";

            let buttonNext = document.createElement("button");
            buttonNext.className = "carousel_arrow";
            buttonNext.innerText = "❯";

            let container = document.createElement("ul");
            container.className = "carousel_container";

            buttonPrev.onclick = function () {
                prev(container);
            };
            buttonNext.onclick = function () {
                next(container);
            };

            element.append(buttonPrev, container, buttonNext);

            let carouselElement;
            for (let i = 0; i < dishes.length; i++) {
                if (i % 4 === 0) {
                    carouselElement = document.createElement("li");
                    carouselElement.className = "carousel_element";
                    container.appendChild(carouselElement);
                }
                let dish = document.createElement("div");
                dish.className = "dishBox";

                let img = document.createElement("img");
                img.src = "data:image/png;base64," + dishes[i].image;

                let name = document.createElement("p");
                name.innerText = dishes[i].name;
                name.className = "link";
                let url = new URL(window.location.protocol + "/" + window.location.host + "/main/dish");
                url.searchParams.set('dishId', dishes[i].id);
                name.onclick = function () {
                    window.location.replace(url);
                };

                let calories = document.createElement("p");
                dish.append(img, name, calories);
                loadDishCalories(dishes[i].id, calories);

                carouselElement.appendChild(dish);
            }
        }
    }
    request.send();
}

function next(container) {
    let width = document.querySelector(".carousel_element").clientWidth;
    container.scrollLeft += width;
}

function prev(container) {
    let width = document.querySelector(".carousel_element").clientWidth;
    container.scrollLeft -= width;
}

function loadDishCalories(dishId, element) {
    let request = initRequest();
    request.open("GET", "/dishes/calories/" + dishId);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            element.innerText = "Calories: " + request.responseText;
        }
    }
    request.send();
}
