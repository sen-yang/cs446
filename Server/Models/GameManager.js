const Player = require('./Player');
const DroppedItem = require('./DroppedItem');
const GameRoom = require('./GameRoom');
const GameState = require('./GameState');
const Constants = require('../constants');
const Helpers = require('../Helpers');

module.exports = class GameManager{

  constructor(seedString, clientList){
    //todo generate from seed
    this.seed = seedString;
    this.gameLoop = null;
    this.updateCallback = null;
    let playerList = [];
    this.gameState = new GameState(seedString, playerList);
    clientList.forEach((client) =>{
      playerList.push(new Player(this.gameState.targetNumber, client));
    });
  }

  initGame(){

  }

  startGame(updateCallback){
    this.gameState.startTime = Date.now();
    this.gameState.previousTickTime = this.gameState.startTime;
    this.updateCallback = updateCallback;
    this.tick();
  }

  tick(){
    this.gameLoop = setTimeout(()=>{
      this.updateCallback(this.updateGame());

      if(!this.gameState.isComplete){
        this.tick();
      }
    }, Constants.TICK_TIME);
  }

  updateGame(){
    let now = Date.now();
    this.gameState.delta = now - this.gameState.previousTickTime;
    this.gameState.previousTickTime = now;
    this.gameState.timeRemaining -= this.gameState.delta;

    this.gameState.playerList.every((player) =>{
      if(player.currentNumber == this.gameState.targetNumber){
        this.gameState.isComplete = true;
        this.gameState.winner = player;
        return false;
      }
      else if(player.lost){
        this.gameState.isComplete = true;
        this.gameState.loser = player;
        return false;
      }
      return true;
    });

    if((this.gameState.winner == null) && (this.gameState.loser == null) && (this.gameState.timeRemaining <= 0)){
      this.gameState.isComplete = true;

      let minDifference = Number.MAX_VALUE;
      let minPlayer = null;
      this.gameState.playerList.forEach((player)=>{
        let difference = Math.abs(this.gameState.targetNumber - player.currentNumber);

        if(difference < minDifference){
          minDifference = difference;
          minPlayer = player;
        }
      });
      this.gameState.winner = minPlayer;
    }

    if(this.gameState.isComplete){
      return Constants.messageType.GAME_FINISH;
    }
    if(Math.random() < (this.gameState.delta / 1500)){
      return Constants.messageType.GAME_DROPPED_ITEM;
    }
    else{
      return Constants.messageType.GAME_STATE_UPDATE;
    }
  }

  generateDrop(){
    return new DroppedItem(Helpers.randomNumberInRange(Constants.MIN_DROP, Constants.MAX_DROP, true, this.seed), Helpers.randomFloat(this.seed));
  }

  clientLeft(client){
    this.gameState.isComplete = true;
    this.gameState.loser = client.getPlayerInCurrentRoom();
    //todo maybe shouldn't wait till next tick
  }
};