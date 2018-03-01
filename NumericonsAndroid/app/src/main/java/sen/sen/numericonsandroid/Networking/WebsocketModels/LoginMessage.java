package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;

public class LoginMessage extends WebsocketMessage{
  private String username;

  public LoginMessage(String username){
    super(Constants.MESSAGE_TYPE.LOGIN);
    this.username = username;
  }
}
