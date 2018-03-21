package sen.sen.numericonsandroid.Networking.WebsocketModels;

import java.io.Serializable;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.User;

public class WebsocketMessage implements Serializable {
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
