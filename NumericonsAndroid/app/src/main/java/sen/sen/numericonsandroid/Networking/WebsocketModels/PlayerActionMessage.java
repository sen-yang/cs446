package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.PlayerAction;

public class PlayerActionMessage extends WebsocketMessage{
  public PlayerAction getPlayerAction() {
    return playerAction;
  }

  public void setPlayerAction(PlayerAction playerAction) {
    this.playerAction = playerAction;
  }

  private PlayerAction playerAction;

  public PlayerActionMessage(PlayerAction playerAction){
    super(Constants.MESSAGE_TYPE.PLAYER_ACTION);
    this.playerAction = playerAction;
  }
}
