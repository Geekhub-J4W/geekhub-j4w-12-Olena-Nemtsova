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
            document.getElementById("saveToFileBtn").style.display = "inline-block";
            dishesData[dishesData.length] = {
                typeOfMeal: typeOfMeal + " dishes variants",
                dishes: dishes
            };

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
                loadDishCalories(dishes[i].id, calories, i, typeOfMeal);

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

function loadDishCalories(dishId, element, i, typeOfMeal) {
    let request = initRequest();
    request.open("GET", "/dishes/calories/" + dishId);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            element.innerText = "Calories: " + request.responseText;

            dishesData.find(el => el.typeOfMeal.includes(typeOfMeal))
                .dishes[i].calories = request.responseText;
        }
    }
    request.send();
}

let dishesData = [];

function saveFile() {
    const doc = new docx.Document({
        styles: {
            paragraphStyles: [
                {
                    id: "tableTitle1",
                    name: "TableTitle1",
                    basedOn: "Normal",
                    next: "Normal",
                    run: {
                        size: 26,
                        bold: true,
                        font: "Rockwell",
                        color: "F2FFF9",
                        underline: {
                            type: docx.UnderlineType.SINGLE,
                        }
                    },
                    paragraph: {
                        alignment: docx.AlignmentType.CENTER,
                        spacing: {
                            line: 300,
                        }
                    }
                },
                {
                    id: "tableTitle2",
                    name: "TableTitle2",
                    basedOn: "Normal",
                    next: "Normal",
                    run: {
                        size: 26,
                        bold: true,
                        font: "Rockwell",
                        color: "F4FFF9",
                    },
                    paragraph: {
                        alignment: docx.AlignmentType.CENTER,
                        spacing: {
                            line: 280,
                        }
                    }
                },
                {
                    id: "title",
                    name: "Title",
                    basedOn: "Normal",
                    next: "Normal",
                    run: {
                        size: 36,
                        bold: true,
                        font: "Rockwell",
                        color: "22A159",
                        underline: {
                            type: docx.UnderlineType.SINGLE,
                        }
                    },
                    paragraph: {
                        alignment: docx.AlignmentType.CENTER,
                        spacing: {
                            line: 500,
                        }
                    }
                },
                {
                    id: "common",
                    name: "Common",
                    basedOn: "Normal",
                    next: "Normal",
                    run: {
                        size: 26,
                        font: "Rockwell",
                        color: "03682E"
                    },
                    paragraph: {
                        spacing: {
                            line: 300,
                        },
                        indent: {
                            left: 400,
                        },
                    }
                },
                {
                    id: "tableRow",
                    name: "TableRow",
                    basedOn: "Normal",
                    next: "Normal",
                    run: {
                        size: 24,
                        font: "Rockwell",
                        color: "034B22"
                    },
                    paragraph: {
                        spacing: {
                            line: 300,
                        },
                        indent: {
                            left: 100,
                        },
                    }
                }
            ]
        }
    });

    const title = new docx.Paragraph({
        text: "Own diet",
        style: "title"
    });
    const totalCalories = new docx.Paragraph({
        text: "Total calories: " + document.getElementById("totalCalories").innerText + "cal",
        style: "common",
        spacing: {
            after: 300,
        }
    });
    const breakfastCalories = new docx.Paragraph({
        text: "Breakfast calories: " + document.getElementById("breakfastCalories").innerText + "cal",
        style: "common"
    });
    const dinnerCalories = new docx.Paragraph({
        text: "Dinner calories: " + document.getElementById("dinnerCalories").innerText + "cal",
        style: "common"
    });
    const lunchCalories = new docx.Paragraph({
        text: "Lunch calories: " + document.getElementById("lunchCalories").innerText + "cal",
        style: "common"
    });
    const supperCalories = new docx.Paragraph({
        text: "Supper calories: " + document.getElementById("supperCalories").innerText + "cal",
        style: "common",
        spacing: {
            after: 300,
        }
    });

    dishesData.sort(function (a, b) {
        if (a.typeOfMeal < b.typeOfMeal) {
            return -1;
        }
        return 1;
    });

    let children = [];

    for (let i = 0; i < dishesData.length; i++) {
        let rows = dishesData[i].dishes.map(dish =>
            new docx.TableRow({
                children: [
                    new docx.TableCell({
                        children: [new docx.Paragraph({
                            text: dish.name,
                            style: "tableRow"
                        })],
                    }),
                    new docx.TableCell({
                        children: [new docx.Paragraph({
                            text: dish.calories,
                            style: "tableRow"
                        })],
                    }),
                ]
            })
        );
        rows.unshift(
            new docx.TableRow({
                children: [
                    new docx.TableCell({
                        children: [new docx.Paragraph({
                            text: dishesData[i].typeOfMeal,
                            style: "tableTitle1"
                        })],
                        columnSpan: 2,
                        shading: {
                            fill: "22A159"
                        }
                    })
                ]
            }),
            new docx.TableRow({
                children: [
                    new docx.TableCell({
                        children: [new docx.Paragraph({
                            text: "Dish",
                            style: "tableTitle2"
                        })],
                        shading: {
                            fill: "22A159"
                        }
                    }),
                    new docx.TableCell({
                        children: [new docx.Paragraph({
                            text: "Calories",
                            style: "tableTitle2"
                        })],
                        shading: {
                            fill: "22A159"
                        }
                    })
                ]
            })
        );

        children[i] = new docx.Table({
            alignment: docx.AlignmentType.CENTER,
            rows: rows,
            width: {
                size: 8640,
                type: docx.WidthType.DXA
            },
            margins: {
                top: 100,
                bottom: 100
            }
        });
    }

    children.unshift(title, totalCalories, breakfastCalories, dinnerCalories, lunchCalories, supperCalories);
    doc.addSection({
        children: children
    });

    docx.Packer.toBlob(doc).then(blob => {
        saveAs(blob, "ownDiet.docx");
    });
}
