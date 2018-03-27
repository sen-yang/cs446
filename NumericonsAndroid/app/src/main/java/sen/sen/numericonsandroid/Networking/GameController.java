package sen.sen.numericonsandroid.Networking;

import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.SharedPreferencesHelper;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;

public class GameController implements Serializable{
  private Class<?> serverType;
  private LocalGameManager localGameManager;

  public GameController(Class<?> serverType){
    this.serverType = serverType;

    if(serverType == LocalGameManager.class){
      List<User> userList = new ArrayList<>();
      userList.add(SharedPreferencesHelper.getSavedUser());
      localGameManager = new LocalGameManager(UUID.randomUUID().toString(), userList);
    }
  }

  public void addGameListener(final GameListener gameListener){
    if(serverType == WebsocketController.class){
      WebsocketController.getInstance().addGameListener(gameListener);
    }
    else if(serverType == BluetoothController.class){
      BluetoothController.getInstance().addGameListener(gameListener);
    }
    else if(serverType == LocalGameManager.class){
      final Handler handler = new Handler();
      handler.postDelayed(new Runnable(){
        @Override
        public void run(){
          localGameManager.startGame(gameListener);
        }
      }, Constants.GAME_READY_TIME);
    }
  }

  public void removeGameListener(GameListener gameListener){
    if(serverType == WebsocketController.class){
      WebsocketController.getInstance().removeGameListener(gameListener);
    }
    else if(serverType == BluetoothController.class){
      BluetoothController.getInstance().removeGameListener(gameListener);
    }
    else if(serverType == LocalGameManager.class){
      localGameManager.stopAndClean();
    }
  }

  public void sendPlayerAction(PlayerAction playerAction){
    if(serverType == WebsocketController.class){
      WebsocketController.getInstance().sendPlayerAction(playerAction);
    }
    else if(serverType == BluetoothController.class){
      BluetoothController.getInstance().sendPlayerAction(playerAction);
    }
    else if(serverType == LocalGameManager.class){
      localGameManager.playerActionPerformed(SharedPreferencesHelper.getUsername(), playerAction);
    }
  }

  public boolean isConnected(){
    if(serverType == WebsocketController.class){
      return WebsocketController.getInstance().isConnected();
    }
    else if(serverType == BluetoothController.class){
      return BluetoothController.getInstance().isConnected();
    }
    else if(serverType == LocalGameManager.class){
      return localGameManager.isRunning();
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
    else if(serverType == LocalGameManager.class){
      return localGameManager.getGameState();
    }
    return null;
  }
}
