package sen.sen.numericonsandroid.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;

public class User implements Serializable{
  private String username;
  private Constants.CHARACTER_SPRITE characterSprite;
  private int rank;
  private int rankRating;
  private boolean isTemporary;

  public User(String username, Constants.CHARACTER_SPRITE characterSprite){
    this.username = username;
    this.characterSprite = characterSprite;
    this.isTemporary = true;
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

  public int getRank(){
    return rank;
  }

  public int getRankRating(){
    return rankRating;
  }

  public boolean isTemporary(){
    return isTemporary;
  }
}
