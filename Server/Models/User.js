const Rank = require('./Rank');
const Constants = require('../Constants');

module.exports = class User{
  constructor(username, isTemporary){
    this.UID = null;
    this.username = username;
    this.isTemporary = false;
    this.rank = new Rank();
    this.characterSprite = Constants.CHARACTER_SPRITE.BIRD_1;
  }

  toJSON(){
    if(this.rank != null){
      return {
        username: this.username,
        characterSprite: this.characterSprite,
        isTemporary: this.isTemporary,
        rankNumber: this.rank.rankno,
        rankRating: this.rank.rating
      };
    }
    else{
      return {
        username: this.username,
        characterSprite: this.characterSprite,
        isTemporary: this.isTemporary
      };
    }
  }
};
