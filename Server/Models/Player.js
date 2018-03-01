const Constants = require('../constants');

module.exports = class Player{
  constructor(targetNumber, clientId){
    this.targetNumber = targetNumber;
    this.currentNumber = 0;
    this.currentOperation = 0;
    this.lost = false;
    this.clientId = clientId;
  }

  doPlayerAction(playerAction){
    switch(+playerAction.commandType){
      case Constants.playerActionType.ADDITION:
      case Constants.playerActionType.SUBTRACTION:
      case Constants.playerActionType.MULTIPLICATION:
      case Constants.playerActionType.DIVISION:
        player.currentOperation = +playerAction.commandType;
        break;
      case Constants.playerActionType.GET_NUMBER:
        player.updateCurrentNumber(playerAction.value);
        break;
      case Constants.playerActionType.USE_POWER_UP:
        break;
    }
  }

  updateCurrentNumber(newNumber){
    switch(this.currentOperation){
      case Constants.playerActionType.ADDITION:
        this.currentNumber += newNumber;
        break;
      case Constants.playerActionType.SUBTRACTION:
        this.currentNumber -= newNumber;
        break;
      case Constants.playerActionType.MULTIPLICATION:
        this.currentNumber *= newNumber;
        break;
      case Constants.playerActionType.DIVISION:
        if(newNumber == 0){
          this.lost = true;
          return;
        }
        this.currentNumber /= newNumber;
        break;
    }
  }

  toJSON(){
    return {currentNumber: this.currentNumber};
  }
};