module.exports = class User{
  constructor(UID, rank, rating, ratingdev, volatility){
    this.UID = UID;
    this.rankno = rank;
    this.rating = rating;
    this.ratingdev = ratingdev;
    this.volatility = volatility;
  }
};
