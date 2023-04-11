function showCustomSelect() {
    document.getElementById('optionsBox').style.display = 'inline';
}

document.addEventListener('click', function (event) {
    if (document.getElementById('sortBox') !== null
        && !document.getElementById('sortBox').contains(event.target)) {
        document.getElementById('optionsBox').style.display = 'none';
    }
});

function loadAll() {
    if (getCookie("categoryId") === "bucket") {
        bucketProducts();
    } else if (getCookie("categoryId") !== undefined) {
        productsByCategory('RATING', getCookie("categoryId"), 5, 1)
    } else {
        allProducts();
    }
    allCategories();
}

function allProducts() {
    document.cookie = "categoryId=" + -1;
    productsByCategory('RATING', -1, 5, 1);
}

function productsByCategory(sortType, categoryId, limit, pageNumber) {
    drawSortBox();
    document.getElementById("customSelect").innerHTML = "Sort by " + sortType.toLowerCase();

    document.getElementById("main").removeChild(document.getElementById("products"));
    if (document.getElementById("pages") !== null) {
        document.getElementById("main").removeChild(document.getElementById("pages"));
    }

    let productsTable = document.createElement("table");
    productsTable.style.borderSpacing = "1em";
    productsTable.style.paddingLeft = "5%";
    productsTable.style.width = "50vw";
    productsTable.id = "products";
    document.getElementById("main").appendChild(productsTable);
    let pagesDiv = document.createElement("div");
    pagesDiv.id = "pages";
    pagesDiv.style.display = "inline-block";
    document.getElementById("main").appendChild(pagesDiv);


    let request = initRequest();
    if (categoryId === -1 && getCookie("categoryId") !== undefined) {
        categoryId = getCookie("categoryId");
    }

    let title_tr = document.createElement("tr");
    productsTable.appendChild(title_tr);
    productsTitle(categoryId, title_tr);


    request.open("GET", "products/" + sortType + "/" + categoryId + "/" + limit + "/" + pageNumber);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let products = request.response;

            for (let i = 0; i < products.length; i++) {
                let tr = document.createElement("tr");

                let image_td = document.createElement("td");
                let image = document.createElement("img");
                image.style.width = "50px";
                image.src = "data:image/png;base64," + products[i].image;
                image_td.appendChild(image);

                let name_td = document.createElement("td");
                let name_p = document.createElement("p");
                name_p.innerText = products[i].name;
                name_p.style.textDecoration = "underline";
                name_p.style.cursor = "pointer";
                let url = new URL(window.location.protocol + "/" + window.location.host + "/product");
                url.searchParams.set('productId', products[i].id);
                name_p.onclick = function () {
                    window.location.replace(url);
                };
                name_td.appendChild(name_p);

                let price_td = document.createElement("td");
                price_td.innerText = products[i].price + " ₴";

                let bucket_td = document.createElement("td");
                let butt = document.createElement("button");
                butt.onclick = function () {
                    addBucketProduct(products[i].id);
                }
                butt.innerHTML = "🧺";

                bucket_td.append(butt);

                if (Number(products[i].quantity) === 0) {
                    tr.style.color = "grey";
                    butt.disabled = true;
                }
                tr.append(image_td, name_td, price_td, bucket_td);
                productsTable.appendChild(tr);

            }

            let requestPages = initRequest();
            requestPages.open("GET", "products/pagesCount/" + categoryId + "/" + limit);
            requestPages.onreadystatechange = function () {
                let pagesCount = Number(requestPages.responseText);

                for (let i = 1; i <= pagesCount; i++) {
                    let page = document.createElement("a");
                    page.style.padding = "8px 16px";
                    page.style.margin = "4px 4px";
                    page.style.border = "1px solid gray";
                    page.innerHTML = i;
                    if (pageNumber === i) {
                        page.style.backgroundColor = "lavender";
                    } else {
                        page.onmouseover = function () {
                            page.style.backgroundColor = "silver";
                        }
                        page.onmouseout = function () {
                            page.style.backgroundColor = "white";
                        }
                    }
                    page.onclick = function () {
                        productsByCategory(sortType, categoryId, limit, i);
                    }
                    document.getElementById("pages").appendChild(page);
                }
            }
            requestPages.send();
        }
    }

    request.send();
}

function productsTitle(categoryId, title_tr) {
    let request = initRequest();

    request.open("GET", "categories/" + categoryId);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let title_td = document.createElement("td");

            let category = request.response;
            if (categoryId !== "-1") {
                title_td.innerText = category.name;
            } else {
                title_td.innerText = "All products";
            }
            title_td.colSpan = 3;
            title_tr.appendChild(title_td);
        }
    }
    request.send();
}

function allCategories() {
    let request = initRequest();

    request.open("GET", "categories");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let categories = request.response;
            for (let i = 0; i < categories.length; i++) {
                let tr = document.createElement("tr");

                let a_td = document.createElement("td");
                let a = document.createElement("a");
                a.onclick = function () {
                    productsByCategory('RATING', categories[i].id, 5, 1);

                    const oneHour = new Date();
                    oneHour.setTime(Date.now() + (60 * 60 * 1000));
                    document.cookie = "categoryId=" + categories[i].id
                        + ";expires=" + oneHour.toUTCString();
                }
                a.innerHTML = categories[i].name;
                a.style.cursor = "pointer";
                a_td.appendChild(a);

                tr.appendChild(a_td);
                document.getElementById("categories").appendChild(tr);
            }
        }
    }
    request.send();
}


function bucketProducts() {
    const oneHour = new Date();
    oneHour.setTime(Date.now() + (60 * 60 * 1000));
    document.cookie = "categoryId=bucket;expires=" + oneHour.toUTCString();

    if (document.getElementById("hr") !== null) {
        document.getElementById("main").removeChild(document.getElementById("hr"));
        document.getElementById("main").removeChild(document.getElementById("sortBox"));
        document.getElementById("main").removeChild(document.getElementById("pages"));
    }

    document.getElementById("main").removeChild(document.getElementById("products"));

    let productsTable = document.createElement("table");
    productsTable.style.borderSpacing = "1em";
    productsTable.id = "products";
    let title_tr = document.createElement("tr");
    let title_td = document.createElement("td");
    title_td.innerText = "Bucket products";
    title_td.colSpan = 3;
    title_tr.appendChild(title_td);

    productsTable.appendChild(title_tr);
    document.getElementById("main").appendChild(productsTable);

    if (getCookie("bucketProducts") !== undefined) {
        let bucketProducts = JSON.parse(getCookie("bucketProducts"));

        for (let i = 0; i < bucketProducts.length; i++) {

            let request = initRequest();
            request.open("GET", "products/" + bucketProducts[i].id);
            request.responseType = "json";

            request.onreadystatechange = function () {
                if (request.readyState === 4 && request.status === 200) {
                    let product = request.response;
                    bucketTotalPrice(product, bucketProducts[i].quantity);
                }
            }
            request.send();
        }
    } else {
        let emptyBucketMessage = document.createElement("h3");
        emptyBucketMessage.style.color = "grey";
        emptyBucketMessage.innerHTML = "empty";
        document.getElementById("products").appendChild(emptyBucketMessage);
        removeCookie("bucketProducts");
    }
}

function addBucketProduct(productId) {
    let products = [];
    if (getCookie("bucketProducts") !== undefined) {
        products = JSON.parse(getCookie("bucketProducts"));
    }
    productId = Number(productId);
    if (products.filter(p => p.id === productId).length !== 0) {
        products.filter(p => p.id === productId).shift().quantity += 1;
    } else {
        let product = {id: productId, quantity: 1};
        products.push(product);
    }

    const oneDay = new Date();
    oneDay.setTime(Date.now() + (24 * 60 * 60 * 1000));
    document.cookie = "bucketProducts=" + JSON.stringify(products) + ";expires=" + oneDay.toUTCString();
    document.location.reload();
}

function deleteOneProductAtBucket(productId) {
    if (getCookie("bucketProducts") !== undefined) {
        let products = JSON.parse(getCookie("bucketProducts"));

        if (products.filter(p => p.id === productId).shift().quantity === 1) {
            deleteAllConcreteProductsAtBucket(productId);
            return;
        }
        products.filter(p => p.id === productId).shift().quantity -= 1;

        const oneDay = new Date();
        oneDay.setTime(Date.now() + (24 * 60 * 60 * 1000));
        document.cookie = "bucketProducts=" + JSON.stringify(products) + ";expires=" + oneDay.toUTCString();
        bucketProducts();
    }
}

function deleteAllConcreteProductsAtBucket(productId) {
    if (getCookie("bucketProducts") !== undefined) {
        let products = JSON.parse(getCookie("bucketProducts"));

        let index = products.indexOf(p => p.id === productId);
        products.splice(index, 1);

        if (products.length === 0) {
            removeCookie("bucketProducts");
            bucketProducts();
            return;
        }
        const oneDay = new Date();
        oneDay.setTime(Date.now() + (24 * 60 * 60 * 1000));
        document.cookie = "bucketProducts=" + JSON.stringify(products) + ";expires=" + oneDay.toUTCString();
        bucketProducts();
    }
}

function bucketTotalPrice(product, quantity) {
    if (document.getElementById("totalPrice") === null) {
        let totalPrice_tr = document.createElement("tr");
        totalPrice_tr.id = "totalPriceTr";

        let totalPrice_td_text = document.createElement("td");
        totalPrice_td_text.innerText = "Total price:";

        let totalPrice_td_val = document.createElement("td");
        totalPrice_td_val.id = "totalPrice";
        totalPrice_td_val.innerText = product.price * quantity;

        let checkButt_td = document.createElement("td");
        let checkButt = document.createElement("button");
        checkButt.onclick = function () {
            window.location.replace("/checkout");
        }
        checkButt.innerHTML = "checkout";
        checkButt.id = "checkout";
        checkButt_td.appendChild(checkButt);
        totalPrice_tr.append(totalPrice_td_text, totalPrice_td_val, checkButt_td);
        document.getElementById("products").appendChild(totalPrice_tr);

    } else {
        let totalPrice = document.getElementById("totalPrice");
        totalPrice.innerText = Number(totalPrice.innerText) + (product.price * quantity);
    }

    let tr = document.createElement("tr");

    let image_td = document.createElement("td");
    let image = document.createElement("img");
    image.style.width = "50px";
    image.src = "data:image/png;base64," + product.image;
    image_td.appendChild(image);

    let name_td = document.createElement("td");
    name_td.innerText = product.name;

    let price_td = document.createElement("td");
    price_td.innerText = product.price+" ₴";

    let bucket_td = document.createElement("td");
    let butt = document.createElement("button");
    butt.onclick = function () {
        deleteAllConcreteProductsAtBucket(product.id);
    }
    butt.innerHTML = "❌";
    bucket_td.appendChild(butt);

    let quantity_td = document.createElement("td");
    let input = document.createElement("input");
    input.type = "number";
    input.style.width = "50px";
    input.valueAsNumber = quantity;
    input.readOnly = true;
    let divArrows = document.createElement("span");
    divArrows.style.marginLeft = "3px";
    let moreArrow = document.createElement("span");
    moreArrow.innerHTML = "🔼";
    moreArrow.style.cursor = "pointer";
    moreArrow.onclick = function () {
        addBucketProduct(product.id);
    }
    let lessArrow = document.createElement("span");
    lessArrow.innerHTML = "🔽";
    lessArrow.style.cursor = "pointer";
    lessArrow.onclick = function () {
        if (input.valueAsNumber === 0) {
            deleteAllConcreteProductsAtBucket(product.id);
        } else {
            deleteOneProductAtBucket(product.id);
        }
    }
    divArrows.append(moreArrow, lessArrow);
    quantity_td.append(input, divArrows);
    tr.append(image_td, name_td, price_td, quantity_td, bucket_td);

    if (Number(product.quantity) < quantity) {
        tr.style.color = "grey";

        let trOut = document.createElement("tr");
        let tdOutMessage = document.createElement("td");
        tdOutMessage.colSpan = 4;
        tdOutMessage.style.color = "red";
        tdOutMessage.innerText = "Product out of stock";
        trOut.appendChild(tdOutMessage);
        document.getElementById("products").insertBefore(trOut, document.getElementById("totalPriceTr"));
        document.getElementById("checkout").disabled = true;
    }
    document.getElementById("products").insertBefore(tr, document.getElementById("totalPriceTr"));
}

function drawSortBox() {
    if (document.getElementById("hr") !== null) {
        return;
    }

    let sortBox = document.createElement("div");
    sortBox.style.textAlign = "left";
    sortBox.style.width = "200px";
    sortBox.style.paddingLeft = "10%";
    sortBox.id = "sortBox";

    let title = document.createElement("p");
    title.innerHTML = "Select products sorting:";

    let customSelect = document.createElement("div");
    customSelect.style.width = "120px";
    customSelect.style.height = "20px";
    customSelect.style.border = "solid gray 1px";
    customSelect.id = "customSelect";
    customSelect.onclick = function () {
        showCustomSelect();
    }

    let options = document.createElement("div");
    options.style.backgroundColor = "paleturquoise";
    options.style.position = "absolute";
    options.style.display = "none";
    options.style.width = "120px";
    options.style.textAlign = "center";
    options.id = "optionsBox";

    let optionName = document.createElement("p");
    optionName.style.cursor = "pointer";
    optionName.innerHTML = "Sort by name";
    optionName.onclick = function () {
        document.getElementById('optionsBox').style.display = 'none';
        productsByCategory('NAME', -1, 5, 1);
    }

    let optionPrice = document.createElement("p");
    optionPrice.style.cursor = "pointer";
    optionPrice.innerHTML = "Sort by price";
    optionPrice.onclick = function () {
        document.getElementById('optionsBox').style.display = 'none';
        productsByCategory('PRICE', -1, 5, 1);
    }

    let optionRating = document.createElement("p");
    optionRating.style.cursor = "pointer";
    optionRating.innerHTML = "Sort by rating";
    optionRating.onclick = function () {
        document.getElementById('optionsBox').style.display = 'none';
        productsByCategory('RATING', -1, 5, 1);
    }

    let optionOrders = document.createElement("p");
    optionOrders.style.cursor = "pointer";
    optionOrders.innerHTML = "Sort by orders";
    optionOrders.onclick = function () {
        document.getElementById('optionsBox').style.display = 'none';
        productsByCategory('ORDERS', -1, 5, 1);
    }

    options.append(optionRating, optionName, optionPrice, optionOrders);
    sortBox.append(title, customSelect, options);
    let hr = document.createElement("hr");
    hr.id = "hr";
    document.getElementById("main").append(sortBox, hr);
}
