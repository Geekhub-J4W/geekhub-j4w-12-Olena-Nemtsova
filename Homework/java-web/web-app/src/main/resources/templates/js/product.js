function load() {
    loadProduct();
    loadCategories();
}

function loadProduct() {
    const urlParams = new URLSearchParams(window.location.search);
    let productId = urlParams.get("productId");

    let request = initRequest();

    request.open("GET", "products/" + productId);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {

            let product = request.response;

            document.getElementById("name").innerText = product.name;
            document.getElementById("image").src = "data:image/png;base64," + product.image;
            document.getElementById("price").innerHTML = product.price;
            if (product.quantity === 0) {
                let availability = document.getElementById("availability");
                availability.style.color = "red";
                availability.innerHTML = "● Product out of stock";
                document.getElementById("bucketBtn").disabled = true;
            }
            document.getElementById("bucketBtn").onclick = function () {
                addBucketProduct(productId);
            }

            let favorBtn = document.getElementById("favorBtn");
            if (favorBtn !== null) {
                let requestFavorite = initRequest();
                requestFavorite.open("GET", "favorites/" + productId);
                requestFavorite.onreadystatechange = function () {
                    if (requestFavorite.readyState === 4 && requestFavorite.status === 200) {
                        let isFavorite = requestFavorite.responseText === "true";
                        if (!isFavorite) {
                            favorBtn.onclick = function () {
                                addFavoriteProduct(product.id);
                                location.reload();
                            }
                            favorBtn.innerHTML = "Add to favorites 🤍";
                        } else {
                            favorBtn.onclick = function () {
                                deleteFavoriteProduct(product.id);
                                location.reload();
                            }
                            favorBtn.innerHTML = "Delete favorite ❤️";
                        }
                    }
                }
                requestFavorite.send();
            }
            loadRating(product.id);
            loadReviews(product.id);
        }
    }
    request.send();
}

function loadRating(productId) {
    let request = initRequest();

    request.open("GET", "reviews/rating/" + productId);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let rating = Number(request.responseText);

            if (rating === 0) {
                for (let i = 1; i <= 5; i++) {
                    document.getElementById(i).style.color = "grey";
                }
                return;
            }
            for (let i = 1; i <= Math.round(rating); i++) {
                document.getElementById(i).style.color = "yellow";
            }
        }
    }
    request.send();
}

function loadReviews(productId) {
    let request = initRequest();

    request.open("GET", "reviews/" + productId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let reviews = request.response;

            if (reviews.length === 0) {
                let emptyMessage = document.createElement("h3");
                emptyMessage.style.color = "grey";
                emptyMessage.innerHTML = "empty";
                document.getElementById("reviews").appendChild(emptyMessage);
                return;
            }

            for (let i = 0; i < reviews.length; i++) {
                let div = document.createElement("div");
                document.getElementById("reviews").appendChild(div);

                let username = document.createElement("p");
                let userRequest = initRequest();
                userRequest.open("GET", "orders/customer/" + reviews[i].orderId)
                userRequest.responseType = "json";
                userRequest.onreadystatechange = function () {
                    if (userRequest.readyState === 4 && userRequest.status === 200) {
                        let user = userRequest.response;
                        username.innerHTML = reviews[i].dateTime + " " + user.firstName + " " + user.lastName;
                    }
                }
                userRequest.send();

                let rating = document.createElement("div");
                for (let j = 1; j <= 5; j++) {
                    let star = document.createElement("span");
                    star.innerHTML = "★";
                    if (j <= reviews[i].rating) {
                        star.style.color = "yellow";
                    }
                    rating.appendChild(star);
                }
                let text = document.createElement("p");
                text.innerHTML = reviews[i].text;
                let hr = document.createElement("hr");

                div.append(username, rating, text, hr);
            }
        }
    }
    request.send();
}
