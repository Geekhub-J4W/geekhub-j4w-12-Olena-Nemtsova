function loadAll() {
    let pageId = getCookie("pageId");

    if (pageId === undefined || pageId === "products") {
        allProducts();
    } else if (pageId === "categories") {
        allCategories();
    } else if (pageId === "users") {
        allUsersById();
    } else if (pageId === "orders") {
        allOrders();
    }
    removeCookie("pageId");
}

function allProducts() {
    resetElements();

    let addButton = document.createElement("button");
    addButton.id = "add";
    addButton.innerHTML = "Add new product";
    addButton.onclick = function () {
        let url = new URL(window.location.protocol + "/" + window.location.host + "/newProduct");
        url.searchParams.set('productId', "new");
        window.location.replace(url);
    }
    document.getElementById("main").insertBefore(addButton, document.getElementById("hr"));
    addSearchInput();

    searchProducts();
}

function addSearchInput() {
    let inputDiv = document.createElement("div");
    inputDiv.style.display = "inline-block";
    inputDiv.id = "search";

    let title = document.createElement("span");
    title.style.marginRight = "10px";
    title.innerHTML = "Search by name:";

    let input = document.createElement("input");
    input.type = "text";
    input.oninput = function () {
        searchProducts(input.value);
    }
    inputDiv.append(title, input);
    document.getElementById("main").appendChild(inputDiv);
}

function addSearchOrdersSelect() {
    let inputDiv = document.createElement("div");
    inputDiv.style.display = "inline-block";
    inputDiv.id = "search";

    let title = document.createElement("span");
    title.style.marginRight = "10px";
    title.innerHTML = "Search by status:";

    let select = document.createElement("select");
    let statuses = ["ALL", "PENDING", "SHIPPED", "COMPLETED"];
    for (let i = 0; i < statuses.length; i++) {
        let option = document.createElement("option");
        option.innerText = statuses[i];
        option.value = statuses[i];
        if (i === 0) {
            option.selected = true;
        }
        select.appendChild(option);
    }

    let button = document.createElement("button");
    button.innerText = "Show";
    button.onclick = function () {
        searchOrders(select.value);
    }
    inputDiv.append(title, select, button);
    document.getElementById("main").appendChild(inputDiv);
}

function searchProducts(input = "") {
    document.getElementById("main").removeChild(document.getElementById("dataTable"));

    let productsTable = document.createElement("table");
    productsTable.style.borderSpacing = "1em";
    productsTable.style.marginLeft = "5%";
    productsTable.id = "dataTable";
    let trTitle = document.createElement('tr');
    let th1 = document.createElement('th');
    th1.innerText = "Image";
    let th2 = document.createElement('th');
    th2.innerText = "Name";
    let th3 = document.createElement('th');
    th3.innerText = "Price";
    let th4 = document.createElement('th');
    th4.innerText = "Category";
    let th5 = document.createElement('th');
    th5.innerText = "Quantity in stock";
    let th6 = document.createElement('th');
    th6.innerText = "Actions";
    trTitle.append(th1, th2, th3, th4, th5, th6);

    productsTable.appendChild(trTitle);

    document.getElementById("main").appendChild(productsTable);

    let request = initRequest();
    if (input !== "") {
        request.open("GET", "products/search/" + input);
    } else {
        request.open("GET", "products");
    }
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
                name_td.innerText = products[i].name;

                let price_td = document.createElement("td");
                price_td.innerText = products[i].price + " ₴";

                let category_td = document.createElement("td");
                let requestCategory = initRequest();
                requestCategory.open("GET", "categories/" + products[i].categoryId);
                requestCategory.responseType = "json";
                requestCategory.onreadystatechange = function () {
                    if (requestCategory.readyState === 4 && requestCategory.status === 200) {
                        let category = requestCategory.response;
                        category_td.innerText = category.name;
                    }
                }
                requestCategory.send();

                let quantity_td = document.createElement("td");
                quantity_td.innerText = products[i].quantity;

                let action_td = document.createElement("td");
                let edit = document.createElement("button");
                edit.onclick = function () {
                    let url = new URL(window.location.protocol + "/" + window.location.host + "/newProduct");
                    url.searchParams.set('productId', products[i].id);
                    window.location.replace(url);
                }
                edit.innerHTML = "✏️";
                let del = document.createElement("button");
                del.style.marginTop = "5px";
                del.onclick = function () {
                    let requestDel = initRequest();
                    requestDel.open("DELETE", "products/" + products[i].id);
                    requestDel.onreadystatechange = function () {
                        if (requestDel.readyState === 4 && requestDel.status === 200) {
                            allProducts();
                        }
                    }
                    requestDel.send();
                }
                del.innerHTML = "❌";
                action_td.append(edit, document.createElement('br'), del);

                tr.append(image_td, name_td, price_td, category_td, quantity_td, action_td);
                document.getElementById("dataTable").appendChild(tr);
            }
        }
    }
    request.send();
}

function allCategories() {
    resetElements();

    let addButton = document.createElement("button");
    addButton.id = "add";
    addButton.innerHTML = "Add new category";
    addButton.onclick = function () {
        let url = new URL(window.location.protocol + "/" + window.location.host + "/newCategory");
        url.searchParams.set('categoryId', "new");
        window.location.replace(url);
    }
    document.getElementById("main").insertBefore(addButton, document.getElementById("hr"));

    document.getElementById("main").removeChild(document.getElementById("dataTable"));
    let categoriesTable = document.createElement("table");
    categoriesTable.style.borderSpacing = "1em";
    categoriesTable.style.marginLeft = "5%";
    categoriesTable.id = "dataTable";
    let trTitle = document.createElement('tr');
    let th1 = document.createElement('th');
    th1.innerText = "Name";
    let th2 = document.createElement('th');
    th2.innerText = "Actions";
    trTitle.append(th1, th2);
    categoriesTable.appendChild(trTitle);
    document.getElementById("main").appendChild(categoriesTable);

    let request = initRequest();
    request.open("GET", "categories");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let categories = request.response;

            for (let i = 0; i < categories.length; i++) {
                let tr = document.createElement("tr");

                let name_td = document.createElement("td");
                name_td.innerText = categories[i].name;

                let action_td = document.createElement("td");
                let edit = document.createElement("button");
                edit.onclick = function () {
                    let url = new URL(window.location.protocol + "/" + window.location.host + "/newCategory");
                    url.searchParams.set('categoryId', categories[i].id);
                    window.location.replace(url);
                }
                edit.innerHTML = "✏️";
                let del = document.createElement("button");
                del.style.marginTop = "5px";
                del.onclick = function () {
                    let check = confirm("If you delete category, all products of it will be deleted.\nAre you sure you want delete category?");
                    if (check) {
                        let requestDel = initRequest();
                        requestDel.open("DELETE", "categories/" + categories[i].id);
                        requestDel.onreadystatechange = function () {
                            if (requestDel.readyState === 4 && requestDel.status === 200) {
                                allCategories();
                            }
                        }
                        requestDel.send();
                    }
                }
                del.innerHTML = "❌";
                action_td.append(edit, document.createElement('br'), del);

                tr.append(name_td, action_td);
                document.getElementById("dataTable").appendChild(tr);
            }
        }
    }
    request.send();
}

function allOrders() {
    resetElements();
    addSearchOrdersSelect();
    searchOrders();
}

function searchOrders(ordersStatus = "ALL") {
    document.getElementById("main").removeChild(document.getElementById("dataTable"));
    let ordersTable = document.createElement("table");
    ordersTable.style.borderSpacing = "1em";
    ordersTable.style.marginLeft = "5%";
    ordersTable.id = "dataTable";
    ordersTable.style.width = "50vw";
    let trTitle = document.createElement('tr');
    let th1 = document.createElement('th');
    th1.innerText = "Date";
    let th2 = document.createElement('th');
    th2.innerText = "Total price";
    let th3 = document.createElement('th');
    th3.innerText = "Status";
    let th4 = document.createElement('th');
    th4.innerText = "Action";
    trTitle.append(th1, th2, th3, th4);
    ordersTable.appendChild(trTitle);
    document.getElementById("main").appendChild(ordersTable);

    let request = initRequest();
    request.open("GET", "orders");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let orders = request.response;

            if (ordersStatus !== "ALL") {
                orders = orders.filter(o => o.status === ordersStatus);
            }
            for (let i = 0; i < orders.length; i++) {
                let tr = document.createElement("tr");

                let date_td = document.createElement("td");
                date_td.innerText = orders[i].dateTime;

                let totalPrice_td = document.createElement("td");
                totalPrice_td.innerText = orders[i].totalPrice + " ₴";

                let status_td = document.createElement("td");
                status_td.innerText = orders[i].status;

                let action_td = document.createElement("td");
                let edit = document.createElement("button");
                edit.onclick = function () {
                    let url = new URL(window.location.protocol + "/" + window.location.host + "/newOrder");
                    url.searchParams.set('orderId', orders[i].id);
                    window.location.replace(url);
                }
                edit.innerHTML = "✏️";
                action_td.append(edit);

                tr.append(date_td, totalPrice_td, status_td, action_td);
                document.getElementById("dataTable").appendChild(tr);

                tr = document.createElement("tr");
                let td_details = document.createElement("td");
                td_details.colSpan = 4;

                let details = document.createElement("details");
                let summary = document.createElement("summary");
                summary.innerHTML = "View details";
                summary.style.color = "blue";
                details.appendChild(summary);

                let productsRequest = initRequest();
                productsRequest.open("GET", "orders/products/" + orders[i].id);
                productsRequest.responseType = "json";
                productsRequest.onreadystatechange = function () {
                    if (productsRequest.readyState === 4 && productsRequest.status === 200) {
                        let products = productsRequest.response;

                        products = [...new Map(products.map(v => [v.id, v])).values()];
                        for (let j = 0; j < products.length; j++) {
                            let detailsProducts = document.createElement("div");
                            detailsProducts.style.width = "50vw";
                            detailsProducts.style.display = "flex";
                            detailsProducts.style.paddingTop = "3vh";
                            detailsProducts.style.paddingBottom = "3vh";

                            let image_div = document.createElement("div");
                            image_div.style.width = "12vw";
                            let image = document.createElement("img");
                            image.style.width = "50px";
                            image.src = "data:image/png;base64," + products[j].image;
                            image_div.appendChild(image);

                            let name_div = document.createElement("div");
                            name_div.innerText = products[j].name;
                            name_div.style.width = "12vw";

                            let price_div = document.createElement("div");
                            price_div.innerText = products[j].price + " ₴";
                            price_div.style.width = "12vw";

                            let pieces_div = document.createElement("div");
                            pieces_div.style.width = "12vw";
                            loadPieces(pieces_div, products[j].id, orders[i].id);

                            detailsProducts.append(image_div, name_div, price_div, pieces_div);
                            details.appendChild(detailsProducts);
                        }

                        let customerRequest = initRequest();
                        customerRequest.open("GET", "users/" + orders[i].userId);
                        customerRequest.responseType = "json";
                        customerRequest.onreadystatechange = function () {
                            if (customerRequest.readyState === 4 && customerRequest.status === 200) {
                                let customer = customerRequest.response;

                                let detailsCustomer = document.createElement("div");
                                detailsCustomer.style.width = "45vw";
                                detailsCustomer.style.textAlign = "left";
                                detailsCustomer.style.paddingLeft = "5vw";
                                detailsCustomer.style.backgroundColor = "paleturquoise";

                                let customer_title = document.createElement("p");
                                customer_title.innerHTML = "Customer:";

                                let name = document.createElement("p");
                                name.innerHTML = "Name: " + orders[i].customerName;

                                let address = document.createElement("p");
                                address.innerHTML = "Address to delivery: " + orders[i].address;

                                let email = document.createElement("p");
                                email.innerHTML = "Email: " + customer.email;

                                detailsCustomer.append(customer_title, name, email, address);
                                details.appendChild(detailsCustomer);

                                td_details.appendChild(details);
                            }
                        }
                        customerRequest.send();
                    }
                }
                productsRequest.send();

                tr.append(td_details);
                document.getElementById("dataTable").appendChild(tr);
            }
        }
    }
    request.send();
}


function allUsers(usersDiv, concreteUsers = "") {
    if (document.getElementById("usersTable") !== null) {
        document.getElementById("dataTable").removeChild(document.getElementById("usersTable"));
    }
    let usersTable = document.createElement("table");
    usersTable.style.borderSpacing = "1em";
    usersTable.style.marginLeft = "5%";
    usersTable.id = "usersTable";
    usersDiv.appendChild(usersTable);

    let trTitle = document.createElement('tr');
    let th1 = document.createElement('th');
    th1.innerText = "First name";
    let th2 = document.createElement('th');
    th2.innerText = "Last name";
    let th3 = document.createElement('th');
    th3.innerText = "Email";
    let th4 = document.createElement('th');
    th4.innerText = "Role";
    let th5 = document.createElement('th');
    th5.innerText = "Actions";
    trTitle.append(th1, th2, th3, th4, th5);
    usersTable.appendChild(trTitle);

    let request = initRequest();
    request.open("GET", "users/role/" + concreteUsers);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let users = request.response;

            for (let i = 0; i < users.length; i++) {
                let tr = document.createElement("tr");

                let name_td = document.createElement("td");
                name_td.innerText = users[i].firstName;

                let lastName_td = document.createElement("td");
                lastName_td.innerText = users[i].lastName;

                let email_td = document.createElement("td");
                email_td.innerText = users[i].email;

                let role_td = document.createElement("td");
                role_td.innerText = users[i].role;

                let action_td = document.createElement("td");
                let edit = document.createElement("button");
                edit.onclick = function () {
                    let url = new URL(window.location.protocol + "/" + window.location.host + "/newUser");
                    url.searchParams.set('userId', users[i].id);
                    window.location.replace(url);
                }
                edit.innerHTML = "✏️";
                let del = document.createElement("button");
                del.style.marginTop = "5px";
                del.onclick = function () {
                    let requestDel = initRequest();
                    requestDel.open("DELETE", "users/" + users[i].id);
                    requestDel.onreadystatechange = function () {
                        if (requestDel.readyState === 4 && requestDel.status === 200) {
                            allUsers(usersDiv, users[i].role);
                        }
                    }
                    requestDel.send();
                }
                del.innerHTML = "❌";
                action_td.append(edit, document.createElement('br'), del);

                tr.append(name_td, lastName_td, email_td, role_td, action_td);
                usersTable.appendChild(tr);
            }
        }
    }
    request.send();
}

function allUsersAdmin() {
    resetElements();

    let addButton = document.createElement("button");
    addButton.id = "add";
    addButton.innerHTML = "Add new user";
    addButton.onclick = function () {
        let url = new URL(window.location.protocol + "/" + window.location.host + "/newUser");
        url.searchParams.set('userId', "new");
        window.location.replace(url);
    }
    document.getElementById("main").insertBefore(addButton, document.getElementById("hr"));

    document.getElementById("main").removeChild(document.getElementById("dataTable"));

    let usersDiv = document.createElement("div");
    usersDiv.id = "dataTable";

    let buttonSellers = document.createElement("button");
    buttonSellers.innerHTML = "Sellers";
    buttonSellers.onclick = function () {
        allUsers(usersDiv,"SELLER");
    }

    let buttonCustomers = document.createElement("button");
    buttonCustomers.style.marginLeft = "10%";
    buttonCustomers.innerHTML = "Users";
    buttonCustomers.onclick = function () {
        allUsers(usersDiv,"USER");
    }

    usersDiv.append(buttonSellers, buttonCustomers);
    document.getElementById("main").appendChild(usersDiv);
    allUsers(usersDiv, "SELLER");
}

function allUsersSuperAdmin() {
    resetElements();

    let addButton = document.createElement("button");
    addButton.id = "add";
    addButton.innerHTML = "Add new user";
    addButton.onclick = function () {
        let url = new URL(window.location.protocol + "/" + window.location.host + "/newUser");
        url.searchParams.set('userId', "new");
        window.location.replace(url);
    }
    document.getElementById("main").insertBefore(addButton, document.getElementById("hr"));

    document.getElementById("main").removeChild(document.getElementById("dataTable"));

    let usersDiv = document.createElement("div");
    usersDiv.id = "dataTable";

    let buttonAdmins = document.createElement("button");
    buttonAdmins.innerHTML = "Admins";
    buttonAdmins.onclick = function () {
        allUsers(usersDiv,"ADMIN");
    }

    let buttonSellers = document.createElement("button");
    buttonSellers.innerHTML = "Sellers";
    buttonSellers.style.marginLeft = "10%";
    buttonSellers.onclick = function () {
        allUsers(usersDiv,"SELLER");
    }

    let buttonCustomers = document.createElement("button");
    buttonCustomers.style.marginLeft = "10%";
    buttonCustomers.innerHTML = "Users";
    buttonCustomers.onclick = function () {
        allUsers(usersDiv,"USER");
    }

    usersDiv.append(buttonAdmins, buttonSellers, buttonCustomers);
    document.getElementById("main").appendChild(usersDiv);
    allUsers(usersDiv, "ADMIN");
}

function allUsersById() {
    let request = initRequest();
    request.open("GET", "users/info");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;
            if (user.role === "ADMIN") {
                allUsersAdmin();
            } else if (user.role === "SUPER_ADMIN") {
                allUsersSuperAdmin();
            } else {
                alert("Request not supported");
                allProducts();
            }
        }
    }
    request.send();
}

function resetElements() {
    if (document.getElementById("add") !== null) {
        document.getElementById("main").removeChild(document.getElementById("add"));
    }
    if (document.getElementById("search") !== null) {
        document.getElementById("main").removeChild(document.getElementById("search"));
    }
}
