function loadAll() {
    let pageId = getCookie("pageId");

    if (pageId === undefined || pageId === "products") {
        allProducts();
    } else if (pageId === "categories") {
        allCategories();
    } else if (pageId === "users") {
        allUsers();
    } else if (pageId === "orders") {
        allOrders();
    }
    removeCookie("pageId");
    removeCookie("productId");
    removeCookie("customerId");
    removeCookie("orderId");
    removeCookie("newUserId");
    removeCookie("categoryId");
}

function allProducts() {
    if (document.getElementById("add") !== null) {
        document.getElementById("main").removeChild(document.getElementById("add"));
    }
    if (document.getElementById("searchByName") !== null) {
        document.getElementById("main").removeChild(document.getElementById("searchByName"));
    }

    let addButton = document.createElement("button");
    addButton.id = "add";
    addButton.innerHTML = "Add new product";
    addButton.onclick = function () {
        document.cookie = "productId=-1";
        window.location.replace("/newProduct");
    }
    document.getElementById("main").insertBefore(addButton, document.getElementById("hr"));
    addSearchInput();

    searchProducts();
}

function addSearchInput() {
    let inputDiv = document.createElement("div");
    inputDiv.style.display = "inline-block";
    inputDiv.id = "searchByName";

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
                image.src = products[i].imagePath;
                image_td.appendChild(image);

                let name_td = document.createElement("td");
                name_td.innerText = products[i].name;

                let price_td = document.createElement("td");
                price_td.innerText = products[i].price;

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
                    document.cookie = "productId=" + products[i].id;
                    window.location.replace("newProduct");
                }
                edit.innerHTML = "✏️";
                let del = document.createElement("button");
                del.style.marginTop = "5px";
                del.onclick = function () {
                    let requestDel = initRequest();
                    requestDel.open("DELETE", "products/deleteProduct/" + products[i].id);
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
    if (document.getElementById("add") !== null) {
        document.getElementById("main").removeChild(document.getElementById("add"));
    }
    if (document.getElementById("searchByName") !== null) {
        document.getElementById("main").removeChild(document.getElementById("searchByName"));
    }
    let addButton = document.createElement("button");
    addButton.id = "add";
    addButton.innerHTML = "Add new category";
    addButton.onclick = function () {
        document.cookie = "categoryId=-1";
        window.location.replace("newCategory");
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
                    document.cookie = "categoryId=" + categories[i].id;
                    window.location.replace("newCategory");
                }
                edit.innerHTML = "✏️";
                let del = document.createElement("button");
                del.style.marginTop = "5px";
                del.onclick = function () {
                    let check = confirm("If you delete category, all products of it will be deleted.\nAre you sure you want delete category?");
                    if (check) {
                        let requestDel = initRequest();
                        requestDel.open("DELETE", "categories/deleteCategory/" + categories[i].id);
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
    if (document.getElementById("add") !== null) {
        document.getElementById("main").removeChild(document.getElementById("add"));
    }
    if (document.getElementById("searchByName") !== null) {
        document.getElementById("main").removeChild(document.getElementById("searchByName"));
    }

    document.getElementById("main").removeChild(document.getElementById("dataTable"));
    let ordersTable = document.createElement("table");
    ordersTable.style.borderSpacing = "1em";
    ordersTable.style.marginLeft = "5%";
    ordersTable.id = "dataTable";
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

            for (let i = 0; i < orders.length; i++) {
                let tr = document.createElement("tr");

                let date_td = document.createElement("td");
                date_td.innerText = orders[i].dateTime;

                let totalPrice_td = document.createElement("td");
                totalPrice_td.innerText = orders[i].totalPrice;

                let status_td = document.createElement("td");
                status_td.innerText = orders[i].status;

                let action_td = document.createElement("td");
                let edit = document.createElement("button");
                edit.onclick = function () {
                    document.cookie = "orderId=" + orders[i].id;
                    document.cookie = "customerId=" + orders[i].userId;
                    window.location.replace("newOrder");
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
                details.style.width = "100%";

                let productsRequest = initRequest();
                productsRequest.open("GET", "orders/products/" + orders[i].id);
                productsRequest.responseType = "json";
                productsRequest.onreadystatechange = function () {
                    if (productsRequest.readyState === 4 && productsRequest.status === 200) {
                        let products = productsRequest.response;

                        for (let i = 0; i < products.length; i++) {
                            let detailsProducts = document.createElement("div");
                            detailsProducts.style.width = "100%";
                            detailsProducts.style.display = "flex";
                            detailsProducts.style.paddingTop = "3vh";

                            let image_td = document.createElement("div");
                            image_td.style.width = "33%";
                            let image = document.createElement("img");
                            image.style.width = "50px";
                            image.src = products[i].imagePath;
                            image_td.appendChild(image);

                            let name_td = document.createElement("div");
                            name_td.innerText = products[i].name;
                            name_td.style.width = "33%";

                            let price_td = document.createElement("div");
                            price_td.innerText = products[i].price;
                            price_td.style.width = "33%";

                            detailsProducts.append(image_td, name_td, price_td);
                            details.appendChild(detailsProducts);
                        }

                        let customerRequest = initRequest();
                        customerRequest.open("GET", "orders/customer/" + orders[i].id);
                        customerRequest.responseType = "json";
                        customerRequest.onreadystatechange = function () {
                            if (customerRequest.readyState === 4 && customerRequest.status === 200) {
                                let customer = customerRequest.response;

                                let detailsCustomer = document.createElement("tr");
                                detailsCustomer.style.width = "100%";

                                let td_title = document.createElement("td");
                                td_title.innerHTML = "Customer:";

                                let td_name = document.createElement("td");
                                td_name.innerHTML = customer.firstName;

                                let td_lastName = document.createElement("td");
                                td_lastName.innerHTML = customer.lastName;

                                let td_email = document.createElement("td");
                                td_email.innerHTML = customer.email;

                                detailsCustomer.append(td_title, td_name, td_lastName, td_email);
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

function allUsers(concreteUsers = "") {
    if (document.getElementById("add") !== null) {
        document.getElementById("main").removeChild(document.getElementById("add"));
    }
    if (document.getElementById("searchByName") !== null) {
        document.getElementById("main").removeChild(document.getElementById("searchByName"));
    }

    let addButton = document.createElement("button");
    addButton.id = "add";
    addButton.innerHTML = "Add new admin";
    addButton.onclick = function () {
        document.cookie = "newUserId=-1";
        window.location.replace("newUser");
    }
    document.getElementById("main").insertBefore(addButton, document.getElementById("hr"));

    document.getElementById("main").removeChild(document.getElementById("dataTable"));

    let usersDiv = document.createElement("div");
    usersDiv.id = "dataTable";

    let buttonAdmins = document.createElement("button");
    buttonAdmins.innerHTML = "Admins";
    buttonAdmins.onclick = function () {
        allUsers("/admins");
    }

    let buttonCustomers = document.createElement("button");
    buttonCustomers.style.marginLeft = "10%";
    buttonCustomers.innerHTML = "Customers";
    buttonCustomers.onclick = function () {
        allUsers("/customers");
    }

    let usersTable = document.createElement("table");
    usersTable.style.borderSpacing = "1em";
    usersTable.style.marginLeft = "5%";
    usersTable.id = "usersTable";

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

    usersDiv.append(buttonAdmins, buttonCustomers, usersTable);
    document.getElementById("main").appendChild(usersDiv);

    let request = initRequest();
    request.open("GET", "users" + concreteUsers);
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
                if (users[i].admin) {
                    role_td.innerText = "Admin";
                } else {
                    role_td.innerText = "Cusromer";
                }

                let action_td = document.createElement("td");
                let edit = document.createElement("button");
                edit.onclick = function () {
                    document.cookie = "newUserId=" + users[i].id;
                    window.location.replace("newUser");
                }
                edit.innerHTML = "✏️";
                let del = document.createElement("button");
                del.style.marginTop = "5px";
                del.onclick = function () {
                    let requestDel = initRequest();
                    requestDel.open("DELETE", "users/deleteUser/" + users[i].id);
                    requestDel.onreadystatechange = function () {
                        if (requestDel.readyState === 4 && requestDel.status === 200) {
                            allUsers();
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
