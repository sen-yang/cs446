package sen.sen.numericonsandroid.Networking;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.gsonfire.GsonFireBuilder;
import io.gsonfire.TypeSelector;
import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.Networking.WebsocketModels.ConfirmationMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.FindGameMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.GameStateMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.LoginMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.PlayerActionMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.RankingsMessage;
import sen.sen.numericonsandroid.Networking.WebsocketModels.WebsocketMessage;

public class CustomGson{
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.FIELD)
  public @interface Exclude{
  }

  private static class AnnotationExclusionStrategy implements ExclusionStrategy{

    @Override
    public boolean shouldSkipField(FieldAttributes f){
      return f.getAnnotation(Exclude.class) != null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz){
      return false;
    }
  }

  public static Gson getGson(){
    GsonFireBuilder builder = new GsonFireBuilder()
        .registerTypeSelector(WebsocketMessage.class, new TypeSelector<WebsocketMessage>(){
          @Override
          public Class<? extends WebsocketMessage> getClassForElement(JsonElement readElement){
            Constants.MESSAGE_TYPE messageType = Constants.MESSAGE_TYPE.valueOf(readElement.getAsJsonObject().get("type").getAsString());
            switch(messageType){
              case PING:
                return WebsocketMessage.class;
              case CONFIRMATION:
                return ConfirmationMessage.class;
              case GAME_INIT:
              case GAME_START:
              case GAME_FINISH:
              case GAME_STATE_UPDATE:
                return GameStateMessage.class;
              case LOGIN:
                return LoginMessage.class;
              case FIND_GAME:
                return FindGameMessage.class;
              case PLAYER_ACTION:
                return PlayerActionMessage.class;
              case GET_RANKINGS:
                return RankingsMessage.class;
            }
            return null;
          }
        });
    return builder.createGsonBuilder()
                  .registerTypeAdapter(Player.class, new Player.PlayerAdapter())
                  .registerTypeAdapter(GameState.class, new GameState.GameStateAdapter())
                  .setExclusionStrategies(new AnnotationExclusionStrategy()).create();
  }
}
