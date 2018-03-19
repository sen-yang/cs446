package sen.sen.numericonsandroid.Networking;

import java.io.Serializable;

import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;

public class GameController implements Serializable{
  private Class<?> serverType;

  public GameController(Class<?> serverType){
    this.serverType = serverType;
  }

  public void addGameListener(GameListener gameListener){
    if(serverType == WebsocketController.class){
      WebsocketController.getInstance().addGameListener(gameListener);
    }
    else if(serverType == BluetoothController.class){
      BluetoothController.getInstance().addGameListener(gameListener);
    }
  }

  public void sendPlayerAction(PlayerAction playerAction){
    if(serverType == WebsocketController.class){
      WebsocketController.getInstance().sendPlayerAction(playerAction);
    }
    else if(serverType == BluetoothController.class){
      BluetoothController.getInstance().sendPlayerAction(playerAction);
    }
  }

  public boolean isConnected(){
    if(serverType == WebsocketController.class){
      return WebsocketController.getInstance().isConnected();
    }
    else if(serverType == BluetoothController.class){
      return BluetoothController.getInstance().isConnected();
    }
    return false;
  }

  public GameState getGameState(){
    if(serverType == WebsocketController.class){
      return WebsocketController.getInstance().getGameState();
    }
    else if(serverType == BluetoothController.class){
      return BluetoothController.getInstance().getGameState();
    }
    return null;
  }

  public User getUser(){
    if(serverType == WebsocketController.class){
      return WebsocketController.getInstance().getUser();
    }
    else if(serverType == BluetoothController.class){
      return BluetoothController.getInstance().getUser();
    }
    return null;
  }
}
