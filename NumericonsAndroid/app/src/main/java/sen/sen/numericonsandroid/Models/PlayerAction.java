package sen.sen.numericonsandroid.Models;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;

public class PlayerAction implements Serializable{
  public Constants.PLAYER_ACTION_TYPE getCommandType() {
    return commandType;
  }

  public void setCommandType(Constants.PLAYER_ACTION_TYPE commandType) {
    this.commandType = commandType;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public DroppedItem getItem(){
    return item;
  }

  public void setItem(DroppedItem item){
    this.item = item;
  }

  private Constants.PLAYER_ACTION_TYPE commandType;
  private int value;
  private DroppedItem item;

  public PlayerAction(Constants.PLAYER_ACTION_TYPE commandType, int value){
    this.commandType = commandType;
    this.value = value;
  }
}
