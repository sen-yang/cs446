const Helpers = require('../Helpers');

module.exports = class DroppedItem{
  constructor(number, xPosition,ySpeed, itemType){
    this.number = number;
    this.xPosition = xPosition;
    this.yPosition = 0;
    this.ySpeed = ySpeed;
    this.itemType = itemType;
  }
};