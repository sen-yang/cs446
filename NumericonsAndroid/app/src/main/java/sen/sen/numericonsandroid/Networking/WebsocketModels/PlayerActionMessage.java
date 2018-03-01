package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.PlayerAction;

public class PlayerActionMessage extends WebsocketMessage{
  private PlayerAction playerAction;

  public PlayerActionMessage(PlayerAction playerAction){
    super(Constants.MESSAGE_TYPE.PLAYER_ACTION);
    this.playerAction = playerAction;
  }
}
