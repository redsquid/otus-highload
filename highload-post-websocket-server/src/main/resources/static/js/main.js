'use strict';
var publisherForm = document.querySelector('#publisherForm');
var messageArea = document.querySelector('#messageArea');
var stompClient = null;
var username = null;

function connect(event) {
    username = document.querySelector('#name').value.trim();
    if(username) {
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}
function onConnected() {
    stompClient.subscribe('/topic/' + username, onMessageReceived);
}
function onError(error) {
    console.error("ERROR");
}

function onMessageReceived(payload) {
    var messageElement = document.createElement('li');
    var postText = document.createTextNode(JSON.parse(payload.body).post);
    messageElement.appendChild(postText);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

publisherForm.addEventListener('submit', connect, true)