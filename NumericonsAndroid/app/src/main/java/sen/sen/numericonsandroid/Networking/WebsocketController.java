package sen.sen.numericonsandroid.Networking;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.gsonfire.GsonFireBuilder;
import io.gsonfire.TypeSelector;
import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Networking.WebsocketModels.ConfirmationMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.FindGameMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameDroppedItemMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameStateMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.LoginMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.PlayerActionMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.WebsocketMessage;


public class WebsocketController{
  public static final String TAG = "WebsocketController";
  private static WebsocketController staticWebsocketController;

  public interface WebsocketListener{
    void loginConfirmed(boolean isConfirmed);
    void gameInitialized(GameState gameState);
    void gameStarted(GameState gameState);
    void gameFinished(GameState gameState);
    void gameStateUpdated(GameState gameState);
    void itemDropped(DroppedItem droppedItem);
  }

  private Gson gson;
  private List<WeakReference<WebsocketListener>> websocketListenerList;
  private WebSocketClient webSocketClient;
  private boolean isConnected;

  public WebsocketController getInstance(){
    if(staticWebsocketController == null){
      staticWebsocketController = new WebsocketController();
      initialize();
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

  //temporary??
  public void login(String username){
    WebsocketMessage websocketMessage = new LoginMessage(username);
    webSocketClient.send(gson.toJson(websocketMessage));
  }

  public void lookForMatch(Constants.GAME_TYPE gameType){
    WebsocketMessage websocketMessage = new FindGameMessage(gameType);
    webSocketClient.send(gson.toJson(websocketMessage));
  }

  public void sendPlayerAction(PlayerAction playerAction){
    WebsocketMessage websocketMessage = new PlayerActionMessage(playerAction);
    webSocketClient.send(gson.toJson(websocketMessage));
  }

  public boolean isConnected(){
    return isConnected;
  }

  private void initialize(){
    GsonFireBuilder builder = new GsonFireBuilder()
        .registerTypeSelector(WebsocketMessage.class, new TypeSelector<WebsocketMessage>(){
          @Override
          public Class<? extends WebsocketMessage> getClassForElement(JsonElement readElement){
            int type = readElement.getAsJsonObject().get("type").getAsInt();
            Constants.MESSAGE_TYPE messageType = Constants.MESSAGE_TYPE.valueOf(type);
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
    websocketListenerList = new ArrayList<>();

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
    WebsocketMessage websocketMessage = gson.fromJson(message, WebsocketMessage.class);
    Iterator<WeakReference<WebsocketListener>> listenerIterator = websocketListenerList.iterator();

    while(listenerIterator.hasNext()){
      WeakReference<WebsocketListener> listenerWeakReference = listenerIterator.next();

      if(listenerIterator.next().get() == null){
        listenerIterator.remove();
      }
      else{
        WebsocketListener websocketListener = listenerWeakReference.get();

        if(websocketListener != null){
          switch(websocketMessage.getType()){
            case PING:
              break;
            case LOGIN_CONFIRMATION:
              websocketListener.loginConfirmed(((ConfirmationMessage)websocketMessage).isConfirmed());
              break;
            case GAME_INIT:
              websocketListener.gameInitialized(((GameStateMessage)websocketMessage).getGameState());
              break;
            case GAME_START:
              websocketListener.gameStarted(((GameStateMessage)websocketMessage).getGameState());
              break;
            case GAME_FINISH:
              websocketListener.gameFinished(((GameStateMessage)websocketMessage).getGameState());
              break;
            case GAME_STATE_UPDATE:
              websocketListener.gameStateUpdated(((GameStateMessage)websocketMessage).getGameState());
              break;
            case GAME_DROPPED_ITEM:
              websocketListener.itemDropped(((GameDroppedItemMessage)websocketMessage).getDroppedItem());
              break;
            default:
              break;
          }
        }
      }
    }
  }
}
