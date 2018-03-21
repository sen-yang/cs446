const Constants = require('../constants');

module.exports = class Player{
  constructor(targetNumber, client){
    this.targetNumber = targetNumber;
    this.currentNumber = 0;
    this.currentOperation = 0;
    this.lost = false;
    this.itemInInventory = null;
    this.client = client;
    this.client.playerInCurrentRoom = this;
  }

  doPlayerAction(playerAction){
    switch(+playerAction.commandType){
      case Constants.playerActionType.ADDITION:
      case Constants.playerActionType.SUBTRACTION:
      case Constants.playerActionType.MULTIPLICATION:
      case Constants.playerActionType.DIVISION:
        this.currentOperation = +playerAction.commandType;
        break;
      case Constants.playerActionType.GET_NUMBER:
        this.updateCurrentNumber(playerAction.value);
        break;
      case Constants.playerActionType.USE_ITEM:
        //todo
        break;
      case Constants.playerActionType.GET_ITEM:
        this.getItem(playerAction.item);
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

  getItem(item){
    this.itemInInventory = item;
  }

  toJSON(){
    return {currentNumber: this.currentNumber,
            username: this.client.user.username};
  }
};