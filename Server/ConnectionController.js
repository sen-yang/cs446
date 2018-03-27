const DB = require('./Database/DatabaseConnector');
const eloRank = require('elo-rank');
var elo = new eloRank();
var settings = {
  // tau : "Reasonable choices are between 0.3 and 1.2, though the system should
  //      be tested to decide which value results in greatest predictive accuracy."
  tau : 0.5,
  // rating : default rating
  rating : 1500,
  //rd : Default rating deviation
  //     small number = good confidence on the rating accuracy
  ratingdev : 200,
  //vol : Default volatility (expected fluctation on the player rating)
  volatility : 0.06
};

db = new DB();

module.exports = class ConnectionController{
    constructor(){
    }
/////////////////////
updateCharSprite(username, newimageLink, callback, failcallback){
  var userData = {
  "username": username,
   "image" : newimageLink
    }
    db.UpdateCharacterSprite(userData)
    .then(data => {
      this.selectUser(username,callback,failcallback);
    })
    .catch(error => {
        console.log('ERROR:'+ error); // print the error;
        failcallback(error);
    })
}

selectUser(username,  callback, failcallback){
  var userData = {
  "username": username,
    }
    db.SelectUser(userData)
    .then(data => {
      callback(data);
    })
    .catch(error => {
        console.log('ERROR:'+ error); // print the error;
        failcallback(error);
    })
}


SelectCharSprite(username, callback,failcallback){
  var userData = {
  "username": username,
    }
    db.SelectCharacterSprite(userData)
    .then(data => {
      callback(data);
    })
    .catch(error => {
      failcallback(error);
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

 UpdateSessionID(username, SessionID,callback,failcallback){
   var userData = {
   "username": username,
   "SessionID": SessionID
     }
       db.UpdateSessionID(userData)
       .then(data => {
         this.selectUser(username, callback,failcallback);
       })
       .catch(error => {
         failcallback(error);
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


 selectRankings(init, offset, callback, failcallback){
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



 Register(username, hashpassword, email,callback, failcallback){
   var userData = {
   "username": username,
   "hashpassword" : hashpassword,
   "email" : email,
     }

db.CheckUser(userData)
    .then(data => {
      console.log(data);
      if(JSON.stringify(data)=="[]")
      this.createUser(username, hashpassword, email, callback, failcallback);
      else failcallback("Username already exists, please choose another");
    })
    .catch(error => {

    console.log('ERROR:'+ error); // print the error;
  })
}


 createUser(username, hashpassword, email, callback,errorcallback){
   var userData = {
   "username": username,
   "hashpassword" : hashpassword,
   "email" : email,
   "charactersprite": Constants.CHARACTER_SPRITE.BIRD_1
     }
  db.registerUser(userData)
      .then(data => {
          console.log("user have been registered");
          db.addDefaultRanking(userData);
          console.log(username);
          this.selectUser(username,callback,errorcallback);//send data to user about a successful registration

          //emit data to user client
          //if successful, emit registration sucessfull
      })
      .catch(errorcallback, error => {
          console.log('ERROR:'+ error); // print the error;
          errorcallback(error);
      })


}


 calculateUpdateRanking(Winnername, Losername,callback, failcallback){
  //TODO
  db.getRating(Winnername, Losername)
        .then(data => {
      })
      .catch(error => {
          console.log('ERROR:'+ error); // print the error;
      })


}
  updateRating(winneruser, winnerScore, loseruser, loserScore, callback, failcallback){
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
      //emit data to user client
      //if successful, emit registration sucessfull
      this.selectUser(userData.username, callback, errorcallback);

  })
  .catch(error => {
    errorcallback(error);
      console.log('ERROR:', error); // print the error;
  })

}

}
