package sen.sen.numericonsandroid.Networking;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.gsonfire.GsonFireBuilder;
import io.gsonfire.TypeSelector;
import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketModels.ConfirmationMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.FindGameMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameDroppedItemMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameStateMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.LoginMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.PlayerActionMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.WebsocketMessage;


public class BluetoothController{
  public static final String TAG = "BluetoothController";
  private static BluetoothController staticBluetoothController;

  public interface BluetoothListener{
    void onConnected();

    void onClose(int code, String reason, boolean remote);

    void invitedToMatch(BluetoothDevice device);

    void matchConfirmed(BluetoothDevice device);

    void gameInitialized(GameState gameState);
  }

  private Gson gson;
  private List<WeakReference<BluetoothListener>> bluetoothListenerList;
  private List<WeakReference<GameListener>> gameListenerList;
  private BluetoothChatService bluetoothChatService;
  private boolean isConnected;
  private User user;
  private GameState gameState;
  private BluetoothDevice connectedDevice;
  private BluetoothAdapter bluetoothAdapter;
  private boolean isHost;

  public static BluetoothController getInstance(){
    if(staticBluetoothController == null){
      staticBluetoothController = new BluetoothController();
      staticBluetoothController.initialize();
    }
    return staticBluetoothController;
  }

  public void addBluetoothListener(BluetoothListener bluetoothListener){
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

  public void listenForInvites(boolean shouldListen){
    if(shouldListen){
      bluetoothChatService.start();
    }
    else{
      bluetoothChatService.stop();
    }
  }

  public void inviteDeviceToGame(BluetoothDevice device){
    bluetoothChatService.connect(device, true);
  }


  public void respondToInvitation(BluetoothDevice device, boolean accepted){
    if(bluetoothChatService.getConnectedDevice() != null){
      if(accepted){
        WebsocketMessage websocketMessage = new WebsocketMessage(Constants.MESSAGE_TYPE.GAME_INIT);
        bluetoothChatService.write(gson.toJson(websocketMessage).getBytes());
      //start own game too.
      }
    }
  }


  public void sendPlayerAction(PlayerAction playerAction) {
    if(isHost){
      //todo
    }
    else{
      bluetoothChatService.write(convertToBytes(new PlayerActionMessage(playerAction)));
    }
  }


  public User getUser(){
    return user;
  }

  private void initialize(){
    bluetoothChatService = new BluetoothChatService(new IncomingBluetoothMessageHandler());
    bluetoothListenerList = new ArrayList<>();
    gameListenerList = new ArrayList<>();

    GsonFireBuilder builder = new GsonFireBuilder()
        .registerTypeSelector(WebsocketMessage.class, new TypeSelector<WebsocketMessage>(){
          @Override
          public Class<? extends WebsocketMessage> getClassForElement(JsonElement readElement){
            Constants.MESSAGE_TYPE messageType = Constants.MESSAGE_TYPE.valueOf(readElement.getAsJsonObject().get("type").getAsString());
            switch(messageType){
              case PING:
                return WebsocketMessage.class;
              case LOGIN_CONFIRMATION:
                return ConfirmationMessage.class;
              case GAME_INIT:
              case GAME_START:
              case GAME_FINISH:
              case GAME_STATE_UPDATE:
                return GameStateMessage.class;
              case GAME_DROPPED_ITEM:
                return GameDroppedItemMessage.class;
              case LOGIN:
                return LoginMessage.class;
              case FIND_GAME:
                return FindGameMessage.class;
              case PLAYER_ACTION:
                return PlayerActionMessage.class;
            }
            return null;
          }
        });
    gson = builder.createGsonBuilder().create();
  }

  private class IncomingBluetoothMessageHandler extends Handler{
    @Override
    public void handleMessage(Message msg){
      Log.d("asdf", msg.toString());
      switch(msg.what){
        case Constants.MESSAGE_STATE_CHANGE:
          switch(msg.arg1){
            case BluetoothChatService.STATE_CONNECTED:
//              setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//              mConversationArrayAdapter.clear();
              break;
            case BluetoothChatService.STATE_CONNECTING:
//              setStatus(R.string.title_connecting);
              break;
            case BluetoothChatService.STATE_LISTEN:
            case BluetoothChatService.STATE_NONE:
//              setStatus(R.string.title_not_connected);
              break;
          }
          break;
        case Constants.MESSAGE_WRITE:
          break;
        case Constants.MESSAGE_READ:
          byte[] readBuf = (byte[]) msg.obj;
          WebsocketMessage message = null;
          // construct a string from the valid bytes in the buffer
//          try {
//            message = convertFromBytes(readBuf);
//          }catch(IOException e){
//            Log.e("ERROR","DATA Cannot be send : "+ e);
//          }catch(ClassNotFoundException ce){
//            Log.e("ERROR","CLASS Cannot be found : "+ ce);
//          }
          if(message!=null)
          BluetoothController.this.handleMessage(message);
          break;
        case Constants.MESSAGE_DEVICE_NAME:
          BluetoothDevice otherDevice = msg.getData().getParcelable(Constants.DEVICE);
          boolean isAccepting = msg.getData().getBoolean(Constants.IS_ACCEPTING);
          Log.d("asdf", isAccepting + " ");

          if(isAccepting){
            for(WeakReference<BluetoothListener> listenerWeakReference : bluetoothListenerList){
              if(listenerWeakReference.get() != null){
                listenerWeakReference.get().invitedToMatch(otherDevice);
              }
            }
          }
          break;
        case Constants.MESSAGE_TOAST:
          Log.d(TAG, "toast: " + msg.getData().getString(Constants.TOAST));
          break;
      }
    }
  }

  private void handleMessage(WebsocketMessage websocketMessage ){

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


  private byte[] convertToBytes(WebsocketMessage object){
    byte[] data = gson.toJson(object).getBytes();
    return data;
  }

  public boolean isConnected(){
    return isConnected;
  }

  public GameState getGameState(){
    return gameState;
  }

}
