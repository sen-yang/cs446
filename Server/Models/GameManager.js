const Player = require('./Player');
const DroppedItem = require('./DroppedItem');
const GameState = require('./GameState');
const Constants = require('../Constants');
const Helpers = require('../Helpers');

module.exports = class GameManager{

  constructor(seedString, clientList){
    //todo generate from seed
    this.seed = seedString;
    this.gameLoop = null;
    this.updateCallback = null;
    let playerList = [];
    this.gameState = new GameState(seedString, playerList);

    if(clientList != null){
      clientList.forEach((client) =>{
        playerList.push(new Player(this.gameState.targetNumber, client));
      });
    }
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
    this.gameLoop = setTimeout(() =>{
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

    if(this.gameState.globalEffectTimeRemaining > 0){
      this.gameState.globalEffectTimeRemaining -= this.gameState.delta;
    }
    if(this.gameState.globalEffectTimeRemaining <= 0){
      this.gameState.globalEffect = null;
    }

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

      if(this.gameState.playerList.length > 1){
        this.gameState.playerList.forEach((player) =>{
          let difference = Math.abs(this.gameState.targetNumber - player.currentNumber);

          if(difference < minDifference){
            minDifference = difference;
            minPlayer = player;
          }
        });
      }
      this.gameState.winner = minPlayer;
    }

    if(this.gameState.isComplete){
      return Constants.MESSAGE_TYPE.GAME_FINISH;
    }
    this.gameState.droppedItemList = [];

    if(this.gameState.globalEffect == Constants.ITEM_TYPE.SPEED_INCREASE){
      if(Helpers.randomFloat(this.seed) * (Constants.DROP_RATE + 50) > this.gameState.delta){
        this.gameState.droppedItemList.push(this.generateDrop());
      }
    }
    else{
      if(Helpers.randomFloat(this.seed) * Constants.DROP_RATE > this.gameState.delta){
        this.gameState.droppedItemList.push(this.generateDrop());
      }
    }
    return Constants.MESSAGE_TYPE.GAME_STATE_UPDATE;
  }

  generateDrop(){
    let dropSpeed = Helpers.randomNumberInRange(Constants.MIN_DROP_SPEED, Constants.MAX_DROP_SPEED, false);

    if(this.gameState.globalEffect == Constants.ITEM_TYPE.SPEED_INCREASE){
      dropSpeed *= 2;
    }
    if(Helpers.randomFloat(this.seed) < Constants.EFFECT_ITEM_DROP_CHANCE){
      return new DroppedItem(0, Helpers.randomFloat(this.seed), dropSpeed, Constants.ITEM_TYPE.SPEED_INCREASE);
    }
    else{
      return new DroppedItem(Helpers.randomNumberInRange(Constants.MIN_DROP, Constants.MAX_DROP, true, this.seed), Helpers.randomFloat(this.seed), dropSpeed, Constants.ITEM_TYPE.NUMBER);
    }
  }

  userLeft(username){
    this.gameState.isComplete = true;
    this.gameState.loser = this.findPlayerByUsername(username);
    //todo maybe shouldn't wait till next tick
  }

  playerActionPerformed(username, playerAction){
    this.findPlayerByUsername(username).doPlayerAction(playerAction, (itemUsed) =>{
      switch(itemUsed){
        case Constants.ITEM_TYPE.SPEED_INCREASE:
          this.gameState.setGlobalEffect(Constants.ITEM_TYPE.SPEED_INCREASE);
          break;
      }
    });
  }

  findPlayerByUsername(username){
    for(let index = 0; index < this.gameState.playerList.length; index++){
      let player = this.gameState.playerList[index];

      if(player.username === username){
        return player;
      }
    }
  }

  getRankedResults(resultsCallback){
    if((this.gameState.playerList != null) && (this.gameState.playerList.length == 2)){
      let winner = this.gameState.getWinner();
      let loser = null;

      this.gameState.playerList.forEach((player) =>{
        if(player != winner){
          loser = player;
        }
      });

      if((winner != null) && (loser != null) && (winner != loser)){
        resultsCallback(winner, loser);
      }
    }
  }
};