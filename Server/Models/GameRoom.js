const genUUID = require('uuid/v1');
const GameManager = require('./GameManager');
const Constants = require('../Constants');
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
    let websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.GAME_INIT);
    websocketMessage.gameState = this.gameManager.gameState;
    let messageString = JSON.stringify(websocketMessage);
    this.clientList.forEach((client)=>{
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
      case Constants.MESSAGE_TYPE.GAME_STATE_UPDATE:
        websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.GAME_STATE_UPDATE);
        websocketMessage.gameState = this.gameManager.gameState;
        this.clientList.forEach((client)=>{
          if(!client.sendMessage(JSON.stringify(websocketMessage))){
            this.clientDropped(client);
          }
        });
        break;
      case Constants.MESSAGE_TYPE.GAME_FINISH:
        websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.GAME_FINISH);
        websocketMessage.gameState = this.gameManager.gameState;
        this.clientList.forEach((client)=>{
          if(!client.sendMessage(JSON.stringify(websocketMessage))){
            this.clientDropped(client);
          }
        });
        this.clearRoom();
        break;
    }
  }

  clientDropped(client){
    //todo check if need 2 do more :)))
    if(client.user != null){
      this.gameManager.userLeft(client.user.username);
    }
  }

  clearRoom(){
    this.clientList.forEach((client)=>{
      client.leaveRoom();
    });
    this.roomClearedCallback(this);
  }
};