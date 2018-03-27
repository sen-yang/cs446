const DB = require('./Database/DatabaseConnector');
const eloRank = require('elo-rank');
var elo = new eloRank();
db = new DB();

module.exports = class ConnectionController{
    constructor(){
    }
/////////////////////
updateCharSprite(username, newimageLink, callback){
  var userData = {
  "username": username,
   "image" : newimageLink
    }
    db.UpdateCharacterSprite(userData)
    .then(data => {
      callback(true);
    })
    .catch(error => {
        console.log('ERROR:'+ error); // print the error;
    })
}


SelectCharSprite(username, callback){
  var userData = {
  "username": username,
    }
    db.SelectCharacterSprite(userData)
    .then(data => {
      callback(data);
    })
    .catch(error => {
        console.log('ERROR:'+ error); // print the error;
    })


}
///////////////////////////////////////
 LoginViaSessionID(SessionID, callback){
   var userData = {
   "SessionID": SessionID
     }
     db.LoginViaSessionID(userData)
     .then(data => {
       callback(data);
     })
     .catch(error => {
         callback(false);
         console.log('ERROR:'+ error); // print the error;
     })
 }

 UpdateSessionID(username, SessionID,callback){
   var userData = {
   "username": username,
   "SessionID": SessionID
     }
       db.UpdateSessionID(userData)
       .then(data => {
         callback(true);
       })
       .catch(error => {
         callback(false);
           console.log('ERROR:'+ error); // print the error;
       })
     }

///////////////////////////////////////
 Login(username, hashpassword, callback, failcallback){
  var userData = {
  "username": username,
   "hashpassword" : hashpassword
    }
    db.Login(userData)
    .then(data => {
      //emit ranking information back to client
      callback(data);//this function must check if the data is set.
    })
    .catch(error => {
        console.log('ERROR:'+ error); // print the error;
    })

  }


 selectRankings(init, offset, callback){
  var userData = {
  "start": init,
   "end" : offset
    }

      db.selectRankings(userData)
      .then(data => {
        //emit ranking information back to client
        callback(data);
      })
      .catch(error => {
          console.log('ERROR:'+ error); // print the error;
      })


    }



 Register(username, hashpassword, email, callback,errorcallback){
   var userData = {
   "username": username,
   "hashpassword" : hashpassword,
   "email" : email
     }

db.CheckUser(userData)
    .then(data => {
      console.log(data);
      if(JSON.stringify(data)=="[]")
      this.createUser(username, hashpassword, email,  callback,errorcallback);
      else callback(false);
    })
    .catch(error => {

    console.log('ERROR:'+ error); // print the error;
  })
}


 createUser(username, hashpassword, email, callback,errorcallback){
   var userData = {
   "username": username,
   "hashpassword" : hashpassword,
   "email" : email
     }
  db.registerUser(userData)
      .then(data => {
          console.log("user have been registered");
          this.Login(username, hashpassword,callback,errorcallback);//send data to user about a successful registration
          //emit data to user client
          //if successful, emit registration sucessfull
      })
      .catch(errorcallback, error => {
          console.log('ERROR:'+ error); // print the error;
          errorcallback(error);
      })


}


 calculateUpdateRanking(Winnername, Losername, callback){
  //TODO
  db.getRating(Winnername, Losername)
        .then(data => {
          var user1 = Object.values(data[0]);
          var user2 = Object.values(data[1]);
          var winnerrating, loserrating;
          var winner,loser;
          if(user1[0]==Winnername){
            winner = user1[0];
          winnerrating = user1[1];
          loserrating = user2[1];
          loser = user2[0];
        }else{
          winner = user2[0];
          winnerrating = user2[1];
          loserrating = user1[1];
          loser = user1[0];
        }
        console.log("loser:" + loser);
        var expectedScoreA = elo.getExpected(winnerrating, loserrating);
        var expectedScoreB = elo.getExpected(loserrating, winnerrating);

          //update score, 1 if won 0 if lost
          var playerA = elo.updateRating(expectedScoreA, 1, winnerrating);
          var playerB = elo.updateRating(expectedScoreB, 0, loserrating);

          this.updateRating(winner,winnerrating, loser, loserrating,callback);
      })
      .catch(error => {
          console.log('ERROR:'+ error); // print the error;
      })


}
  updateRating(winneruser, winnerScore, loseruser, loserScore, callback){
    db.updateRatingAndRank(winneruser,winnerScore)
    .then(data => {
    console.log(winneruser + "\'s score is  updated")
  })
  .catch(error => {
      console.log('ERROR:'+ error); // print the error;
  });


    db.updateRatingAndRank(loseruser, loserScore)
    .then(data => {
      console.log("Test");
    console.log(loseruser + "\'s score is  updated")
  })
  .catch(error => {
      console.log('ERROR:'+ error); // print the error;
  });
  }



 checkANDChangePassword(username, oldpassword ,updatePassword,  callback, errorcallback){
  //hash password please
  var userData = {
  "username": username,
  "hashpassword" : updatePassword,
  "oldpassword": oldpassword
    }


    db.CheckOldPassword(userData)
    .then(data => {
        if(JSON.stringify(data) != "[]"){
          console.log(data)
          this.changePass(userData, callback,errorcallback);

        }else
        callback(false);

        //emit data to user client
        //if successful, emit registration sucessfull
    })
    .catch(error => {
        console.log('ERROR:', error); // print the error;
        errorcallback(error);
    })

}


 changePass(userData, callback, errorcallback){
  db.updateUser(userData)
  .then(data => {
      callback(true);
      //emit data to user client
      //if successful, emit registration sucessfull

  })
  .catch(error => {
      console.log('ERROR:', error); // print the error;
  })

}

}
