package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.Helpers;

public class LoginMessage extends WebsocketMessage{
  private String username;
  private String password;
  private String sessionID;

  public LoginMessage(Constants.MESSAGE_TYPE messageType){
    super(messageType);
  }

  public void setUsername(String username){
    this.username = username;
  }

  public void setPassword(String password){
    this.password = password;
  }

  public void setSessionID(String sessionID){
    this.sessionID = sessionID;
  }
}
