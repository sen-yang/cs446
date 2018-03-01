const genUUID = require('uuid/v1');
const GameState = require('./GameState');
const Constants = require('../constants');
const Helpers = require('../Helpers');
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

  initGame(){
    let websocketMessage = new WebsocketMessage(Constants.messageType.GAME_INIT);
    websocketMessage.gameState = gameState;
    let messageString = JSON.stringify(websocketMessage);
    this.clientList.forEach((client)=>{
      client.ws.send(messageString);
    });

    setTimeout(()=>{
      this.gameState.startGame(this.gameStateUpdated);
    }, Constants.GAME_READY_TIME);
  }

  gameStateUpdated(reason){
    let websocketMessage;
    switch(reason){
      case Constants.messageType.GAME_STATE_UPDATE:
        websocketMessage = new WebsocketMessage(Constants.messageType.GAME_STATE_UPDATE);
        websocketMessage.gameState = gameState;
        this.clientList.forEach((client)=>{
          client.ws.send(messageString);
        });
        break;
      case Constants.messageType.GAME_FINISH:
        websocketMessage = new WebsocketMessage(Constants.messageType.GAME_FINISH);
        websocketMessage.gameState = gameState;
        this.clientList.forEach((client)=>{
          client.ws.send(messageString);
        });
        break;
      case Constants.messageType.GAME_DROPPED_ITEM:
        websocketMessage = new WebsocketMessage(Constants.messageType.GAME_STATE_UPDATE);
        websocketMessage.gameState = gameState;
        this.clientList.forEach((client)=>{
          client.ws.send(messageString);
        });

        websocketMessage = new WebsocketMessage(Constants.messageType.GAME_DROPPED_ITEM);
        websocketMessage.droppedItem = this.gameState.generateDrop();
        this.clientList.forEach((client)=>{
          client.ws.send(messageString);
        });
        break;
    }
  }
};