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
    return  db.any('select username from users,rankingsdata where rank BETWEEN $1 and $2 order by Rank desc', [userData.start, userData.end]);
  }


  Login(userData){
    return  db.any('select username from users where username=$1 AND hashpassword=$2', [userData.username, userData.hashpassword])
    }

    updateUser(userData){
      return db.any('update users set hashpassword=$2  where username=$1', [userData.username, userData.hashpassword])
    }


    registerUser(userData){
      return db.any('insert into users(username, hashpassword, email) values($1,$2,$3)', [userData.username, userData.hashpassword, userData.email])
    }


    CheckUser(userData){
        return db.any('select username from users where username=$1', [userData.username]);
    }

    CheckOldPassword(userData){
      return db.any('select username from users where username=$1 AND hashpassword=$2', [userData.username, userData.oldpassword]);
    }

    updateRanking(userData){
      return db.any('', [userData.username]);
    }

    getRating(winner,loser){
      return db.any('select username,rating from users,rankingsdata where users.UID=rankingsdata.UID and (username=$1 OR username=$2)',[winner,loser] );
    }
    updateRatingAndRank(user, userscore){
      return db.any('update rankingsdata set rating=$1 from users where users.UID=rankingsdata.UID and users.username=$2',[userscore, user]);
    }
};
/////////////////////////////////////////////database interface/
