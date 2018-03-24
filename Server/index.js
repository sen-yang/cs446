const Constants = require('./constants');
const WebSocketServer = require('ws').Server;
const genUUID = require('uuid/v1');
const WebsocketClient = require('./Models/WebsocketClient');
const GameRoom = require('./Models/GameRoom');
const WebsocketMessage = require('./Models/WebsocketMessage');
const User = require('./Models/User');

const config = {
  host: "0.0.0.0",
  port: Constants.PORT_NUMBER,
  verifyClient: verifyClient
};
const server = new WebSocketServer(config, function(){
  console.log('Server started on port', Constants.PORT_NUMBER);
});

let connectedClients = [];
let usernamesSet = new Set();
let gameRooms = {};
let clientsSearchingForGame = [];

server.on('connection', function(ws){
  console.log('connected');
  let client = new WebsocketClient(ws);
  connectedClients[client.id] = client;

  ws.on('message', function(message){
    console.log('received: ', message);
    handleMessage(JSON.parse(message), client);
  });

  ws.on('close', function(){
    console.log('connection is closed');
    delete connectedClients[client.id];

    for(var i = clientsSearchingForGame.length; i >= 0; i--){
      if(clientsSearchingForGame[i] == client){
        clientsSearchingForGame.splice(i, 1);
        break;
      }
    }

    if(client.user != null){
      usernamesSet.delete(client.user.username);
    }
  });
});

function verifyClient(info){
  // console.log('verifyClient', info);
  //todo wat i do here...
  return true;
}


// client message handlers

function handleMessage(message, client){
  switch(+message.type){
    case Constants.messageType.PING:
      break;

    //client messages
    case Constants.messageType.LOGIN:
      loginUser(message, client);
      break;
    case Constants.messageType.CREATE_USER:
      Register(message, client);
    break;
    case Constants.messageType.GET_RANKINGS:
      getRankings(message, client);
      break;
    case Constants.messageType.FIND_GAME:
      findGame(message, client);
      break;
    case Constants.messageType.PLAYER_ACTION:
      playerAction(message, client);
      break;
  }
}

function Register(message, client){
  
}

function loginUser(message, client){
  let confirmed = false;

  if((message.username != null)){// && (!usernamesSet.has(message.username))){
    client.user = new User("", message.username);
    confirmed = true;
  }
  let websocketMessage = new WebsocketMessage(Constants.messageType.LOGIN_CONFIRMATION);
  websocketMessage.isConfirmed = confirmed;
  websocketMessage.user = client.user;
  client.sendMessage(JSON.stringify(websocketMessage));
}

function getRankings(message, client){
  //todo get rankings
}

function findGame(message, client1){
  createSinglePlayerGame(client1);
  // if(client1.currentRoom != null){
  //   //already in room
  // }
  // else if(clientsSearchingForGame.length > 0){
  //   client2 = clientsSearchingForGame[0];
  //
  //   if(client1.id != client2.id){
  //     clientsSearchingForGame.splice(0, 1);
  //     createGame(client1, client2);
  //   }
  // }
  // else{
  //   clientsSearchingForGame.push(client1);
  // }
}

function playerAction(message, client){
  let player = client.playerInCurrentRoom;

  if(player != null){
    player.doPlayerAction(message.playerAction);
  }

}

function createSinglePlayerGame(client1){
  let clientList = [];
  clientList.push(client1);
  let gameRoom = new GameRoom(0, clientList, (gameRoom) =>{
    gameFinished(gameRoom);
  });
  gameRoom.initGame();
  gameRooms[gameRoom.id] = gameRoom;
}

function createGame(client1, client2){
  //todo game types not implemented
  let clientList = [];
  clientList.push(client1);
  clientList.push(client2);
  let gameRoom = new GameRoom(0, clientList, (gameRoom) =>{
    gameFinished(gameRoom);
  });
  gameRoom.initGame();
  gameRooms[gameRoom.id] = gameRoom;
}

function gameFinished(gameRoom){
  delete gameRooms[gameRoom.id];
}
