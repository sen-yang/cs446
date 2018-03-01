var genUUID = require('uuid/v1');
var User = require('./User');

module.exports = class GameState{
  constructor(ws){
    this.id = genUUID();
    this.user = new User(genUUID());
    this.ws = ws;
    this.currentRoom = null;
  }
};