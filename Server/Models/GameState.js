const Player = require('./Player');
const DroppedItem = require('./DroppedItem');
const Constants = require('../Constants');
const Helpers = require('../Helpers');

module.exports = class GameState{

  constructor(seedString, playerList){
    //todo generate from seed
    this.seed = seedString;
    this.targetNumber = Math.round(Math.random() * 200) - 100;
    this.isComplete = false;
    this.winner = null;
    this.loser = null;
    this.timeRemaining = 1000 * 60;
    this.previousTickTime = 0;
    this.delta = 0;
    this.startTime = 0;
    this.playerList = playerList;
  }

  toJSON(){
    let winner = this.winner;

    if(winner == null && this.isComplete){
      this.playerList.every((player) =>{
        if(player != this.loser){
          winner = player;
          return false;
        }
        return true;
      });
    }
    return {targetNumber: this.targetNumber,
            playerList: this.playerList,
            isComplete: this.isComplete,
            winner: winner,
            timeRemaining: this.timeRemaining};
  }
};