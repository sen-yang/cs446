package sen.sen.numericonsandroid.Networking;

import android.util.Log;

import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.Helpers;
import sen.sen.numericonsandroid.Global.SharedPreferencesHelper;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketModels.ConfirmationMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.FindGameMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameStateMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GetRankingsMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.LoginMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.PlayerActionMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.RankingsMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.UserMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.WebsocketMessage;


public class WebsocketController{
  public static final String TAG = "WebsocketController";
  private static WebsocketController staticWebsocketController;

  public interface WebsocketListener{
    void onConnected();

    void onClose(int code, String reason, boolean remote);

    void userConfirmed(boolean isConfirmed, User user, String errorMessage);

    void gameInitialized(GameState gameState);

    void newUserList(List<User> newUserList, boolean isError);
  }

  private Gson gson;
  private List<WeakReference<WebsocketListener>> websocketListenerList;
  private List<WeakReference<GameListener>> gameListenerList;
  private WebSocketClient webSocketClient;
  private boolean isConnected;
  private GameState gameState;

  public static WebsocketController getInstance(){
    if(staticWebsocketController == null){
      staticWebsocketController = new WebsocketController();
      staticWebsocketController.initialize();
    }
    return staticWebsocketController;
  }

  public void addWebsocketListener(WebsocketListener websocketListener){
    boolean existsInList = false;
    for(WeakReference listenerReference : websocketListenerList){
      if(listenerReference.get() != null && listenerReference.get() == websocketListener){
        existsInList = true;
      }
    }
    if(existsInList == false){
      websocketListenerList.add(new WeakReference<WebsocketListener>(websocketListener));
    }
  }

  public void removeWebsocketListener(WebsocketListener websocketListener){
    ListIterator<WeakReference<WebsocketListener>> iterator = websocketListenerList.listIterator();
    while(iterator.hasNext()){
      WebsocketListener listener = iterator.next().get();

      if(listener.equals(websocketListener)){
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

  public void register(String username, String password){
    LoginMessage loginMessage = new LoginMessage(Constants.MESSAGE_TYPE.REGISTER);
    loginMessage.setUsername(username);
    loginMessage.setPassword(Helpers.fingerprintPassword(password));

    if(webSocketClient.isOpen()){
      webSocketClient.send(gson.toJson(loginMessage));
    }
  }

  public void login(String username, String password){
    LoginMessage loginMessage = new LoginMessage(Constants.MESSAGE_TYPE.LOGIN);
    loginMessage.setUsername(username);
    loginMessage.setPassword(Helpers.fingerprintPassword(password));

    if(webSocketClient.isOpen()){
      webSocketClient.send(gson.toJson(loginMessage));
    }
  }

  public void updateUser(User user){
    WebsocketMessage websocketMessage = new UserMessage(user);

    if(webSocketClient.isOpen()){
      webSocketClient.send(gson.toJson(websocketMessage));
    }
  }

  public void getRankings(int limit, int offset){
    WebsocketMessage websocketMessage = new GetRankingsMessage(limit, offset);

    if(webSocketClient.isOpen()){
      webSocketClient.send(gson.toJson(websocketMessage));
    }
  }

  public void lookForMatch(Constants.GAME_TYPE gameType){
    WebsocketMessage websocketMessage = new FindGameMessage(gameType);
    if(webSocketClient.isOpen()){
      webSocketClient.send(gson.toJson(websocketMessage));
    }
  }

  public void sendPlayerAction(PlayerAction playerAction){
    WebsocketMessage websocketMessage = new PlayerActionMessage(playerAction);
    if(webSocketClient.isOpen()){
      webSocketClient.send(gson.toJson(websocketMessage));
    }
  }

  public boolean isConnected(){
    return isConnected;
  }

  public GameState getGameState(){
    return gameState;
  }

  private void initialize(){
    gson = CustomGson.getGson();
    websocketListenerList = new ArrayList<>();
    gameListenerList = new ArrayList<>();

    URI uri;
    try{
      uri = new URI(Constants.SERVER_URL);
    } catch(URISyntaxException e){
      e.printStackTrace();
      return;
    }
    Map<String, String> headers = new HashMap<>();
    headers.put("Origin", Constants.SERVER_URL);
    this.webSocketClient = new WebSocketClient(uri, new Draft_6455(), headers, 1000){
      @Override
      public void onOpen(ServerHandshake handshakedata){
        Log.d(TAG, "onOpen");
        loginWithSessionOrTemporaryUser();

        for(WeakReference<WebsocketListener> listenerWeakReference : websocketListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().onConnected();
          }
        }
        isConnected = true;
      }

      @Override
      public void onMessage(String message){
        Log.d(TAG, "onMessage: " + message);
        handleMessage(message);
      }

      @Override
      public void onClose(int code, String reason, boolean remote){
        Log.d(TAG, "onClose: " + code + " " + reason);

        for(WeakReference<WebsocketListener> listenerWeakReference : websocketListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().onClose(code, reason, remote);
          }
        }
        for(WeakReference<GameListener> listenerWeakReference : gameListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().disconnected();
          }
        }
        isConnected = false;
      }

      @Override
      public void onError(Exception ex){
        Log.e(TAG, "onError: ", ex);
      }
    };
    webSocketClient.connect();
  }

  private void handleMessage(String message){
    Log.d(TAG, "Message received: " + message);
    WebsocketMessage websocketMessage = gson.fromJson(message, WebsocketMessage.class);

    switch(websocketMessage.getType()){
      case PING:
        break;
      case CONFIRMATION:
        handleConfirmationMessage((ConfirmationMessage) websocketMessage);
        break;
      case GAME_INIT:
        gameState = ((GameStateMessage) websocketMessage).getGameState();

        for(WeakReference<WebsocketListener> listenerWeakReference : websocketListenerList){
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
      case GET_RANKINGS:
        List<User> userList = ((RankingsMessage) websocketMessage).getUserList();
        boolean isError = ((RankingsMessage) websocketMessage).isError();

        for(WeakReference<WebsocketListener> listenerWeakReference : websocketListenerList){
          if(listenerWeakReference.get() != null){
            listenerWeakReference.get().newUserList(userList, isError);
          }
        }
      default:
        break;
    }
  }

  private void handleConfirmationMessage(ConfirmationMessage confirmationMessage){
    if(confirmationMessage.getConfirmationType() == Constants.CONFIRMATION_TYPE.USER_CONFIRMATION){
      SharedPreferencesHelper.saveUser(confirmationMessage.getUser());

      if(Helpers.isNonEmptyString(confirmationMessage.getSessionID())){
        SharedPreferencesHelper.saveSessionID(confirmationMessage.getSessionID());
      }

      for(WeakReference<WebsocketListener> listenerWeakReference : websocketListenerList){
        if(listenerWeakReference.get() != null){
          listenerWeakReference.get().userConfirmed(confirmationMessage.isConfirmed(), SharedPreferencesHelper.getSavedUser(), confirmationMessage.getErrorMessage());
        }
      }
    }
  }

  private void loginWithSessionOrTemporaryUser(){
    LoginMessage loginMessage = new LoginMessage(Constants.MESSAGE_TYPE.LOGIN);
    String sessionID = SharedPreferencesHelper.getSavedSessionID();

    if(Helpers.isNonEmptyString(sessionID)){
      loginMessage.setSessionID(sessionID);
    }
    else{
      loginMessage.setUsername(android.os.Build.MODEL + UUID.randomUUID().toString());
    }
    if(webSocketClient.isOpen()){
      webSocketClient.send(gson.toJson(loginMessage));
    }
  }
}
