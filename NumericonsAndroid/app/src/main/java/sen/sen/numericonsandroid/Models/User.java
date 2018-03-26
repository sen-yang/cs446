package sen.sen.numericonsandroid.Models;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;

public class User implements Serializable{
  private String username;
  private Constants.CHARACTER_SPRITE characterSprite;

  public User(String username, Constants.CHARACTER_SPRITE characterSprite){
    this.username = username;
    this.characterSprite = characterSprite;
  }

  public String getUsername(){
    return username;
  }

  public void setUsername(String username){
    this.username = username;
  }

  public Constants.CHARACTER_SPRITE getCharacterSprite(){
    return characterSprite;
  }

  public void setCharacterSprite(Constants.CHARACTER_SPRITE characterSprite){
    this.characterSprite = characterSprite;
  }
}
