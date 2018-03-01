const Constants = require('../constants');

module.exports = class Player{
  constructor(targetNumber){
    this.targetNumber = targetNumber;
    this.currentNumber = 0;
    this.currentOperation = 0;
  }

  //return -1 if divide by 0
  //return -2 if invalid
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
          return -1;
        }
        this.currentNumber /= newNumber;
        break;
      default:
        return -2;
    }
    return 0;
  }
};