module.exports = class GameState{
  constructor(seedString){
    this.targetNumber = 0;
    this.playerList = [];
    this.isComplete = false;
    this.winner = null;
    this.timeRemaining = 1000 * 60;
  }
};