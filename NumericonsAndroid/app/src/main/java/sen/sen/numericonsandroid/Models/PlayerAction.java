package sen.sen.numericonsandroid.Models;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;

public class PlayerAction implements Serializable{
  private Constants.PLAYER_ACTION_TYPE commandType;
  private int value;

  public PlayerAction(Constants.PLAYER_ACTION_TYPE commandType, int value){
    this.commandType = commandType;
    this.value = value;
  }
}
