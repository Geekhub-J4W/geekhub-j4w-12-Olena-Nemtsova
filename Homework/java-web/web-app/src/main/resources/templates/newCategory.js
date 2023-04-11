function loadCategory() {
    const urlParams = new URLSearchParams(window.location.search);
    let categoryId = urlParams.get("categoryId");

    if (categoryId !== "new") {
        document.getElementById("submit").innerHTML = "Edit";
        document.getElementById("legend").innerHTML = "edit category";

        let request = initRequest();
        request.open("GET", "categories/" + categoryId);
        request.responseType = "json";
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let category = request.response;

                document.getElementById("name").value = category.name;
            }
        }
        request.send();
    }
}

function submit() {
    if (checkName()) {
        const urlParams = new URLSearchParams(window.location.search);
        let categoryId = urlParams.get("categoryId");

        let category = {
            name: document.getElementById("name").value
        };

        let request = initRequest();
        if (categoryId === "new") {
            request.open("POST", "categories");
        } else {
            request.open("POST", "categories/" + categoryId);
        }
        request.setRequestHeader("Accept", "application/json");
        request.setRequestHeader("Content-Type", "application/json");

        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {

                let message = "category successfully added!";
                if (categoryId !== "new") {
                    message = "category successfully edited!";
                }
                alert(message);
                allCategories();
            }
        }
        let obj = JSON.stringify(category);

        request.send(obj);
    }
}


