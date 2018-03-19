package sen.sen.numericonsandroid.Models;

import java.io.Serializable;

public class User implements Serializable{
  private String username;
  private String imageUrl;
  private String imageUrlThumbnail;
  private String id;

  public String getUsername(){
    return username;
  }

  public void setUsername(String username){
    this.username = username;
  }
}
