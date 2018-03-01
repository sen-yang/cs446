var ranGen = require('random-seed').create();

randomTimeSendRandomInt();



function randomTimeSendRandomInt(minimumTime ,timerange){
  ranGen.seed();
  var timeSend = ranGen.range(timerange) + minimumTime;
  var randomINT = ranGen.range(10);
  console.log(randomINT);
  setTimeout(randomTimeSendRandomInt, timeSend);
}

function generateRandom(seed){
    ranGen.seed(seed);
    return ranGen.range(10);
}
