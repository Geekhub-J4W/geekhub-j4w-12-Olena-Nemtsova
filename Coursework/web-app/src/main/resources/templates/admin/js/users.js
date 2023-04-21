function loadUsers() {
    document.getElementById("show").onclick = function () {
        searchUsers(
            document.getElementById("role_select").value,
            document.getElementById("limit_select").value,
            1,
            document.getElementById("search_input").value
        );
    }

    document.getElementById("addNew").onclick = function () {
        let url = new URL(window.location.protocol + "/" + window.location.host + "/admin/user");
        url.searchParams.set('userId', 'new');
        window.location.replace(url);
    }

    loadAdminPages();
    searchUsers();
}

function searchUsers(role = "USER", limit = 10, currentPage = 1, input = "") {
    input = input === "" ? null : input;
    cleanContainer();

    if (role.includes("view")) {
        document.getElementById("actions").style.display = "none";
    } else {
        document.getElementById("actions").style.display = "block";
    }

    let request = initRequest();
    request.open("GET", "/users/" + role.replace("view", "") + "/" + limit + "/" + currentPage + "/" + input);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let users = request.response;
            for (let i = 0; i < users.length; i++) {
                let container_element = document.createElement("div");
                container_element.className = "container_element";
                document.getElementById("container").appendChild(container_element);

                let firstName = document.createElement("span");
                firstName.innerText = users[i].firstName;
                let lastName = document.createElement("span");
                lastName.innerText = users[i].lastName;
                let email = document.createElement("span");
                email.innerText = users[i].email;

                if (role.includes("view")) {
                    createTableRow([firstName, lastName, email], container_element);
                } else {
                    let editBtn = document.createElement("button");
                    editBtn.innerText = "✏️";
                    editBtn.onclick = function () {
                        editUser(users[i].id);
                    }
                    let removeBtn = document.createElement("button");
                    removeBtn.innerText = "❌";
                    removeBtn.onclick = function () {
                        removeUser(users[i].id, role, limit, currentPage, input);
                    }
                    let actionsDiv = document.createElement("div");
                    actionsDiv.append(editBtn, removeBtn);

                    createTableRow([firstName, lastName, email, actionsDiv], container_element);
                }
                if (i === users.length - 1) {
                    loadUsersPages(role.replace("view", ""), currentPage, limit, input);
                }
            }
        }
    }
    request.send();
}

function loadUsersPages(role, currentPage, limit, input) {
    let request = initRequest();
    request.open("GET", "/users/pages/" + role + "/" + limit + "/" + input);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let pagesCount = Number(request.responseText);
            createPages(pagesCount, currentPage, limit, input, searchUsers, role);
        }
    }
    request.send();
}

function editUser(userId) {
    let url = new URL(window.location.protocol + "/" + window.location.host + "/admin/user");
    url.searchParams.set('userId', userId);
    window.location.replace(url);
}

function removeUser(userId, role, limit, currentPage, input) {
    let requestDel = initRequest();
    requestDel.open("DELETE", "/users/" + userId);
    requestDel.onreadystatechange = function () {
        if (requestDel.readyState === 4 && requestDel.status === 200) {
            searchUsers(role, limit, currentPage, input);
        }
    }
    requestDel.send();
}

function loadUser() {
    loadAdminPages();

    const urlParams = new URLSearchParams(window.location.search);
    let userId = urlParams.get("userId");
    if (userId === "new") {
        document.getElementById("messageForEdit").style.display = "none";
        return;
    }

    document.getElementById("headline").innerHTML = "Edit user";
    document.getElementById("submit").innerHTML = "Edit";

    let request = initRequest();
    request.open("GET", "/users/" + userId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;

            document.getElementById("firstName").value = user.firstName;
            document.getElementById("lastName").value = user.lastName;
            document.getElementById("email").value = user.email;
            document.getElementById("role").value = user.role;
        }
    }
    request.send();
}

function submit() {
    const urlParams = new URLSearchParams(window.location.search);
    let userId = urlParams.get("userId");

    if (!checked(userId)) {
        return;
    }

    let user = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        role: document.getElementById("role").value
    };
    if (document.getElementById("password").value !== "") {
        user.password = document.getElementById("password").value;
    }

    let request = initRequest();
    if (userId === "new") {
        request.open("POST", "/users");
    } else {
        request.open("PUT", "/users/" + userId);
    }
    request.setRequestHeader("Accept", "application/json");
    request.setRequestHeader("Content-Type", "application/json");

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            if (request.response === "") {
                alert("Something went wrong :(");
                return;
            }

            let message = userId === "new" ? "user successfully added!" : "user successfully edited!";
            alert(message);
            window.location.replace("/admin/users");
        }
    }
    request.send(JSON.stringify(user));
}

function checked(userId) {
    return document.getElementById("password").value !== "" || userId === "new"
        ? checkFirstName()
        && checkLastName()
        && checkEmail()
        && checkPassword()
        && checkPasswordConfirm()

        : checkFirstName()
        && checkLastName()
        && checkEmail();
}



