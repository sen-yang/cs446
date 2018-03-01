const genUUID = require('uuid/v1');

odule.exports = class GameState{
  constructor(gameType,clientList, gameSeed){
    this.gameType = gameType;
    this.clientList = clientList;
    this.id = genUUID();
    this.gameState = new GameState(gameSeed);
  }
};