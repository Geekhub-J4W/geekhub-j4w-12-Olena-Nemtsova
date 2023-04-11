function load() {
    personalData();
}

function personalData() {
    document.getElementById("data").removeChild(document.getElementById("dataTable"));
    let dataTable = document.createElement("table");
    dataTable.id = "dataTable";
    let title = document.createElement("h3");
    title.innerHTML = "Personal data:";
    dataTable.appendChild(title);

    let request = initRequest();
    request.open("GET", "users/info");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;

            let firstName_tr = document.createElement("tr");
            let firstName_td = document.createElement("td");
            let firstName_title = document.createElement("p");
            firstName_title.innerHTML = "First name:";
            let firstName_value = document.createElement("p");
            firstName_value.innerHTML = user.firstName;
            firstName_td.append(firstName_title, firstName_value);
            firstName_tr.append(firstName_td);

            let lastName_tr = document.createElement("tr");
            let lastName_td = document.createElement("td");
            let lastName_title = document.createElement("p");
            lastName_title.innerHTML = "Last name:";
            let lastName_value = document.createElement("p");
            lastName_value.innerHTML = user.lastName;
            lastName_td.append(lastName_title, lastName_value);
            lastName_tr.append(lastName_td);

            let email_tr = document.createElement("tr");
            let email_td = document.createElement("td");
            let email_title = document.createElement("p");
            email_title.innerHTML = "Email:";
            let email_value = document.createElement("p");
            email_value.innerHTML = user.email;
            email_td.append(email_title, email_value);
            email_tr.append(email_td);

            let edit_tr = document.createElement("tr");
            let edit_td = document.createElement("td");
            let edit_button = document.createElement("button");
            edit_button.innerHTML = "Edit";
            edit_button.onclick = function () {
                showEditPersonData();
            }
            edit_td.appendChild(edit_button);
            edit_tr.appendChild(edit_td);

            dataTable.append(firstName_tr, lastName_tr, email_tr, edit_tr);
            document.getElementById("data").appendChild(dataTable);
        }
    }
    request.send();
}

function showEditPersonData() {
    document.getElementById("data").removeChild(document.getElementById("dataTable"));
    let dataTable = document.createElement("table");
    dataTable.id = "dataTable";
    let title = document.createElement("h3");
    title.innerHTML = "Personal data:";
    dataTable.appendChild(title);

    let request = initRequest();
    request.open("GET", "users/info");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;

            let firstName_tr = document.createElement("tr");
            let firstName_td = document.createElement("td");
            let firstName_title = document.createElement("p");
            firstName_title.innerHTML = "First name:";
            let firstName_value = document.createElement("input");
            firstName_value.value = user.firstName;
            firstName_value.id = "firstName";
            firstName_td.append(firstName_title, firstName_value);
            firstName_tr.append(firstName_td);

            let lastName_tr = document.createElement("tr");
            let lastName_td = document.createElement("td");
            let lastName_title = document.createElement("p");
            lastName_title.innerHTML = "Last name:";
            let lastName_value = document.createElement("input");
            lastName_value.value = user.lastName;
            lastName_value.id = "lastName";
            lastName_td.append(lastName_title, lastName_value);
            lastName_tr.append(lastName_td);

            let email_tr = document.createElement("tr");
            let email_td = document.createElement("td");
            let email_title = document.createElement("p");
            email_title.innerHTML = "Email:";
            let email_value = document.createElement("input");
            email_value.value = user.email;
            email_value.id = "email";
            email_td.append(email_title, email_value);
            email_tr.append(email_td);

            let pass_tr = document.createElement("tr");
            let pass_td = document.createElement("td");
            let pass_title = document.createElement("p");
            pass_title.innerHTML = "Password:";
            let pass_value = document.createElement("input");
            pass_value.id = "password";
            pass_td.append(pass_title, pass_value);
            pass_tr.append(pass_td);

            let save_tr = document.createElement("tr");
            let save_td = document.createElement("td");
            let save_button = document.createElement("button");
            save_button.innerHTML = "Save";
            save_button.onclick = function () {
                saveChanges();
            }
            save_td.appendChild(save_button);
            save_tr.appendChild(save_td);

            dataTable.append(firstName_tr, lastName_tr, email_tr, pass_tr, save_tr);
            document.getElementById("data").appendChild(dataTable);
        }
    }
    request.send();
}

function saveChanges() {
    if (checked()) {
        let user = {
            firstName: document.getElementById("firstName").value,
            lastName: document.getElementById("lastName").value,
            password: document.getElementById("password").value,
            email: document.getElementById("email").value
        };

        let request = initRequest();
        request.open("POST", "users/editUser");

        request.setRequestHeader("Accept", "application/json");
        request.setRequestHeader("Content-Type", "application/json");

        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                alert("Changes saved!");
                personalData();
            }
        }
        let obj = JSON.stringify(user);

        request.send(obj);
    }
}

function checked() {
    let ok = true;
    let regExp = /[a-zA-Z0-9-_.]{3,}@[a-z]{3,}\.[a-z]{3,}/;
    if (!regExp.test(document.getElementById("email").value)) {
        ok = false;
        alert("Please enter valid email");
    }
    regExp = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{6,}$/;
    if (!regExp.test(document.getElementById("password").value)) {
        ok = false;
        alert("Please enter valid password");
    }
    regExp = /\b([A-Z][a-z]+)/;
    if (!regExp.test(document.getElementById("firstName").value)) {
        ok = false;
        alert("Please enter valid first name");
    }
    if (!regExp.test(document.getElementById("lastName").value)) {
        ok = false;
        alert("Please enter valid last name");
    }
    return ok;
}

function personalOrders() {
    document.getElementById("data").removeChild(document.getElementById("dataTable"));
    let ordersTable = document.createElement("table");
    ordersTable.id = "dataTable";
    ordersTable.style.border =
        ordersTable.style.width = "55vw";
    let title = document.createElement("h3");
    title.innerHTML = "My orders:";
    ordersTable.appendChild(title);

    let tr_title = document.createElement('tr');
    let th1 = document.createElement('th');
    th1.innerText = "Date";
    let th2 = document.createElement('th');
    th2.innerText = "Total price";
    let th3 = document.createElement('th');
    th3.innerText = "Status";
    let th4 = document.createElement('th');
    th4.innerText = "Address";
    let th5 = document.createElement('th');
    th5.innerText = "Customer";
    tr_title.append(th1, th2, th3, th4, th5);
    ordersTable.appendChild(tr_title);
    document.getElementById("data").appendChild(ordersTable);

    let request = initRequest();
    request.open("GET", "orders/user");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let orders = request.response;

            for (let i = 0; i < orders.length; i++) {
                let tr = document.createElement("tr");

                let date_td = document.createElement("td");
                date_td.innerText = orders[i].dateTime;

                let totalPrice_td = document.createElement("td");
                totalPrice_td.innerText = orders[i].totalPrice;

                let status_td = document.createElement("td");
                status_td.innerText = orders[i].status;

                let address_td = document.createElement("td");
                address_td.innerText = orders[i].address;

                let customer_td = document.createElement("td");
                customer_td.innerText = orders[i].customerName;

                tr.append(date_td, totalPrice_td, status_td, address_td, customer_td);

                let productsRequest = initRequest();
                productsRequest.open("GET", "orders/products/" + orders[i].id);
                productsRequest.responseType = "json";
                productsRequest.onreadystatechange = function () {
                    if (productsRequest.readyState === 4 && productsRequest.status === 200) {
                        let products = productsRequest.response;

                        let tr_details = document.createElement("tr");
                        let td_details = document.createElement("td");
                        td_details.colSpan = 5;
                        let details = document.createElement("details");
                        let summary = document.createElement("summary");
                        summary.innerHTML = "View details";
                        summary.style.color = "blue";
                        details.appendChild(summary);

                        products = [...new Map(products.map(v => [v.id, v])).values()];
                        for (let j = 0; j < products.length; j++) {

                            let detailsProducts = document.createElement("div");
                            detailsProducts.style.width = "55vw";
                            detailsProducts.style.display = "flex";
                            detailsProducts.style.paddingTop = "3vh";
                            detailsProducts.style.paddingBottom = "5vh";
                            detailsProducts.style.backgroundColor = "paleturquoise";

                            let image_div = document.createElement("div");
                            image_div.style.width = "10vw";
                            let image = document.createElement("img");
                            image.style.width = "50px";
                            image.src = "data:image/png;base64," + products[j].image;
                            image_div.appendChild(image);

                            let name_div = document.createElement("div");
                            name_div.innerText = products[j].name;
                            name_div.style.width = "10vw";

                            let price_div = document.createElement("div");
                            price_div.innerText = products[j].price+" ₴";
                            price_div.style.width = "10vw";

                            let pieces_div = document.createElement("div");
                            pieces_div.style.width = "10vw";
                            loadPieces(pieces_div, products[j].id, orders[i].id);

                            let reviewDiv = document.createElement("div");
                            reviewDiv.style.width = "10vw";
                            initReviewDiv(reviewDiv, products[j].id, orders[i].id);

                            detailsProducts.append(image_div, name_div, price_div, pieces_div, reviewDiv);
                            details.appendChild(detailsProducts);
                        }
                        td_details.appendChild(details);
                        tr_details.appendChild(td_details);
                        document.getElementById("dataTable").appendChild(tr);
                        document.getElementById("dataTable").appendChild(tr_details);
                    }
                }
                productsRequest.send();
            }
        }
    }
    request.send();
}


function initReviewDiv(reviewDiv, productId, orderId) {
    let request = initRequest();
    request.open("GET", "reviews/" + productId + "/" + orderId);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let review = request.response;

            let reviewBtn = document.createElement("button");
            reviewDiv.appendChild(reviewBtn);
            if (review === null) {
                reviewBtn.innerHTML = "Add review";
            } else {
                reviewBtn.innerHTML = "Edit review";
            }

            reviewBtn.onclick = function () {
                let reviewChangeDiv = document.getElementById("reviewChangeDiv");
                if (reviewChangeDiv !== null) {
                    reviewChangeDiv.parentNode.removeChild(reviewChangeDiv);
                }
                reviewChangeDiv = document.createElement("div");
                reviewChangeDiv.id = "reviewChangeDiv";

                let ratingDiv = document.createElement("div");
                ratingDiv.style.marginTop = "2vh";

                let rating = document.createElement("input");
                rating.value = "1";
                ratingDiv.appendChild(rating);
                rating.type = "hidden";
                for (let i = 1; i <= 5; i++) {
                    let star = document.createElement("span");
                    star.innerHTML = "★";
                    if (review !== null && i <= review.rating || i === 1) {
                        star.style.color = "yellow";
                    }
                    star.onclick = function () {
                        rating.value = i;
                        for (let j = 1; j < ratingDiv.children.length; j++) {
                            if (j <= i) {
                                ratingDiv.children[j].style.color = "yellow";
                            } else {
                                ratingDiv.children[j].style.color = "black";
                            }
                        }
                    }
                    ratingDiv.appendChild(star);
                }

                let text = document.createElement("input");
                text.style.marginTop = "2vh";
                text.type = "text";
                if (review !== null) {
                    text.value = review.text;
                }

                let saveBtn = document.createElement("button");
                saveBtn.style.marginTop = "2vh";
                saveBtn.innerHTML = "Save";
                saveBtn.onclick = function () {
                    let newReview = {
                        id: -1,
                        dateTime: getNow(),
                        text: text.value,
                        rating: rating.value,
                        productId: productId,
                        orderId: orderId
                    };

                    let saveRequest = initRequest();
                    if (review !== null) {
                        saveRequest.open("POST", "reviews/" + review.id);
                    } else {
                        saveRequest.open("POST", "reviews");
                    }
                    saveRequest.setRequestHeader("Accept", "application/json");
                    saveRequest.setRequestHeader("Content-Type", "application/json");

                    saveRequest.onreadystatechange = function () {
                        if (saveRequest.readyState === 4 && saveRequest.status === 200) {
                            personalOrders();
                        }
                    }
                    let obj = JSON.stringify(newReview);
                    saveRequest.send(obj);
                }

                reviewChangeDiv.append(ratingDiv, text, saveBtn);
                reviewDiv.append(reviewChangeDiv);
            }
        }
    }

    request.send();
}

function personalFavorites() {
    document.getElementById("data").removeChild(document.getElementById("dataTable"));
    let dataTable = document.createElement("table");
    dataTable.id = "dataTable";
    dataTable.style.width = "45vw";
    let title = document.createElement("h3");
    title.innerHTML = "Favorites:";
    dataTable.appendChild(title);
    document.getElementById("data").appendChild(dataTable);

    let request = initRequest();
    request.open("GET", "favorites");
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let products = request.response;
            if (products.length === 0) {
                let emptyMessage = document.createElement("h3");
                emptyMessage.style.color = "grey";
                emptyMessage.innerHTML = "empty";
                dataTable.appendChild(emptyMessage);
                return;
            }
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
                price_td.innerText = products[i].price+" ₴";

                let bucket_td = document.createElement("td");
                let butt = document.createElement("button");
                butt.onclick = function () {
                    addBucketProduct(products[i].id);
                }
                butt.innerHTML = "🧺";

                let butt2 = document.createElement("button");
                butt2.onclick = function () {
                    deleteFavorite(products[i].id);
                }
                butt2.innerHTML = "❌";
                butt2.style.marginLeft = "1vw";

                bucket_td.append(butt, butt2);

                if (Number(products[i].quantity) === 0) {
                    tr.style.color = "grey";
                    butt.disabled = true;
                }
                tr.append(image_td, name_td, price_td, bucket_td);
                dataTable.appendChild(tr);
            }
        }
    }
    request.send();
}

function deleteFavorite(productId) {
    let requestDelete = initRequest();
    requestDelete.open("DELETE", "favorites/" + productId);
    requestDelete.onreadystatechange = function () {
        if (requestDelete.readyState === 4 && requestDelete.status === 200) {
            personalFavorites();
        }
    }
    requestDelete.send();
}
