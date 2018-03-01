const io = require('socket.io-client');

const socket = io('http://localhost:3000');


socket.on('connect', function() {
  console.log(socket.id);
  //////////////////search for game/////////////////
  searchGame();
});

function searchGame(){

  socket.emit('searchGame');

  ///////////////when game is matched, detailed is recieved to the client
  socket.on('gameFound', function(msg){
    console.log("game found, joining game");
  });

  socket.on('noGame', function(msg){
    console.log("No game found");
    console.log("creating own game");
    makeGame();

  });


  socket.on('PlayerJoined', function(msg){
    console.log("player " + msg.playerJoined + " has joined the game!");
    setTimeout(function () {

      console.log("starting game...");
    }, 100);

    setTimeout(function () {

      socket.emit('startGame');
    }, 5000);

  });

  socket.on('startingGame', function(msg){
    var goalNumber = msg.goalNumber;
    var time = msg.time;
    var list = msg.Numlist;
    console.log(goalNumber);
    console.log(list);
});





}


function makeGame(){
  socket.emit('makeGame');
  ////////////confirmation of game created//////////////////
  socket.on('gameCreated', function(){
    console.log("one game have been created");
  });
}
