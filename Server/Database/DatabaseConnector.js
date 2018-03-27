class DatabaseInterface{
    constructor(){
    }
        selectRanking(userData){}
        Login(userData){}
        updateUser(userData){}
        registerUser(userData){}
        CheckUser(userData){}
        updateRanking(userData){}
    };






'use strict';

const dbhost = 'localhost';
const dbport = 5432;
const dbName = 'CS446Rise';
const dbuser = 'test';
const dbpassword = 'test';

const promise = require('bluebird'); // or any other Promise/A+ compatible library;

const initOptions = {
    promiseLib: promise // overriding the default (ES6 Promise);
};

const pgp = require('pg-promise')(initOptions);


const dbConnectLink = {
  host: dbhost,
  port: dbport,
  database: dbName,
  user: dbuser,
  password: dbpassword
};

const db = pgp(dbConnectLink); // database instance;
module.exports = class DatabaseConnector extends DatabaseInterface{
  constructor(){
    super();
  }



  selectRankings(userData){
    //select ranking between number userData.start and userData.end
    return  db.any('select * from seeranks where rank BETWEEN $1 and $2 order by Rank desc LIMIT 10', [userData.start, userData.end]);
  }

  UpdateSessionID(userData){
    return db.any ('update users set SessionID=$2 where username=$1',[userData.username, userData.SessionID]);
  }

  LoginViaSessionID(userData){
    return  db.any('select * from users where SessionID=$1', [userData.SessionID])
  }
  SelectUser(userData){
    return  db.any('select * from users,rankingsdata where username=$1 and users.UID = rankingsdata.UID', [userData.username])
  }

  Login(userData){
    return  db.any('select * from users,rankingsdata where users.UID = rankingsdata.UID and username=$1 AND hashpassword=$2', [userData.username, userData.hashpassword])
    }
  UpdateCharacterSprite(userData){
    return  db.any('update users set characterSprite=$2  where username=$1', [userData.username, userData.image])

  }
  SelectCharacterSprite(userData){
    return  db.any('select characterSprite from users where username=$1', [userData.username, userData.image])

  }
    updateUser(userData){
      return db.any('update users set hashpassword=$2  where username=$1', [userData.username, userData.hashpassword])
    }


    registerUser(userData){
      return db.any('insert into users(username, hashpassword, email, characterSprite) values($1,$2,$3,$4)', [userData.username, userData.hashpassword, userData.email, userData.characterSprite])
    }
    UpdateRanking(userData){
      return db.any('update rankingsdata set rating=$2,tau=$3,ratingdev=$4,volatility=$5 from users where users.UID=rankingsdata.UID and users.username=$1', [userData.username])
    }
    addDefaultRanking(userData){
      return db.any('insert into rankingsdata(UID, tau, rating, ratingdev, volatility) VALUES ((select UID from users where username=$1), 0.5, 1500, 200, 0.06)', [userData.username])
    }

    CheckUser(userData){
        return db.any('select username from users where username=$1', [userData.username]);
    }

    CheckOldPassword(userData){
      return db.any('select username from users where username=$1 AND hashpassword=$2', [userData.username, userData.oldpassword]);
    }


    updateRanking(userData){
      return db.any('update rankingsdata set rating=$1 from users where users.UID=rankingsdata.UID and users.username=$1', [userData.username]);
    }

    getRating(winner,loser){
      return db.any('select username, tau,rating,ratingdev,volatility from rankingsdata,users where users.UID=rankingsdata.UID and (users.username=$1 OR users.username=$2)',[winner,loser] );
    }
    updateRating(userData){
      return db.any('update rankingsdata set rating=$1,ratingdev=$2,volatility=$3 from users where users.UID=rankingsdata.UID and users.username=$4',[userData.rating,userData.ratingdev,userData.volatility, userData.username]);
    }
};
/////////////////////////////////////////////database interface/
