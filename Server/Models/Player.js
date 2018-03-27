const Constants = require('../Constants');

module.exports = class Player{
  constructor(targetNumber, client){
    this.targetNumber = targetNumber;
    this.currentNumber = 0;
    this.currentOperation = Constants.PLAYER_ACTION_TYPE.ADDITION;
    this.lost = false;
    this.itemInInventory = null;
    this.username = client.user.username;
    this.characterSprite = client.user.characterSprite;
  }

  doPlayerAction(playerAction, itemUsedCallback){
    switch(playerAction.commandType){
      case Constants.PLAYER_ACTION_TYPE.ADDITION:
      case Constants.PLAYER_ACTION_TYPE.SUBTRACTION:
      case Constants.PLAYER_ACTION_TYPE.MULTIPLICATION:
      case Constants.PLAYER_ACTION_TYPE.DIVISION:
        this.currentOperation = playerAction.commandType;
        break;
      case Constants.PLAYER_ACTION_TYPE.GET_NUMBER:
        this.updateCurrentNumber(playerAction.value);
        break;
      case Constants.PLAYER_ACTION_TYPE.USE_ITEM:
        if(this.itemInInventory != null){
          itemUsedCallback(this.itemInInventory.itemType);
        }
        break;
      case Constants.PLAYER_ACTION_TYPE.GET_ITEM:
        this.getItem(playerAction.item);
        break;
    }
  }

  updateCurrentNumber(newNumber){
    switch(this.currentOperation){
      case Constants.PLAYER_ACTION_TYPE.ADDITION:
        this.currentNumber += newNumber;
        break;
      case Constants.PLAYER_ACTION_TYPE.SUBTRACTION:
        this.currentNumber -= newNumber;
        break;
      case Constants.PLAYER_ACTION_TYPE.MULTIPLICATION:
        this.currentNumber *= newNumber;
        break;
      case Constants.PLAYER_ACTION_TYPE.DIVISION:
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
    return {
      currentNumber: this.currentNumber,
      username: this.username,
      characterSprite: this.characterSprite,
      itemInInventory: this.itemInInventory
    };
  }
};