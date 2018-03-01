module.exports = {};

module.exports.randomFloat = (seed)=>{
  //todo implement seed
  return Math.random();
};

module.exports.randomNumberInRange = (min, max, shouldRound = true, seed = null) =>{
  let number = module.exports.randomFloat(seed) * (max - min) + min;

  if(shouldRound){
    return Math.round(number);
  }
  else{
    return number;
  }
};