package sen.sen.numericonsandroid.Networking.WebsocketModels;

import com.google.gson.annotations.SerializedName;

import sen.sen.numericonsandroid.Global.Constants;

/**
 * Created by sen on 2018-02-28.
 */

public class FindGameMessage extends WebsocketMessage{
  private Constants.GAME_TYPE gameType;

  public FindGameMessage(Constants.GAME_TYPE gameType){
    super(Constants.MESSAGE_TYPE.FIND_GAME);
    this.gameType = gameType;
  }
}
