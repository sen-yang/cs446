package sen.sen.numericonsandroid.Networking.WebsocketModels;

import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.User;

public class RankingsMessage extends WebsocketMessage{
  private List<User>userList;

  public List<User> getUserList(){
    return userList;
  }
}
