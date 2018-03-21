const genUUID = require('uuid/v1');
const GameManager = require('./GameManager');
const Constants = require('../constants');
const Helpers = require('../Helpers');
const WebsocketMessage = require('./WebsocketMessage');

module.exports = class GameRoom{
  constructor(gameType,clientList, roomClearedCallback){
    this.gameType = gameType;
    this.clientList = clientList;
    this.id = genUUID();
    this.gameManager = new GameManager(genUUID(), clientList);
    this.roomClearedCallback = roomClearedCallback;
    this.clientList.forEach((client)=>{
      client.currentRoom = this;
    });
  }

  initGame(){
    let websocketMessage = new WebsocketMessage(Constants.messageType.GAME_INIT);
    websocketMessage.gameState = this.gameManager.gameState;
    let messageString = JSON.stringify(websocketMessage);
    this.clientList.forEach((client)=>{
      console.log("asdf", client.user.username, messageString);
      client.sendMessage(messageString);
    });

    setTimeout(()=>{
      this.gameManager.startGame((reason)=>{
        this.gameStateUpdated(reason);
      });
    }, Constants.GAME_READY_TIME);
  }

  gameStateUpdated(reason){
    let websocketMessage;
    switch(reason){
      case Constants.messageType.GAME_STATE_UPDATE:
        websocketMessage = new WebsocketMessage(Constants.messageType.GAME_STATE_UPDATE);
        websocketMessage.gameState = this.gameManager.gameState;
        this.clientList.forEach((client)=>{
          if(!client.sendMessage(JSON.stringify(websocketMessage))){
            this.clientDropped(client);
          }
        });
        break;
      case Constants.messageType.GAME_FINISH:
        websocketMessage = new WebsocketMessage(Constants.messageType.GAME_FINISH);
        websocketMessage.gameState = this.gameManager.gameState;
        this.clientList.forEach((client)=>{
          if(!client.sendMessage(JSON.stringify(websocketMessage))){
            this.clientDropped(client);
          }
        });
        this.clearRoom();
        break;
      case Constants.messageType.GAME_DROPPED_ITEM:
        websocketMessage = new WebsocketMessage(Constants.messageType.GAME_STATE_UPDATE);
        websocketMessage.gameState = this.gameManager.gameState;
        this.clientList.forEach((client)=>{
          if(!client.sendMessage(JSON.stringify(websocketMessage))){
            this.clientDropped(client);
          }
        });

        websocketMessage = new WebsocketMessage(Constants.messageType.GAME_DROPPED_ITEM);
        websocketMessage.droppedItem = this.gameManager.generateDrop();
        this.clientList.forEach((client)=>{
          if(!client.sendMessage(JSON.stringify(websocketMessage))){
            this.clientDropped(client);
          }
        });
        break;
    }
  }

  clientDropped(client){
    //todo implement client droppped
    this.this.gameManager.clientLeft(client);
  }

  clearRoom(){
    this.clientList.forEach((client)=>{
      client.leaveRoom();
    });
    this.roomClearedCallback(this);
  }
};