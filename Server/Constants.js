module.exports = {
  PORT_NUMBER: 6969,
  TICK_TIME: 1000 / 5, // (1s in ms) / fps
  GAME_READY_TIME: 5000,
  MIN_TARGET: -999,
  MAX_TARGET: 999,
  MIN_DROP: -9,
  MAX_DROP: 9,
  MIN_DROP_SPEED: 0.01,
  MAX_DROP_SPEED: 0.05,
  DROP_RATE: 225,
  GAME_START_DELAY: 5000,
  GLOBAL_ITEM_EFFECT_DURATION: 5000,
  EFFECT_ITEM_DROP_CHANCE: 0.04
};

module.exports.MESSAGE_TYPE = {
  PING: "PING",

  //server messages
  CONFIRMATION: "CONFIRMATION",
  GAME_INIT: "GAME_INIT",
  GAME_START: "GAME_START",
  GAME_FINISH: "GAME_FINISH",
  GAME_STATE_UPDATE: "GAME_STATE_UPDATE",
  GAME_FOUND: "GAME_FOUND",
  GET_RANKINGS: "GET_RANKINGS",

  //client messages
  LOGIN: "LOGIN",
  REGISTER: "REGISTER",
  UPDATE_USER: "UPDATE_USER",
  FIND_GAME: "FIND_GAME",
  PLAYER_ACTION: "PLAYER_ACTION",
  CREATE_USER: "CREATE_USER"
};

module.exports.PLAYER_ACTION_TYPE = {
  ADDITION: "ADDITION",
  SUBTRACTION: "SUBTRACTION",
  MULTIPLICATION: "MULTIPLICATION",
  DIVISION: "DIVISION",
  GET_NUMBER: "GET_NUMBER",
  USE_ITEM: "USE_ITEM",
  GET_ITEM: "GET_ITEM"
};

module.exports.GAME_TYPE = {
  SINGLEPLAYER: "SINGLEPLAYER",
  RANKED: "RANKED",
  GROUP_GAME: "GROUP_GAME",
  SECRET_MODE: "SECRET_MODE",
  CANCEL: "CANCEL"
};

module.exports.ITEM_TYPE = {
  NUMBER: "NUMBER",
  SPEED_INCREASE: "SPEED_INCREASE"
};

module.exports.CHARACTER_SPRITE = {
  BIRD_1: "BIRD_1",
  BIRD_2: "BIRD_2",
  BIRD_3: "BIRD_3"
};

module.exports.CONFIRMATION_TYPE = {
  BLUETOOTH_ACCEPT_GAME: "BLUETOOTH_ACCEPT_GAME",
  USER_CONFIRMATION: "USER_CONFIRMATION"
};
