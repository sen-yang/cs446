module.exports = {
  PORT_NUMBER: 6969,
  TICK_TIME: 1000 / 10, // (1s in ms) / fps
  GAME_READY_TIME: 5000,
  MIN_TARGET: -99,
  MAX_TARGET: 100,
  MIN_DROP: -9,
  MAX_DROP: 10
};

module.exports.messageType = {
  PING: 0,

  //server messages
  LOGIN_CONFIRMATION: 1,
  GAME_INIT: 2,
  GAME_START: 3,
  GAME_FINISH: 4,
  GAME_STATE_UPDATE: 5,
  GAME_DROPPED_ITEM: 6,
  GAME_FOUND: 7,
  GET_RANKINGS: 8,

  //client messages
  LOGIN: 100,
  FIND_GAME: 101,
  PLAYER_ACTION: 102
};

module.exports.playerActionType = {
  ADDITION: 0,
  SUBTRACTION: 1,
  MULTIPLICATION: 2,
  DIVISION: 3,
  GET_NUMBER: 4,
  USE_ITEM: 5,
  GET_ITEM: 6
};