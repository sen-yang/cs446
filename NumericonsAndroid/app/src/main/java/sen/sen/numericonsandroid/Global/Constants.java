package sen.sen.numericonsandroid.Global;


import java.util.HashMap;
import java.util.Map;

public class Constants{
  public static final String SERVER_URL = "ws://sen.ddns.net";

  public enum MESSAGE_TYPE{
    PING(0),

    //server messages
    LOGIN_CONFIRMATION(1),
    GAME_INIT(2),
    GAME_START(3),
    GAME_FINISH(4),
    GAME_STATE_UPDATE(5),
    GAME_DROPPED_ITEM(6),

    //client messages
    LOGIN(100),
    FIND_GAME(101),
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
    ADDITION(0),
    SUBTRACTION(1),
    MULTIPLICATION(2),
    DIVISION(3),
    GET_NUMBER(4),
    USE_POWER_UP(5);

    private int value;
    private static Map map = new HashMap<>();

    PLAYER_ACTION_TYPE(int value){
      this.value = value;
    }

    public int getValue(){
      return value;
    }
  }
}
