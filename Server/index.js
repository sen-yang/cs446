const Constants = require('./Constants');
const Helpers = require('./Helpers');
const WebSocketServer = require('ws').Server;
const genUUID = require('uuid/v1');
const WebsocketClient = require('./Models/WebsocketClient');
const GameRoom = require('./Models/GameRoom');
const WebsocketMessage = require('./Models/WebsocketMessage');
const User = require('./Models/User');
const ConnectionController = require('./ConnectionController');

const config = {
  host: "0.0.0.0",
  port: Constants.PORT_NUMBER,
  verifyClient: verifyClient
};
const dbController = new ConnectionController();
const server = new WebSocketServer(config, function(){
  console.log('Server started on port', Constants.PORT_NUMBER);
});

let connectedClients = [];
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

  ws.on('error', (error) =>{
    console.log('connection error: ' + error);
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
}

// client message handlers

function handleMessage(message, client){
  switch(message.type){
    case Constants.MESSAGE_TYPE.PING:
      break;

    //client messages
    case Constants.MESSAGE_TYPE.LOGIN:
      loginUser(message, client);
      break;
    case Constants.MESSAGE_TYPE.REGISTER:
      registerUser(message, client);
      break;
    case Constants.MESSAGE_TYPE.UPDATE_USER:
      updateUser(message, client);
      break;
    case Constants.MESSAGE_TYPE.CREATE_USER:
      Register(message, client);
      break;
    case Constants.MESSAGE_TYPE.GET_RANKINGS:
      getRankings(message, client);
      break;
    case Constants.MESSAGE_TYPE.FIND_GAME:
      findGame(message, client);
      break;
    case Constants.MESSAGE_TYPE.PLAYER_ACTION:
      playerAction(message, client);
      break;
  }
}

function registerUser(message, client){
  let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
  websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
  websocketMessage.isConfirmed = false;

  if(!Helpers.isNonEmptyString(message.username) || (message.username.length < 3)){
    websocketMessage.errorMessage = "Username needs to be at least 3 characters long.";
    client.sendMessage(JSON.stringify(websocketMessage));
  }
  else if(!Helpers.isNonEmptyString(message.password) || (message.password.length < 8)){
    websocketMessage.errorMessage = "Password needs to be at least 8 characters long.";
    client.sendMessage(JSON.stringify(websocketMessage));
  }
  else if(Helpers.isNonEmptyString(message.username) && Helpers.isNonEmptyString(message.password)){
    dbController.Register(message.username, message.password, "", (user) =>{
      let newSessionID = genUUID();
      dbController.UpdateSessionID(user.username, newSessionID, (user)=>{
        //login success
        let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
        websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
        websocketMessage.isConfirmed = true;
        websocketMessage.sessionID = newSessionID;
        client.user = user;
        websocketMessage.user = user;
        client.sendMessage(JSON.stringify(websocketMessage));
      },(error)=>{//session id create error
        let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
        websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
        websocketMessage.isConfirmed = false;
        websocketMessage.errorMessage = "Server error";
        client.sendMessage(JSON.stringify(websocketMessage));
      });
    }, (error) =>{
      websocketMessage.errorMessage = error;
      client.sendMessage(JSON.stringify(websocketMessage));
    });
    client.user = new User(message.username);
    websocketMessage.user = client.user;
  }
  else{
    websocketMessage.errorMessage = "Bad username or password.";
    client.sendMessage(JSON.stringify(websocketMessage));
  }
}

function loginUser(message, client){
  if(Helpers.isNonEmptyString(message.sessionID)){//sign in with session id
    dbController.LoginViaSessionID(message.sessionID, (user)=>{
      //login success
      let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
      websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
      websocketMessage.isConfirmed = true;
      client.user = user;
      websocketMessage.sessionID = user.sessionID;
      websocketMessage.user = user;
      client.sendMessage(JSON.stringify(websocketMessage));
    },(error)=>{
      //login error
      let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
      websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
      websocketMessage.isConfirmed = false;
      websocketMessage.errorMessage = "Invalid sessionID";
      client.sendMessage(JSON.stringify(websocketMessage));
    });
  }
  else if(Helpers.isNonEmptyString(message.username) && Helpers.isNonEmptyString(message.password)){
    //sign in with username and password
    dbController.Login(message.username, message.password, (user)=>{
      client.user = user;
      let newSessionID = genUUID();
      dbController.UpdateSessionID(user.username, newSessionID, (user)=>{
        //login success
        let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
        websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
        websocketMessage.isConfirmed = true;
        websocketMessage.sessionID = newSessionID;
        client.user = user;
        websocketMessage.user = user;
        client.sendMessage(JSON.stringify(websocketMessage));
      },(error)=>{//session id create error
        let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
        websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
        websocketMessage.isConfirmed = false;
        websocketMessage.errorMessage = "Server error";
        client.sendMessage(JSON.stringify(websocketMessage));
      });
    },(error)=>{
      //user login error
      let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
      websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
      websocketMessage.isConfirmed = false;
      websocketMessage.errorMessage = "Wrong username or password";
      client.sendMessage(JSON.stringify(websocketMessage));
    });
  }
  else if(Helpers.isNonEmptyString(message.username)){
    //create temporary user
    client.user = new User(message.username);
    client.user.isTemporary = true;
    let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
    websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
    websocketMessage.isConfirmed = true;
    websocketMessage.user = client.user;
    client.sendMessage(JSON.stringify(websocketMessage));
  }
  else{
    //login error
    let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
    websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
    websocketMessage.isConfirmed = false;
    websocketMessage.errorMessage = "Bad credentials";
    client.sendMessage(JSON.stringify(websocketMessage));
  }
}

function getRankings(message, client){
  dbController.selectRankings(message.limit, message.offset, (userList) =>{
    let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.GET_RANKINGS);
    websocketMessage.userList = userList;
    client.sendMessage(JSON.stringify(websocketMessage));
  }, (error) =>{
    //error
  });
}

function updateUser(message, client){
  if((client.user != null) && (message.user != null) && (client.user.characterSprite != message.user.characterSprite)){
    if(!client.user.isTemporary){
      //is valid user
      dbController.updateCharSprite(client.user.username, message.user.characterSprite, (user) =>{
        client.user = user;
        //update success
        let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
        websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
        websocketMessage.isConfirmed = true;
        websocketMessage.user = client.user;
        console.log("asdf", user);
        websocketMessage.sessionID = user.sessionID;
        client.sendMessage(JSON.stringify(websocketMessage));
      }, (error) =>{
        //update failed
        let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
        websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
        websocketMessage.isConfirmed = false;
        websocketMessage.errorMessage = error;
        client.sendMessage(JSON.stringify(websocketMessage));
      });
    }
    else{
      //is temporary user
      client.user.characterSprite = message.user.characterSprite;

      let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.CONFIRMATION);
      websocketMessage.confirmationType = Constants.CONFIRMATION_TYPE.USER_CONFIRMATION;
      websocketMessage.isConfirmed = true;
      websocketMessage.user = client.user;
      client.sendMessage(JSON.stringify(websocketMessage));
    }
  }
}

function findGame(message, client){
  switch(message.gameType){
    case Constants.GAME_TYPE.SINGLEPLAYER:
      createSinglePlayerGame(client);
      break;
    case Constants.GAME_TYPE.RANKED:
      searchForRanked(client);
      break;
    case Constants.GAME_TYPE.GROUP_GAME:
      searchForGroup(client);
      break;
    case Constants.GAME_TYPE.CANCEL:
      clearSearchingClient(client);
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
      client1.isSearchingForMatch = false;
      client2.isSearchingForMatch = false;
    }
  }
  else{
    clientsSearchingForRanked.push(client1);
    client1.isSearchingForMatch = true;
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
      clientList.forEach((client) =>{
        client.isSearchingForMatch = false;
      });
      clientsSearchingForGroup.splice(0, 2);
      createGroupGame(clientList);
    }
  }
  else{
    clientsSearchingForRanked.push(client1);
    client1.isSearchingForMatch = true;
  }
}

function playerAction(message, client){
  if((client.user != null) && (client.currentRoom != null) && (message.playerAction != null)){
    client.currentRoom.gameManager.playerActionPerformed(client.user.username, message.playerAction);
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

function clearSearchingClient(client){
  Helpers.removeValueFromArray(clientsSearchingForRanked, client);
  Helpers.removeValueFromArray(clientsSearchingForGroup, client);
}
