const Player = require('./Player');

module.exports = class GameState{
  constructor(seedString, clientList){
    //todo generate from seed
    this.targetNumber = Math.round(Math.random() * 200) - 100;
    this.isComplete = false;
    this.winner = null;
    this.timeRemaining = 1000 * 60;

    this.playerList = {};
    clientList.forEach((client) =>{
      this.playerList[client.id] = new Player(this.targetNumber);
    });
  }
};