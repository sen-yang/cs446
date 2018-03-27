const Player = require('./Player');
const DroppedItem = require('./DroppedItem');
const Constants = require('../Constants');
const Helpers = require('../Helpers');

module.exports = class GameState{

  constructor(seedString, playerList){
    //todo generate from seed
    this.seed = seedString;
    this.targetNumber = Helpers.randomNumberInRange(Constants.MIN_TARGET, Constants.MAX_TARGET, true, this.seed);
    this.isComplete = false;
    this.winner = null;
    this.loser = null;
    this.timeRemaining = 1000 * 60;
    this.previousTickTime = 0;
    this.delta = 0;
    this.startTime = 0;
    this.playerList = playerList;
    this.droppedItemList = [];
    this.globalEffect = null;
    this.globalEffectTimeRemaining = 0;
  }

  setGlobalEffect(itemEffect){
    this.globalEffectTimeRemaining = Constants.ITEM_TYPE.GLOBAL_ITEM_EFFECT_DURATION;
    this.globalEffect = itemEffect;
  }

  getWinner(){
    let winner = this.winner;

    if((winner == null) && this.isComplete && (this.playerListlength != null) && (this.playerListlength > 1)){
      this.playerList.every((player) =>{
        if(player != this.loser){
          winner = player;
          return false;
        }
        return true;
      });
    }
    return winner;
  }

  toJSON(){
    return {
      targetNumber: this.targetNumber,
      playerList: this.playerList,
      isComplete: this.isComplete,
      winner: this.getWinner(),
      timeRemaining: this.timeRemaining,
      droppedItemList: this.droppedItemList,
      globalEffect: this.globalEffect
    };
  }
};