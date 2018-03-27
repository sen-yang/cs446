package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.User;

public class UserMessage extends WebsocketMessage{
  private User user;

  public UserMessage(User user){
    super(Constants.MESSAGE_TYPE.UPDATE_USER);
    this.user = user;
  }
}
