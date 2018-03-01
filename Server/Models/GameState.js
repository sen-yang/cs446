const Player = require('./Player');
const Constants = require('../constants');

module.exports = class GameState{
  constructor(seedString, clientList){
    //todo generate from seed
    this.targetNumber = Math.round(Math.random() * 200) - 100;
    this.isComplete = false;
    this.winner = null;
    this.loser = null;
    this.timeRemaining = 1000 * 60;
    this.previousTickTime = 0;
    this.delta = 0;
    this.startTime = 0;
    this.gameLoop = null;

    this.playerList = {};
    clientList.forEach((client) =>{
      this.playerList[client.id] = new Player(this.targetNumber, client.id);
    });
  }

  startGame(updateCallback){
    this.startTime = Date.now();
    this.previousTickTime = this.startTime;
    this.gameLoop = new DeltaTimer(()=>{
      updateCallback(this.updateGame());
    }, Constants.TICK_TIME);
  }

  updateGame(){
    let now = Date.now();
    this.delta = now - this.previousTickTime;
    this.previousTickTime = now;

    let winner;
    let loser;
    this.playerList.forEach((player) =>{
      if(player.currentNumber == this.targetNumber){
        this.isComplete = true;
        this.winner = player;
      }
      else if(player.lost){
        this.isComplete = true;
        this.loser = player;
      }
    });

    if(this.gameFinished){
      return Constants.messageType.GAME_FINISH;
    }
    if(Math.random() > this.delta / 1000){
      return Constants.messageType.GAME_DROPPED_ITEM;
    }
    else{
      return Constants.messageType.GAME_STATE_UPDATE;
    }
  }
};