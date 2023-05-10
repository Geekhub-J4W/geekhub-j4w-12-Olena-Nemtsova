let stompClient;
let userId;

function loadMessages() {
    loadPages();

    let request = initRequest();
    request.open("GET", "/users");
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let user = request.response;
            userId = user.id;
            getMessages();
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

        stompClient.subscribe('/topic/reply/' + userId, function (response) {
            let message = JSON.parse(response.body);

            let messageDiv = document.createElement("div");
            let dateDiv = document.createElement("div");
            dateDiv.className = "message_date";
            messageDiv.innerText = message.text;
            dateDiv.innerText = message.dateTime;
            messageDiv.appendChild(dateDiv);

            if (message.senderId === userId) {
                messageDiv.className = "message_own own";
            } else {
                messageDiv.className = "message not_own";
            }
            document.getElementById("chat_container").appendChild(messageDiv);
        });
    });
}

function getMessages() {
    let request = initRequest();
    request.open("GET", "/messages");
    request.responseType = "json";
    request.onreadystatechange = function () {
        if (request.readyState === 4 && request.status === 200) {
            let messages = request.response;
            for (let i = 0; i < messages.length; i++) {
                let messageDiv = document.createElement("div");
                let dateDiv = document.createElement("div");

                messageDiv.innerText = messages[i].text;
                dateDiv.innerText = messages[i].dateTime;

                dateDiv.className = "message_date";
                messageDiv.appendChild(dateDiv);

                if (messages[i].senderId === userId) {
                    messageDiv.className = "message_own own";
                } else {
                    messageDiv.className = "message not_own";
                }
                document.getElementById("chat_container").appendChild(messageDiv);
            }
            connect();
        }
    }
    request.send();
}

document.getElementById("send").onclick = function () {
    let text = document.getElementById("newMessage").value;
    if (text === "") {
        return;
    }
    stompClient.send("/app/chat", {}, JSON.stringify({
        text: text,
        senderId: userId,
        dateTime: getNow()
    }));
    document.getElementById("newMessage").value = "";
}

window.onbeforeunload = function () {
    stompClient.disconnect();
};

