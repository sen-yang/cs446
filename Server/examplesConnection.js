const ConnectionController = require('./ConnectionController');


const CC = new ConnectionController();


//Examples for using ConnectionController


function UpdateImage(){
  CC.updateImage("user1","google.com",function(data){
    console.log(data);
  });
}

////////////Register////////////////////
function register(){//username, password, //email
  //hashing should be done on the client side.
 CC.Register("612z2z","notreallyhashed","emailtest", function(data){
   if(data){
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
function login(){
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
function changepassword(){
  //hashing done at client side for both old password and new one
                            //username // oldpassword, new password
  CC.checkANDChangePassword("612z2z","notreallyhashed2", "notreallyhashed",function(data){
    if(data){
      //successful changed password
      console.log("YES!!");

    }else{
      //nope.
      console.log("NOPE");
    }
  },function(error){
    console.log(error);
  });
}
///////////////////////////////////////
function selectRanks(){
CC.selectRankings(1, 10 , function(data){
console.log(data);
});


}
/////////////////////////////////update rank
CC.calculateUpdateRanking("user2","user3", function(){

});
//
// function updateRanks(Winner, score1,  Loser,score2){
//
// }
UpdateImage();


//////////////////////////////////////////////////////////
