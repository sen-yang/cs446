const ConnectionController = require('./ConnectionController');


const CC = new ConnectionController();


//Examples for using ConnectionController


function UpdateImage(){
  CC.updateCharSprite("user1","google.com",function(data){
    console.log(data);
  });
}
function SelectImage(){
  CC.SelectCharSprite("user1",function(data){
    console.log(data);
  });
}
////////////Register////////////////////
function Register(){//username, password, //email
  //hashing should be done on the client side.
 CC.Register("z7546462z","notreallyhashed","emailtest", function(data){
   if(data){
     console.log(data)
     //return registration successful
     console.log("WORKS?");
   }else {
     //not really.
     console.log("data exists");
   }
 },function(error){
   console.log("nope dont works");
 });
}
/////////////LOGIN///////////////////////////
function Login(){
  CC.Login("612z2z","notreallyhashed",function(data){
  //successful select login

  if (JSON.stringify(data)==[]){
    //fail login, notify user
    console.log("FAILS");
  }else {
    console.log(data);
    //successful login. notify user
  }

  },function(){
  //fail login
  console.log("FAIL: " + error);
  })
}
//////////////////change password/////////////////////////
function Changepassword(){
  //hashing done at client side for both old password and new one
                            //username // oldpassword, new password
  CC.checkANDChangePassword("user2","password", "notreallyhashed",function(data){
    console.log(data);

  },function(error){
    console.log(error);
  });
}
///////////////////////////////////////
function SelectRanks(){
CC.selectRankings(1, 10 , function(data){
console.log(data);
});


}
/////////////////////////////////update rank
function UpdateRanking(){
CC.calculateUpdateRanking("user1","user3", function(data){

});

}

function InsertSessionID(){
  CC.UpdateSessionID("user1", "test" ,function(data){
    console.log(data);
  })
}

function LoginSessionID(){
  CC.LoginViaSessionID("test",function(data){
    console.log(data)
  });
}

//
// function updateRanks(Winner, score1,  Loser,score2){
//
// }
//SelectImage();
Register();
//SelectRanks();
// InsertSessionID();
// LoginSessionID();
//Changepassword();
//UpdateRanking();
//////////////////////////////////////////////////////////
