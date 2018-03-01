module.exports = class GameState{
  constructor(seedString, playerList){
    this.targetNumber = ;
    this.playerList = [];
    this.isComplete = false;
    this.winner = null;
    this.timeRemaining = 1000 * 60;
  }
};