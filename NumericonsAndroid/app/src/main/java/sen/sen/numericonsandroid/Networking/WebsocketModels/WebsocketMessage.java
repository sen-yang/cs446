package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.User;

public class WebsocketMessage{
  private Constants.MESSAGE_TYPE type;

  public WebsocketMessage(){

  }
  public WebsocketMessage(Constants.MESSAGE_TYPE type){
    this.type = type;
  }

  public Constants.MESSAGE_TYPE getType(){
    return type;
  }
}
