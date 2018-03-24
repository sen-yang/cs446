const Constants = require('./Constants');
const Helpers = require('./Helpers');
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
let clientsSearchingForRanked = [];
let clientsSearchingForGroup = [];

server.on('connection', function(ws){
  console.log('connected');
  let client = new WebsocketClient(ws);
  connectedClients[client.id] = client;

  ws.on('message', (message) =>{
    console.log('received: ', message);
    handleMessage(JSON.parse(message), client);
  });

  ws.on('close', () =>{
    console.log('connection is closed');
    clientDisconnected(client);
  });

  ws.on('error', () =>{
    console.log('connection error');
    clientDisconnected(client);
  });
});

function verifyClient(info){
  // console.log('verifyClient', info);
  //todo wat i do here...
  return true;
}

function clientDisconnected(client){
  delete connectedClients[client.id];

  for(var i = clientsSearchingForRanked.length; i >= 0; i--){
    if(clientsSearchingForRanked[i] == client){
      clientsSearchingForRanked.splice(i, 1);
      break;
    }
  }
  for(var i = clientsSearchingForGroup.length; i >= 0; i--){
    if(clientsSearchingForGroup[i] == client){
      clientsSearchingForGroup.splice(i, 1);
      break;
    }
  }

  if(client.user != null){
    usernamesSet.delete(client.user.username);
  }
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

function findGame(message, client){
  switch(message.gameType){
    case Constants.gampeType.SINGLEPLAYER:
      createSinglePlayerGame(client);
      break;
    case Constants.gampeType.RANKED:
      searchForRanked(client);
      break;
    case Constants.gampeType.GROUP_GAME:
      createGroupGame(client);
      break;
  }
}

function searchForRanked(client1){
  if(client1.currentRoom != null){
    //already in room
  }
  else if(clientsSearchingForRanked.length > 0){
    let client2 = clientsSearchingForRanked[0];

    if(client1.id != client2.id){
      clientsSearchingForRanked.splice(0, 1);
      createRankedGame(client1, client2);
    }
  }
  else{
    clientsSearchingForRanked.push(client1);
  }
}

function searchForGroup(client1){
  let clientList = [client1];
  if(client1.currentRoom != null){
    //already in room
  }
  else if(clientsSearchingForRanked.length > 1){
    clientList.push(clientsSearchingForRanked[0]);
    clientList.push(clientsSearchingForRanked[1]);

    if(!Helpers.hasDuplicates(clientList)){
      clientsSearchingForGroup.splice(0, 2);
      createGroupGame(clientList);
    }
  }
  else{
    clientsSearchingForRanked.push(client1);
  }
}

function playerAction(message, client){
  let player = client.playerInCurrentRoom;

  if(player != null){
    player.doPlayerAction(message.playerAction);
  }

}

function createSinglePlayerGame(client1){
  let gameRoom = new GameRoom(0, [client1], (gameRoom) =>{
    gameFinished(gameRoom);
  });
  gameRoom.initGame();
  gameRooms[gameRoom.id] = gameRoom;
}

function createRankedGame(client1, client2){
  let clientList = [];
  clientList.push(client1);
  clientList.push(client2);
  let gameRoom = new GameRoom(0, clientList, (gameRoom) =>{
    gameFinished(gameRoom);
  });
  gameRoom.initGame();
  gameRooms[gameRoom.id] = gameRoom;
}

function createGroupGame(newClientList){
  let gameRoom = new GameRoom(0, newClientList, (gameRoom) =>{
    gameFinished(gameRoom);
  });
  gameRoom.initGame();
  gameRooms[gameRoom.id] = gameRoom;
}

function gameFinished(gameRoom){
  delete gameRooms[gameRoom.id];
}
