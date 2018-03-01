const genUUID = require('uuid/v1');
const GameState = require('./GameState');

module.exports = class GameRoom{
  constructor(gameType,clientList, gameSeed){
    this.gameType = gameType;
    this.clientList = clientList;
    this.id = genUUID();
    this.gameState = new GameState(gameSeed, clientList);
  }
};