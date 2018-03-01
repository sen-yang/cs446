const Constants = require('./constants');
const WebSocketServer = require('ws').Server;
const genUUID = require('uuid/v1');
const WebsocketClient = require('./Models/WebsocketClient');

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
      break;
  }
}

function loginUser(message, client){
  if((message.username != null) && (!usernamesSet.has(message.username))){
    client.user = {username: message.username}
  }
}

function findGame(message, client1){
  if(clientsSearchingForGame.length > 0){
    client2 = clientsSearchingForGame[Object.keys(clientsSearchingForGame)[0]];

    if(client1.id != client2.id){
      delete clientsSearchingForGame[client2.id];
      createGame(client1, client2);
    }
  }
}

function playerAction(message, client){
}

function createGame(client1, client2){

}