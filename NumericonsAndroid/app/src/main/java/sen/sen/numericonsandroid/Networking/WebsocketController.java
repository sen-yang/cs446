package sen.sen.numericonsandroid.Networking;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
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
import sen.sen.numericonsandroid.Models.User;
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
    void onConnected();
    void onClose();
    void loginConfirmed(boolean isConfirmed, User user);
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
  private User user;

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

  public User getUser(){
    return user;
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

    gson = builder.createGsonBuilder().registerTypeAdapter(Constants.MESSAGE_TYPE.class, new JsonDeserializer<Constants.MESSAGE_TYPE>(){
      @Override
      public Constants.MESSAGE_TYPE deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
        return Constants.MESSAGE_TYPE.valueOf(json.getAsInt());
      }
    }).registerTypeAdapter(Constants.PLAYER_ACTION_TYPE.class, new JsonDeserializer<Constants.PLAYER_ACTION_TYPE>(){
      @Override
      public Constants.PLAYER_ACTION_TYPE deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
        return Constants.PLAYER_ACTION_TYPE.valueOf(json.getAsInt());
      }
    }).create();
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
        login(android.os.Build.MODEL);

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
            listenerWeakReference.get().onConnected();
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
    WebsocketMessage websocketMessage = gson.fromJson(message, WebsocketMessage.class);
    Iterator<WeakReference<WebsocketListener>> listenerIterator = websocketListenerList.iterator();

    Log.d("asdf", "handle" + (websocketMessage.getType() == Constants.MESSAGE_TYPE.GAME_INIT));
    while(listenerIterator.hasNext()){
      WeakReference<WebsocketListener> listenerWeakReference = listenerIterator.next();

      if(listenerWeakReference.get() == null){
      }
      else{
        WebsocketListener websocketListener = listenerWeakReference.get();

        if(websocketListener != null){
          switch(websocketMessage.getType()){
            case PING:
              break;
            case LOGIN_CONFIRMATION:
              user = ((ConfirmationMessage)websocketMessage).getUser();
              websocketListener.loginConfirmed(((ConfirmationMessage)websocketMessage).isConfirmed(), ((ConfirmationMessage)websocketMessage).getUser());
              break;
            case GAME_INIT:
              Log.d("asdf", "handle");
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
