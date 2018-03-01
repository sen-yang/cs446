const genUUID = require('uuid/v1');
const GameState = require('./GameState');
const Constants = require('../constants');
const WebsocketMessage = require('./WebsocketMessage');

module.exports = class GameRoom{
  constructor(gameType,clientList, gameSeed){
    this.gameType = gameType;
    this.clientList = clientList;
    this.id = genUUID();
    this.gameState = new GameState(gameSeed, clientList);
    this.clientList.forEach((client)=>{
      client.currentRoom = this;
    });
  }

  startGame(){
    let websocketMessage = new WebsocketMessage(Constants.messageType.GAME_START);
    websocketMessage.gameState = gameState;
    let messageString = JSON.stringify(websocketMessage);
    this.clientList.forEach((client)=>{
      client.ws.send(messageString);
    });

    setTimeout()
  }
};