package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.User;

public class ConfirmationMessage extends WebsocketMessage{
  private boolean isConfirmed;
  private User user;
  private Constants.CONFIRMATION_TYPE confirmationType;
  private String errorMessage;
  private String sessionID;

  public boolean isConfirmed(){
    return isConfirmed;
  }

  public User getUser(){
    return user;
  }

  public Constants.CONFIRMATION_TYPE getConfirmationType(){
    return confirmationType;
  }

  public String getErrorMessage(){
    return errorMessage;
  }

  public String getSessionID(){
    return sessionID;
  }

  public ConfirmationMessage(boolean isConfirmed, Constants.CONFIRMATION_TYPE confirmationType, User user){
    super(Constants.MESSAGE_TYPE.CONFIRMATION);
    this.isConfirmed = isConfirmed;
    this.confirmationType = confirmationType;
    this.user = user;
  }
}
