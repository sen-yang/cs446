package sen.sen.numericonsandroid.Networking;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.SharedPreferencesHelper;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketModels.ConfirmationMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameStateMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.PlayerActionMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.WebsocketMessage;

import static sen.sen.numericonsandroid.Global.Constants.CONFIRMATION_TYPE.BLUETOOTH_ACCEPT_GAME;


public class BluetoothController{
  public static final String TAG = "BluetoothController";
  private static BluetoothController staticBluetoothController;

  public interface BluetoothListener{
    void onConnected();

    void onClose(int code, String reason, boolean remote);

    void invitedToMatch(BluetoothDevice device);

    void matchConfirmed(BluetoothDevice device, boolean isConfirmed);

    void gameInitialized(GameState gameState);
  }

  private List<WeakReference<BluetoothListener>> bluetoothListenerList;
  private List<WeakReference<GameListener>> gameListenerList;
  private BluetoothChatService bluetoothChatService;
  private Gson gson;
  private User opponentUser;
  private GameState gameState;
  private boolean isHost;
  private boolean isConnected;
  private LocalGameManager localGameManager;

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

  public void removeBluetoothListener(BluetoothListener bluetoothListener){
    ListIterator<WeakReference<BluetoothListener>> iterator = bluetoothListenerList.listIterator();
    while(iterator.hasNext()){
      BluetoothListener listener = iterator.next().get();

      if(listener.equals(bluetoothListener)){
        iterator.remove();
        break;
      }
    }
  }

  public void removeGameListener(GameListener gameListener){
    ListIterator<WeakReference<GameListener>> iterator = gameListenerList.listIterator();
    while(iterator.hasNext()){
      GameListener listener = iterator.next().get();

      if(listener.equals(gameListener)){
        iterator.remove();
        break;
      }
    }
  }

  public void listenForInvites(boolean shouldListen){
    if(shouldListen){
      bluetoothChatService.start();
    }
    else{
      bluetoothChatService.stopAccepting();
    }
  }

  public void inviteDeviceToGame(BluetoothDevice device){
    bluetoothChatService.connect(device, true);
    isHost = true;
  }


  public void respondToInvitation(BluetoothDevice device, boolean accepted){
    if(bluetoothChatService.getConnectedDevice() != null && bluetoothChatService.getConnectedDevice().equals(device)){
      WebsocketMessage websocketMessage = new ConfirmationMessage(accepted, BLUETOOTH_ACCEPT_GAME, SharedPreferencesHelper.getSavedUser());
      bluetoothChatService.write(gson.toJson(websocketMessage).getBytes());

      if(accepted){
        isHost = false;
      }
    }
  }


  public void sendPlayerAction(PlayerAction playerAction){
    if(isHost){
      localGameManager.playerActionPerformed(SharedPreferencesHelper.getUsername(), playerAction);
    }
    else{
      bluetoothChatService.write(gson.toJson(new PlayerActionMessage(playerAction)).getBytes());
    }
  }

  private void initialize(){
    bluetoothChatService = new BluetoothChatService(new IncomingBluetoothMessageHandler());
    bluetoothListenerList = new ArrayList<>();
    gameListenerList = new ArrayList<>();

    gson = CustomGson.getGson();
    reset();
  }

  private void reset(){
    isHost = false;
    opponentUser = null;
    gameState = null;
    isConnected = false;

    if(localGameManager != null){
      localGameManager.stopAndClean();
    }
    localGameManager = null;
  }

  private class IncomingBluetoothMessageHandler extends Handler{
    @Override
    public void handleMessage(Message msg){
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
              for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
                if(listenerWeakReference.get() != null){
                  listenerWeakReference.get().disconnected();
                }
              }
              if(isHost && localGameManager != null){
                localGameManager.userLeft(opponentUser.getUsername());
              }
              break;
          }
          break;
        case Constants.MESSAGE_WRITE:
          break;
        case Constants.MESSAGE_READ:
          byte[] readBuffer = (byte[]) msg.obj;
          String message = new String(readBuffer, 0, msg.arg1);
          BluetoothController.this.handleMessage(message);
          break;
        case Constants.MESSAGE_DEVICE_NAME:
          BluetoothDevice otherDevice = msg.getData().getParcelable(Constants.DEVICE);
          boolean isAccepting = msg.getData().getBoolean(Constants.IS_ACCEPTING);

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

  private void handleMessage(String message){
    WebsocketMessage websocketMessage = null;
    try{
      websocketMessage = gson.fromJson(message, WebsocketMessage.class);
    } catch(JsonSyntaxException e){
      e.printStackTrace();
    }
    if(websocketMessage == null){
      return;
    }
    if(isHost){
      switch(websocketMessage.getType()){
        case PLAYER_ACTION:
          localGameManager.playerActionPerformed(opponentUser.getUsername(), ((PlayerActionMessage) websocketMessage).getPlayerAction());
          break;
        case CONFIRMATION:
          handleConfirmationMessage((ConfirmationMessage) websocketMessage);
          break;
      }
    }
    else{
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
          reset();
          break;
        case GAME_STATE_UPDATE:
          gameState = ((GameStateMessage) websocketMessage).getGameState();
          for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
            if(listenerWeakReference.get() != null){
              listenerWeakReference.get().gameStateUpdated(gameState);
            }
          }
          break;
        default:
          break;
      }
    }
  }

  private void handleConfirmationMessage(ConfirmationMessage confirmationMessage){
    if(confirmationMessage.getConfirmationType() == BLUETOOTH_ACCEPT_GAME){
      for(WeakReference<BluetoothListener> listenerWeakReference : bluetoothListenerList){
        if(listenerWeakReference.get() != null){
          listenerWeakReference.get().matchConfirmed(bluetoothChatService.getConnectedDevice(), confirmationMessage.isConfirmed());
        }
      }
      if(confirmationMessage.isConfirmed()){
        isHost = true;
        List<User> userList = new ArrayList<>();
        opponentUser = confirmationMessage.getUser();
        userList.add(SharedPreferencesHelper.getSavedUser());
        userList.add(opponentUser);
        localGameManager = new LocalGameManager(UUID.randomUUID().toString(), userList);
        startGame();
      }
      else{
        isHost = false;
      }
    }
  }

  private void startGame(){
    WebsocketMessage websocketMessage = new GameStateMessage(Constants.MESSAGE_TYPE.GAME_INIT, localGameManager.getGameState());
    bluetoothChatService.write(gson.toJson(websocketMessage).getBytes());

    for(WeakReference<BluetoothListener> listenerWeakReference : bluetoothListenerList){
      if(listenerWeakReference.get() != null){
        listenerWeakReference.get().gameInitialized(localGameManager.getGameState());
      }
    }

    final Handler handler = new Handler();
    handler.postDelayed(new Runnable(){
      @Override
      public void run(){
        localGameManager.startGame(localGameManagerListener);
      }
    }, Constants.GAME_READY_TIME);
  }

  private GameListener localGameManagerListener = new GameListener(){
    @Override
    public void disconnected(){
      for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
        if(listenerWeakReference.get() != null){
          listenerWeakReference.get().disconnected();
        }
      }
    }

    @Override
    public void gameStarted(GameState gameState){
      WebsocketMessage websocketMessage = new GameStateMessage(Constants.MESSAGE_TYPE.GAME_START, gameState);
      bluetoothChatService.write(gson.toJson(websocketMessage).getBytes());

      for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
        if(listenerWeakReference.get() != null){
          listenerWeakReference.get().gameStarted(gameState);
        }
      }
    }

    @Override
    public void gameFinished(GameState gameState){
      WebsocketMessage websocketMessage = new GameStateMessage(Constants.MESSAGE_TYPE.GAME_FINISH, gameState);
      bluetoothChatService.write(gson.toJson(websocketMessage).getBytes());

      for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
        if(listenerWeakReference.get() != null){
          listenerWeakReference.get().gameFinished(gameState);
        }
      }
      reset();
    }

    @Override
    public void gameStateUpdated(GameState gameState){
      WebsocketMessage websocketMessage = new GameStateMessage(Constants.MESSAGE_TYPE.GAME_STATE_UPDATE, gameState);
      bluetoothChatService.write(gson.toJson(websocketMessage).getBytes());

      for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
        if(listenerWeakReference.get() != null){
          listenerWeakReference.get().gameStateUpdated(gameState);
        }
      }
    }
  };

  public boolean isConnected(){
    return isConnected;
  }

  public GameState getGameState(){
    if(isHost){
      return localGameManager.getGameState();
    }
    else{
      return gameState;
    }
  }

}
