package sen.sen.numericonsandroid.Networking;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.List;

import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketModels.ConfirmationMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameDroppedItemMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameStateMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.PlayerActionMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.WebsocketMessage;

public class BluetoothController{
  public static final String TAG = "BluetoothController";
  private static BluetoothController staticBluetoothController;

  public interface BluetoothListener{
    void onConnected();

    void onClose(int code, String reason, boolean remote);

    void loginConfirmed(boolean isConfirmed, User user);

    void gameInitialized(GameState gameState);
  }

  private Gson gson;
  private List<WeakReference<BluetoothListener>> bluetoothListenerList;
  private List<WeakReference<GameListener>> gameListenerList;
  private boolean isConnected;
  private User user;
  private GameState gameState;

  public static BluetoothController getInstance(){
    if(staticBluetoothController == null){
      staticBluetoothController = new BluetoothController();
      staticBluetoothController.initialize();
    }
    return staticBluetoothController;
  }

  public void addBlluetoothListener(BluetoothListener bluetoothListener){
    boolean existsInList = false;
    for(WeakReference listenerReference : bluetoothListenerList){
      if(listenerReference.get() != null && listenerReference.get() == bluetoothListener){
        existsInList = true;
      }
    }
    if(existsInList == false){
      bluetoothListenerList.add(new WeakReference<BluetoothListener>(bluetoothListener));
    }
  }


  public void addGameListener(GameListener gameListener){
    boolean existsInList = false;
    for(WeakReference listenerReference : gameListenerList){
      if(listenerReference.get() != null && listenerReference.get() == gameListener){
        existsInList = true;
      }
    }
    if(existsInList == false){
      gameListenerList.add(new WeakReference<GameListener>(gameListener));
    }
  }


  public void sendPlayerAction(PlayerAction playerAction){
    WebsocketMessage websocketMessage = new PlayerActionMessage(playerAction);
//    webSocketClient.send(gson.toJson(websocketMessage));
  }

  public boolean isConnected(){
    return isConnected;
  }

  public GameState getGameState(){
    return gameState;
  }

  public User getUser(){
    return user;
  }

  private void initialize(){

  }

  private void handleMessage(String message){
    WebsocketMessage websocketMessage = gson.fromJson(message, WebsocketMessage.class);

    switch(websocketMessage.getType()){
      case PING:
        break;
      case GAME_INIT:
        gameState = ((GameStateMessage) websocketMessage).getGameState();

        for(WeakReference<BluetoothListener> listenerWeakReference : bluetoothListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().gameInitialized(gameState);
          }
        }
        break;
      case GAME_START:
        gameState = ((GameStateMessage) websocketMessage).getGameState();
        for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().gameStarted(gameState);
          }
        }
        break;
      case GAME_FINISH:
        gameState = ((GameStateMessage) websocketMessage).getGameState();
        for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().gameFinished(gameState);
          }
        }
        break;
      case GAME_STATE_UPDATE:
        gameState = ((GameStateMessage) websocketMessage).getGameState();
        for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().gameStateUpdated(gameState);
          }
        }
        break;
      case GAME_DROPPED_ITEM:
        DroppedItem droppedItem = ((GameDroppedItemMessage) websocketMessage).getDroppedItem();
        for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().itemDropped(droppedItem);
          }
        }
        break;
      default:
        break;
    }
  }
}
