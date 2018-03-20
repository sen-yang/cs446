const DB = require('./Models/DatabaseConnector');

db = new DB();

function Login(username, hashpassword){
  userData = {
  "username": username,
   "hashpassword" : hashpassword
    }
    db.Login(userData)
    .then(data => {
      //emit ranking information back to client
      if( JSON.stringify(data)=="[]"){
        console.log("not logged in");
        //emit not logged in. username/password wrong
      }
      else console.log("logged in" + data);
      //emit logged in and pass the data back

    })
    .catch(error => {
        console.log('ERROR:', error); // print the error;
    })

  }


function selectRankings(init, offset){
  userData = {
  "start": init,
   "end" : offset
    }

      db.selectRankings(userData)
      .then(data => {
        //emit ranking information back to client
        console.log("ranking" + data);

      })
      .catch(error => {
          console.log('ERROR:', error); // print the error;
      })


    }


function registerwithCheckUser(username, hashpassword, email){
  userData = {
  "username": username,
   "hashpassword" : hashpassword,
   "email" : email
    }

db.CheckUser(userData)
    .then(data => {
      if(data[0]!=undefined){
        console.log("there exist a user, please choose another username");
        //emit username exists, choosen another
      }else{
        createUser(userData);
      }
    })
    .catch(error => {
        console.log('ERROR:', error); // print the error;
    })

  }

function createUser(UserData){
  db.registerUser(userData)
      .then(data => {
          console.log("user have been registered");
          //emit data to user client
          //if successful, emit registration sucessfull

      })
      .catch(error => {
          console.log('ERROR:', error); // print the error;
      })


}


function updateRanking(){
  //TODO
}

function checkANDChangePassword(username, oldpassword ,updatePassword){
  //hash password please
  userData = {
  "username": username,
  "hashpassword" : updatePassword,
  "oldpassword": oldpassword
    }


    db.CheckOldPassword(userData)
    .then(data => {

        //emit data to user client
        //if successful, emit registration sucessfull
        changePass(userData);
    })
    .catch(error => {
        console.log('ERROR:', error); // print the error;
    })

}
function changePass(userData){
      console.log("TES"+userData);
  db.updateUser(userData)
  .then(data => {
      console.log("user have changed his password");
      //emit data to user client
      //if successful, emit registration sucessfull

  })
  .catch(error => {
      console.log('ERROR:', error); // print the error;
  })

}

//Login("user1","notreallyhashed");
//selectTop10Rank();
//db.selectALL();
//registerwithCheckUser("12z2z","notreallyhashed","emailtest");
checkANDChangePassword("12z2z","oldpassword", "notreallyhashed2");
//Login("122zz","notreallyhashed","emailtest");
//selectRankings(1,3);
