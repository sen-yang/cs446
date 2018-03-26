package sen.sen.numericonsandroid.Models;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;

public class User implements Serializable{
  private String username;
  private Constants.USER_CHARACTER userCharacter;

  public User(String username, Constants.USER_CHARACTER userCharacter){
    this.username = username;
    this.userCharacter = userCharacter;
  }

  public String getUsername(){
    return username;
  }

  public void setUsername(String username){
    this.username = username;
  }

  public Constants.USER_CHARACTER getUserCharacter(){
    return userCharacter;
  }
}
