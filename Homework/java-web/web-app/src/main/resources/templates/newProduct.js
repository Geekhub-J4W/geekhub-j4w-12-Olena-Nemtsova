function loadProduct() {
    loadCategories();

    const urlParams = new URLSearchParams(window.location.search);
    let productId = urlParams.get("productId");
    if (productId !== "new") {
        document.getElementById("submit").innerHTML = "Edit";
        document.getElementById("legend").innerHTML = "edit product";

        let request = initRequest();
        request.open("GET", "products/" + productId);
        request.responseType = "json";
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let product = request.response;

                document.getElementById("name").value = product.name;
                document.getElementById("price").value = product.price;
                document.getElementById("icon").src = "data:image/png;base64," + product.image;
                document.getElementById("category").value = product.categoryId;
                document.getElementById("quantity").value = product.quantity;
            }
        }
        request.send();
    }
}

function loadCategories() {
    let request = initRequest();
    request.open("GET", "categories");
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let categories = request.response;

            for (let i = 0; i < categories.length; i++) {
                let option = document.createElement("option");
                option.value = categories[i].id;
                option.innerText = categories[i].name;

                document.getElementById("category").appendChild(option);
            }
        }
    }
    request.send();
}

document.getElementById("image").addEventListener("change", handleFile, false);

function handleFile() {
    const file = this.files[0];
    document.getElementById("icon").src = URL.createObjectURL(file);
    resetErrorById('error_image');
}

function submit() {

    if (checked()) {
        const urlParams = new URLSearchParams(window.location.search);
        let productId = urlParams.get("productId");

        let product = {
            name: document.getElementById("name").value,
            price: Number(document.getElementById("price").value),
            categoryId: Number(document.getElementById("category").value),
            quantity: document.getElementById("quantity").value
        };

        let request = initRequest();
        if (productId === "new") {
            request.open("POST", "products");
        } else {
            request.open("POST", "products/" + productId);
        }
        request.setRequestHeader("Accept", "application/json");
        request.setRequestHeader("Content-Type", "application/json");

        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                let product = JSON.parse(request.response);

                let requestImg = initRequest();
                requestImg.open("POST", "products/setImage/" + product.id);

                let formData = new FormData();
                fetch(document.getElementById("icon").src)
                    .then(res => res.blob())
                    .then(blob => {
                            const file = new File([blob], 'file', {type: blob.type});
                            formData.append("file", file);
                            requestImg.send(formData);
                        }
                    )

                let message = "product successfully added!";
                if (productId !== "new") {
                    message = "product successfully edited!";
                }
                alert(message);
                document.location.replace("/main");
            }
        }
        request.send(JSON.stringify(product));
    }
}

function checked() {
    return checkName()
        && checkPrice()
        && checkImage()
        && checkQuantity();
}

