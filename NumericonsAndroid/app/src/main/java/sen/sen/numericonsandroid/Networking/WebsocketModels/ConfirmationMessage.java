package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Models.User;

public class ConfirmationMessage extends WebsocketMessage{
  private boolean isConfirmed;
  private User user;

  public boolean isConfirmed(){
    return isConfirmed;
  }

  public User getUser(){
    return user;
  }
}
