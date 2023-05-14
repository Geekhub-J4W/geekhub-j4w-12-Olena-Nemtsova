let stompClient;
let ownId;

function loadChat() {
    loadAdminPages();

    const urlParams = new URLSearchParams(window.location.search);
    let userId = urlParams.get("userId");
    let adminId = urlParams.get("adminId");
    loadUserName(userId);

    let request = initRequest();
    request.open("GET", "/users");
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;
            ownId = user.id;

            if (Number(adminId) !== Number(ownId) && user.role !== "SUPER_ADMIN") {
                document.getElementById("send").disabled = true;
                document.getElementById("newMessage").disabled = true;
            }
            loadMessages(userId);
        }
    }
    request.send();
}

function loadMessages(userId) {
    let request = initRequest();
    request.open("GET", "/messages/" + userId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let messages = request.response;
            for (let i = 0; i < messages.length; i++) {
                let messageDiv = document.createElement("div");
                let dateDiv = document.createElement("div");
                dateDiv.className = "message_date";

                messageDiv.innerText = messages[i].text;
                dateDiv.innerText = messages[i].dateTime;
                messageDiv.appendChild(dateDiv);

                if (messages[i].senderId === ownId) {
                    messageDiv.className = "message_own own";
                } else {
                    messageDiv.className = "message not_own";
                }

                document.getElementById("chat_container").appendChild(messageDiv);
            }
            connect(userId);
        }
    }
    request.send();
}

function connect(userId) {
    let headers = {};
    let header = document.querySelector('meta[name="_csrf_header"]').content;
    headers[header] = document.querySelector('meta[name="_csrf"]').content;

    let socket = new SockJS('http://localhost:8081/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect(headers, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/reply/' + userId, function (response) {
            let message = JSON.parse(response.body);

            let messageDiv = document.createElement("div");
            let dateDiv = document.createElement("div");

            messageDiv.innerText = message.text;
            dateDiv.innerText = message.dateTime;
            dateDiv.className = "message_date";
            messageDiv.appendChild(dateDiv);

            if (message.senderId === ownId) {
                messageDiv.className = "message_own own";
            } else {
                messageDiv.className = "message not_own";
            }
            document.getElementById("chat_container").appendChild(messageDiv);
        });
    });

}

function loadUserName(userId) {
    let request = initRequest();
    request.open("GET", "/users/" + userId);
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;
            document.getElementById("userName").innerText = user.firstName + " " + user.lastName;
        }
    }
    request.send();

    document.getElementById("send").onclick = function () {
        let text = document.getElementById("newMessage").value;
        if (text.trim() === "") {
            return;
        }
        stompClient.send("/app/chat/" + userId, {}, JSON.stringify({
            text: text,
            senderId: ownId,
            dateTime: getNow()
        }));
        document.getElementById("newMessage").value = "";
    }
}

window.onbeforeunload = function () {
    stompClient.disconnect();
};
