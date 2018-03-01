const Constants = require('./constants');
const WebSocketServer = require('ws').Server;
const genUUID = require('uuid/v1');
const WebsocketClient = require('./Models/WebsocketClient');
const GameRoom = require('./Models/GameRoom');
const WebsocketMessage = require('./Models/WebsocketMessage');

const config = {
  host: "0.0.0.0",
  port: Constants.PORT_NUMBER,
  verifyClient: verifyClient
};
const server = new WebSocketServer(config, function(){
  console.log('Server started on port', Constants.PORT_NUMBER);
});

let connectedClients = {};
let usernamesSet = new Set();
let gameRooms = {};
let clientsSearchingForGame = {};

server.on('connection', function(ws){
  let client = new WebsocketClient(ws);
  connectedClients[client.id] = client;

  ws.on('message', function(message){
    console.log('received: ', message);
    handleMessage(JSON.parse(message), client);
  });

  ws.on('close', function(){
    console.log('connection is closed');
    delete connectedClients[client.id];

    if(client.user != null){
      usernamesSet.delete(client.user.username);
    }
  });
});

function verifyClient(info){
  // console.log('verifyClient', info);
  //todo
  return true;
}


// client message handlers

function handleMessage(message, client){
  switch(message.type){
    case Constants.messageType.PING:
      break;

    //client messages
    case Constants.messageType.LOGIN:
      loginUser(message, client);
      break;
    case Constants.messageType.FIND_GAME:
      findGame(message, client);
      break;
    case Constants.messageType.PLAYER_ACTION:
      playerAction(message, client);
      break;
  }
}

function loginUser(message, client){
  let confirmed = false;

  if((message.username != null) && (!usernamesSet.has(message.username))){
    client.user = {username: message.username};
    confirmed = true;
  }
  let websocketMessage = new WebsocketMessage(Constants.messageType.LOGIN_CONFIRMATION);
  websocketMessage.isConfirmed = confirmed;
  client.ws.send(JSON.stringify(websocketMessage));
}

function findGame(message, client1){
  if(clientsSearchingForGame.length > 0){
    client2 = clientsSearchingForGame[Object.keys(clientsSearchingForGame)[0]];

    if(client1.id != client2.id){
      delete clientsSearchingForGame[client2.id];
      createGame(client1, client2);
    }
  }
  else{
    clientsSearchingForGame[client1.id] = client1;
  }
}

function playerAction(message, client){
  let player = client.getPlayerInCurrentRoom();

  if(player != null){
    player.doPlayerAction(message.playerAction);
  }

}

function createGame(client1, client2){
  //todo game types not implemented
  let clientList = {};
  clientList[client1.id] = client1;
  clientList[client2.id] = client2;
  let gameRoom = new GameRoom(0, clientList,genUUID());
  gameRoom.initGame();
}