package sen.sen.numericonsandroid.Global;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Constants{
  public static final String SERVER_URL = "ws://sen.ddns.net";

  public static final String GAME_CONTROLLER = "GAME_CONTROLLER";

  public static final int TOTAL_GAME_TIME = 60 * 1000;

  public enum MESSAGE_TYPE{
    @SerializedName("0")
    PING(0),

    //server messages
    @SerializedName("1")
    LOGIN_CONFIRMATION(1),
    @SerializedName("2")
    GAME_INIT(2),
    @SerializedName("3")
    GAME_START(3),
    @SerializedName("4")
    GAME_FINISH(4),
    @SerializedName("5")
    GAME_STATE_UPDATE(5),
    @SerializedName("6")
    GAME_DROPPED_ITEM(6),
    @SerializedName("7")
    GAME_FOUND(7),

    //client messages
    @SerializedName("100")
    LOGIN(100),
    @SerializedName("101")
    FIND_GAME(101),
    @SerializedName("102")
    PLAYER_ACTION(102);

    private int value;
    private static Map map = new HashMap<>();

    MESSAGE_TYPE(int value){
      this.value = value;
    }

    static{
      for(MESSAGE_TYPE messageType : MESSAGE_TYPE.values()){
        map.put(messageType.value, messageType);
      }
    }

    public static MESSAGE_TYPE valueOf(int pageType){
      return (MESSAGE_TYPE) map.get(pageType);
    }

    public int getValue(){
      return value;
    }
  }

  public enum GAME_TYPE{
    SINGLEPLAYER,
    RANKED,
    SECRET_MODE
  }

  public enum PLAYER_ACTION_TYPE{
    @SerializedName("0")
    ADDITION(0),
    @SerializedName("1")
    SUBTRACTION(1),
    @SerializedName("2")
    MULTIPLICATION(2),
    @SerializedName("3")
    DIVISION(3),
    @SerializedName("4")
    GET_NUMBER(4),
    @SerializedName("5")
    USE_POWER_UP(5);

    private int value;
    private static Map map = new HashMap<>();

    PLAYER_ACTION_TYPE(int value){
      this.value = value;
    }

    static{
      for(PLAYER_ACTION_TYPE messageType : PLAYER_ACTION_TYPE.values()){
        map.put(messageType.value, messageType);
      }
    }

    public static PLAYER_ACTION_TYPE valueOf(int pageType){
      return (PLAYER_ACTION_TYPE) map.get(pageType);
    }

    public int getValue(){
      return value;
    }
  }

  public enum GAME_STAGE{
    INIT,
    RUNNING,
    FINISHED
  }
}
