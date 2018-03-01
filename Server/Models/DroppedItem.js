const Helpers = require('../Helpers');

module.exports = class DroppedItem{
  constructor(number, xPosition, yPosition){
    this.number = number;
    this.xPosition = xPosition;
    this.yPosition = 0;
    this.ySpeed = Helpers.randomNumberInRange(0.005, 0.02, false);
  }
};