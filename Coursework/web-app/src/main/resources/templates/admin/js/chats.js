let stompClient;

function loadChats() {
    loadAdminPages();
    showChats();
}

function showChats(limit = 10, currentPage = 1, adminId = -1) {
    cleanContainer();

    let request = initRequest();
    request.open("GET", "/chats/" + limit + "/" + currentPage + "/" + adminId);
    request.responseType = "json";

    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let chats = request.response;

            if (chats.length === 0) {
                let empty = document.createElement("div");
                empty.id = "empty";
                empty.innerText = "Empty";
                document.getElementById("container").appendChild(empty);
                return;
            }
            for (let i = 0; i < chats.length; i++) {
                let container_element = document.createElement("div");
                container_element.className = "container_element chat";
                container_element.onclick = function () {
                    redirectToChat(chats[i].userId, chats[i].adminId);
                }
                document.getElementById("container").appendChild(container_element);

                let owner = document.createElement("span");
                owner.className = "owner_label";
                setUserName(owner, chats[i].adminId);

                let user = document.createElement("span");
                user.className = "from_label";
                user.innerText = "From: ";
                setUserName(user, chats[i].userId);

                let message = document.createElement("span");
                message.className = "message_label";
                loadFirstMessage(message, chats[i].userId);

                let addBtn = document.createElement("button");
                addBtn.className = "owner_add";
                addBtn.innerText = "➕️";
                addBtn.onclick = function () {
                    addOwnChat(chats[i].id, chats[i].userId);
                }
                if (chats[i].adminId !== 0) {
                    addBtn.style.display = "none";
                }

                createTableRow([owner, user, message, addBtn], container_element);
                if (i === chats.length - 1) {
                    loadChatsPages(adminId, currentPage, limit);
                }
            }
        }
    }
    request.send();
}

function loadChatsPages(adminId, currentPage, limit) {
    let request = initRequest();
    request.open("GET", "/chats/pages/" + limit + "/" + adminId);
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let pagesCount = Number(request.responseText);
            createPages(pagesCount, currentPage, limit, null, showChats);

            if (stompClient === undefined) {
                connect();
            }
        }
    }
    request.send();
}

function loadFirstMessage(mess, userId) {
    let request = initRequest();
    request.open("GET", "/messages/last/" + userId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let message = request.response;

            let date = document.createElement("span");
            date.innerText = message.dateTime;

            let text = document.createElement("span");
            let textMessage = message.text;
            if (textMessage.length > 13) {
                textMessage = textMessage.slice(0, 13);
                textMessage += "...";
            }
            text.innerText = textMessage;

            mess.append(date, document.createElement("br"), text);
        }
    }
    request.send();
}

function addOwnChat(chatId, userId) {
    stompClient.send("/app/chat/update/" + chatId, {}, JSON.stringify({
        userId: userId
    }));
}

function setUserName(span, userId) {
    if (userId === 0) {
        span.innerText = "New";
        span.style.backgroundColor = "red";
        return;
    }
    let request = initRequest();
    request.open("GET", "/users/" + userId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;
            span.innerText = span.innerText + user.firstName + " " + user.lastName;
        }
    }
    request.send();
}

function connect() {
    let headers = {};
    let header = document.querySelector('meta[name="_csrf_header"]').content;
    headers[header] = document.querySelector('meta[name="_csrf"]').content;

    let socket = new SockJS('http://localhost:8081/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/refresh', function () {
            document.getElementById("show").click();
        });
    });
}

function redirectToChat(userId, adminId) {
    let url = new URL(window.location.protocol + "/" + window.location.host + "/admin/chat");
    url.searchParams.set('userId', userId);
    url.searchParams.set('adminId', adminId);
    window.location.replace(url);
}

document.getElementById("show").onclick = function () {
    showChats(
        document.getElementById("limit_select").value,
        1,
        document.getElementById("chat_select").value
    );
}

window.onbeforeunload = function () {
    stompClient.disconnect();
};
