const genUUID = require('uuid/v1');
const User = require('./User');

module.exports = class WebsocketClient{
  constructor(ws){
    this.id = genUUID();
    this.user = new User(genUUID());
    this.ws = ws;
    this.currentRoom = null;
  }
};