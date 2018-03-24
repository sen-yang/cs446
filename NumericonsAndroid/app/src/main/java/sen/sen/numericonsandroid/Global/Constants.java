package sen.sen.numericonsandroid.Global;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Constants{
//  public static final String SERVER_URL = "ws://sen.ddns.net";
  public static final String SERVER_URL = "ws://192.168.1.30:6969";

  public static final String GAME_CONTROLLER = "GAME_CONTROLLER";

  public static final int TOTAL_GAME_TIME = 60 * 1000;

  public static final int INFINITE_LOAD_SIZE = 20;

  public static final int INFINITE_LOAD_TRIIGER_SIZE = 3;


  public enum MESSAGE_TYPE{
    PING,

    //server messages
    LOGIN_CONFIRMATION,
    GAME_INIT,
    GAME_START,
    GAME_FINISH,
    GAME_STATE_UPDATE,
    GAME_DROPPED_ITEM,
    GAME_FOUND,
    GET_RANKINGS,

    //client messages
    LOGIN,
    FIND_GAME,
    PLAYER_ACTION;
  }

  public enum GAME_TYPE{
    SINGLEPLAYER,
    RANKED,
    GROUP_GAME,
    SECRET_MODE
  }

  public enum PLAYER_ACTION_TYPE{
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    GET_NUMBER,
    USE_POWER_UP;
  }

  public enum GAME_STAGE{
    INIT,
    RUNNING,
    FINISHED
  }


  //bluetooth messaging constants



  public static final int MESSAGE_STATE_CHANGE = 1;
  public static final int MESSAGE_READ = 2;
  public static final int MESSAGE_WRITE = 3;
  public static final int MESSAGE_DEVICE_NAME = 4;
  public static final int MESSAGE_TOAST = 5;

  public static final String DEVICE = "DEVICE";
  public static final String IS_ACCEPTING = "IS_ACCEPTING";
  public static final String TOAST = "TOAST";
}
