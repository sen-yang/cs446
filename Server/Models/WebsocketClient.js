const genUUID = require('uuid/v1');
const User = require('./User');

module.exports = class WebsocketClient{
  constructor(ws){
    this.id = genUUID();
    this.user = new User(genUUID());
    this.ws = ws;
    this.currentRoom = null;
  }

  getPlayerInCurrentRoom(){
    if(this.currentRoom != null){
      for(let index = 0; index < this.currentRoom.gameState.playerList.length; index++){
        if(this.currentRoom.gameState.playerList[index].clientId == this.id){
          return this.currentRoom.gameState.playerList[index];
        }
      }
    }
  }

  //returns false if failed
  sendMessage(message){
    if(this.ws.readyState === this.ws.OPEN){
      this.ws.send(message);
      return true;
    }
    else{
      return false;
    }
  }
};