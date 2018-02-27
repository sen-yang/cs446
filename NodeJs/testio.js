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
var roomid = 0;
var connectedclients = [];
var inroomclients = [];
const roomname = '/room' + roomid;


/////////////////////////connect//////////////////////////////
io.on('connection', function(socket){
    connectedclients.push(socket.id);
    console.log(connectedclients);
  console.log('a user connected');
///////////////////////////////////////////////////////
  socket.on('makeGame', function () {
    makeGame(socket);
  });
////////////////////////////////////////////////////
  socket.on('searchGame', function(){
    searchGame(socket);
  });


///////////////////////////////////////////////////////
///////////////////////disconnect/////////////////////
  socket.on('disconnect', function(socket){
    console.log('user disconnected');
    var index = connectedclients.indexOf(socket.id);
      connectedclients.splice(index, 1);

  });
///////////////////////////////////////////////////////

});

function searchGame(socket){
  var i = 0;
  var joined = false;
   for(i = 0; i < gameCollection.gameList.length; i++){
     if(gameCollection.gameList[i].playerTwo == null){
       socket.join(gameCollection.gameList[i].gameId);
      console.log(gameId);
       gameCollection.gameList[i].playerTwo = socket.id;
       gameCollection.gameList[i].playerTwoSocket = socket;
       ///////////////////tell the client that a game has been found/////////////////
        socket.emit('gameFound', {
          gameId : gameCollection.gameList[i].gameId,
          playerOne: gameCollection.gameList[i].playerOne,
          playerTwo:gameCollection.gameList[i].playerTwo
        })
       console.log(gameCollection.gameList[0].playerTwo);

       /////////////tell the player one that a client has joined his game//////////
       io.to(gameCollection.gameList[i].gameID).emit('SecondPlayerJoined', {
         gameId : gameCollection.gameList[i].gameId,
         playerOne: gameCollection.gameList[i].playerOne,
         playerTwo:gameCollection.gameList[i].playerTwo
       })
       joined = true;
       break;

     }
   }
   if(!joined)
   ///////////when no game can be found//////////
   socket.emit('noGame');


}


function makeGame(socket){
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
  console.log(gameId);
}



////////////////////////////////////////////////
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
