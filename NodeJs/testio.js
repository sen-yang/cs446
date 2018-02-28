//////////////////////////required/////////////////////////////
const server = require('http').createServer();
const io = require('socket.io')(server, {
  // below are engine.IO options
  pingInterval: 10000,
  pingTimeout: 5000,
  cookie: false
});
///////////////////////////////////////////////////////
/////////////////////game collection to keep gameList and rooms//////////////////////////////////
var gameCollection =  new function() {

  this.totalgameCount = 0,
  this.gameList = []

};


///////////////////////////////////////////////////////
server.listen(3000);
var roomID = 0;
var connectedclients = [];
var inroomclients = [];


/////////////////////////connect//////////////////////////////
io.on('connection', function(socket){
  connectedclients.push(socket.id);
  console.log(connectedclients);
  console.log('a user connected');

  /////////////create room and make game//////////////////////////////////////////
  socket.on('makeGame', function () {
    makeGame(socket);
  });
  ////////////////////////////////////////////////////
  socket.on('searchGame', function(){
    searchGame(socket,findPlayerRoom(socket.id));
  });
  socket.on('startGame', function(){
    var j = findPlayerRoom(socket.id);
    startGame(socket, gameCollection.gameList[j].gameId);
  });
  socket.on('ComputeData', function(msg){
    computeAndDecide(socket, msg);
  });

  ///////////////////////////////////////////////////////
  ///////////////////////disconnect/////////////////////
  socket.on('disconnect', function(sockets){
    disconnectUser(socket);


  });
  ///////////////////////////////////////////////////////

});



//////////////////breaking into smaller function for better portability///////////////////////////////////



//////////////////disconnect user///////////////////////////////////////////////
function disconnectUser(socket){
  console.log('user disconnected');
  var index = connectedclients.indexOf(socket.id);
  connectedclients.splice(index, 1);
  console.log(connectedclients);
  var i = findPlayerRoom(socket.id);
  if(i !=-1){
    socket.leave(gameCollection.gameList[i].gameId);
    var a = findleftPlayer(socket.id);
    if(a == 1)
    gameCollection.gameList[i].playerOne = null;
    else {
      gameCollection.gameList[i].playerTwo = null;
    }
    if(gameCollection.gameList[i].playerTwo == null&& gameCollection.gameList[i].playerOne == null){
      console.log("Game room with no users, destroying room...")
      gameCollection.gameList.splice(i, 1);
    }
  }
}

///////////////////////////////////////////////////
function searchGame(socket){//search for a game
  var i = 0;
  var joined = false;
  var player;
  for(i = 0; i < gameCollection.gameList.length; i++){
    if(gameCollection.gameList[i].playerTwo == null || gameCollection.gameList[i].playerOne == null){
      socket.join(gameCollection.gameList[i].gameId);
      if(gameCollection.gameList[i].playerTwo == null){
        gameCollection.gameList[i].playerTwo = socket.id;
      }
      else
      gameCollection.gameList[i].playerOne = socket.id;
      gameCollection.gameList[i].playerTwoSocket = socket;
      io.to(gameCollection.gameList[i].gameId).emit('PlayerJoined', {
        gameId : gameCollection.gameList[i].gameId,
        playerOne: gameCollection.gameList[i].playerOne,
        playerTwo:gameCollection.gameList[i].playerTwo,
        playerJoined: socket.id
      })
      ///////////////////tell the client that a game has been found/////////////////
      socket.emit('gameFound', {
        gameId : gameCollection.gameList[i].gameId,
        playerOne: gameCollection.gameList[i].playerOne,
        playerTwo:gameCollection.gameList[i].playerTwo
      })
      console.log(socket.id +" has joined the game " + gameCollection.gameList[i].gameId);

      /////////////tell the player one that a client has joined his game//////////

      joined = true;
      break;

    }
  }
  if(!joined)
  ///////////when no game can be found//////////
  socket.emit('noGame');


}

//////////////create new game//////////////////////////
function makeGame(socket){//make a new room
  var gameId = (Math.random()+1).toString(36).slice(2, 18);
  console.log("Game Created by "+ socket.id + " w/ " + gameId);
  var game = {gameId: gameId, playerOne: socket.id, playerOneSocket: socket, open: true,playerTwo: null, playerTwoSocket: null };
  gameCollection.gameList.push(game);
  gameCollection.totalgameCount++;
  socket.emit('gameCreated', {
    username: socket.username,
    gameId: gameId
  });
  socket.join(gameId);
  console.log("user "+ socket.id + " has created a game " + gameId);
}


////////////////////////////////////////////////
function computeAndDecide(socket, msg){
var nextcurrent = computeData(msg.current,msg.digit, msg.operator);
var i = findPlayerRoom(socket.id);
var win = checkWin( msg.goal, nextcurrent);
if(win){
  socket.to(gameCollection.gameList[i].gameId).emit('gameLost',);//broadcast win
  socket.emit('gameWin', {});//tell the other player they have lost
}
else{
  socket.emit('next', {
    current: nextcurrent
  });
  }
}

////////////startgame///////////////////////////
function startGame(socket, gameName){
  var goal = RandomizeGoal();
  var listno = 30;
  var i = 0;
  var list = [];
  for(i = 0; i < listno; i++){
    list[i] = RandomizeNum();
  }
  var t = findPlayerRoom(socket.id);
  var time = (new Date()).getTime();
  io.to(gameName).emit('startingGame',{
    goalNumber: goal,
    Numlist : list,
    time: time
  });
}//with two users, starting the game when prompted by players

////////////////////////////////////////


function RandomizeNum(){
  return Math.floor((Math.random() * 10));
}


function RandomizeGoal(){
  var start = 0;
  var end = 0;
  //  return Math.floor((Math.random() * end) + start);
  return Math.floor((Math.random() * 30) + 30);

}

function RandomizeStart(){
  return Math.floor((Math.random() * 10));

}



function findPlayerRoom(playerID){//returns the index of room that the player is in
  var i = 0;
  for(i = 0; i < gameCollection.gameList.length; i++){
    if(gameCollection.gameList[i].playerTwo == playerID ||gameCollection.gameList[i].playerOne == playerID )
    return i;

  }
  return -1;
}

function findleftPlayer(playerID){
  var i = 0;
  for(i = 0; i < gameCollection.gameList.length; i++){
    if(gameCollection.gameList[i].playerOne == playerID)
    return 1;
    else if(gameCollection.gameList[i].playerTwo == playerID)
    return 2;

    return -1;
  }
}//who left the game?
///////////////////computation and stuff///////////////////////
function checkWin(goal, current){
  return   (goal==current);
}
function computeData(current,digit ,operator){

}
///////////////////////////////
