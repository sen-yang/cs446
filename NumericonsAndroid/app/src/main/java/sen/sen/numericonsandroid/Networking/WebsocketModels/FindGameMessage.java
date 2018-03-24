package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;

public class FindGameMessage extends WebsocketMessage{
  private Constants.GAME_TYPE gameType;

  public FindGameMessage(Constants.GAME_TYPE gameType){
    super(Constants.MESSAGE_TYPE.FIND_GAME);
    this.gameType = gameType;
  }
}
