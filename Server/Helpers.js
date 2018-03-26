module.exports = {};

module.exports.randomFloat = (seed) =>{
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

module.exports.hasDuplicates = (array) =>{
  return (new Set(array)).size !== array.length;
};



module.exports.removeValueFromArray = (array, value) =>{
  let index = array.indexOf(value);

  if(index !== -1){
    array.splice(index, 1);
  }
};

