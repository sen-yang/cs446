const Player = require('./Player');
const DroppedItem = require('./DroppedItem');
const Constants = require('../constants');
const Helpers = require('../Helpers');

module.exports = class GameState{

  constructor(seedString, clientList){
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
    this.gameLoop = null;
    this.updateCallback = null;

    this.playerList = [];
    clientList.forEach((client) =>{
      this.playerList.push(new Player(this.targetNumber, client));
    });
  }

  startGame(updateCallback){
    this.startTime = Date.now();
    this.previousTickTime = this.startTime;
    this.updateCallback = updateCallback;
    this.tick();
  }

  tick(){
    this.gameLoop = setTimeout(()=>{
      this.updateCallback(this.updateGame());

      if(!this.isComplete){
        this.tick();
      }
    }, Constants.TICK_TIME);
  }

  updateGame(){
    let now = Date.now();
    this.delta = now - this.previousTickTime;
    this.previousTickTime = now;
    this.timeRemaining -= this.delta;

    this.playerList.every((player) =>{
      if(player.currentNumber == this.targetNumber){
        this.isComplete = true;
        this.winner = player;
        return false;
      }
      else if(player.lost){
        this.isComplete = true;
        this.loser = player;
        return false;
      }
      return true;
    });

    if((this.winner == null) && (this.loser == null) && (this.timeRemaining <= 0)){
      this.isComplete = true;

      let minDifference = Number.MAX_VALUE;
      let minPlayer = null;
      this.playerList.forEach((player)=>{
        let difference = Math.abs(this.targetNumber - player.currentNumber);

        if(difference < minDifference){
          minDifference = difference;
          minPlayer = player;
        }
      });
      this.winner = minPlayer;
    }

    if(this.isComplete){
      return Constants.messageType.GAME_FINISH;
    }
    if(Math.random() < (this.delta / 1500)){
      return Constants.messageType.GAME_DROPPED_ITEM;
    }
    else{
      return Constants.messageType.GAME_STATE_UPDATE;
    }
  }

  generateDrop(){
    return new DroppedItem(Helpers.randomNumberInRange(Constants.MIN_DROP, Constants.MAX_DROP, true, this.seed), Helpers.randomFloat(this.seed));
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

  clientLeft(client){
    this.isComplete = true;
    this.loser = client.getPlayerInCurrentRoom();
    //todo maybe shouldn't wait till next tick
  }
};