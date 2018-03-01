package sen.sen.numericonsandroid.Models;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;

public class Player implements Serializable{
  private int targetNumber;
  private int currentNumber;
  private Constants.PLAYER_ACTION_TYPE currentOperation;
  private String username;
  private String imageUrlThumbnail;

  public int getTargetNumber(){
    return targetNumber;
  }

  public int getCurrentNumber(){
    return currentNumber;
  }

  public Constants.PLAYER_ACTION_TYPE getCurrentOperation(){
    return currentOperation;
  }

  public String getUsername(){
    return username;
  }

  public String getImageUrlThumbnail(){
    return imageUrlThumbnail;
  }
}
