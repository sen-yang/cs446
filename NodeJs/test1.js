const express = require('express');
const url = require('url');
const WebSocket = require('ws');


const wss = new WebSocket.Server({ port: 3000 });
var sockets = [];
var number  = 0;


//if connected to, sync time,
wss.on('connection', function open(w){
  var id  = req.headers['sec-websocket-key'];
  socket[id] = w;
  console.log(id);
  w.on('message', function test1(msg){
  console.log("message: " + msg);
  });

  w.on('close', function closeCon(){
    console.log("close connections");
  });



});
// ////////////////////////////////////////////////
// function CalculateScore(score1, score2){
//
//
// }
//
// function syncTime(time1, time2){
//   if(time1==time2)
//     return true;
//     else {
//
//     }
// }
