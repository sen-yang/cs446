package sen.sen.numericonsandroid.Global;

import sen.sen.numericonsandroid.R;

public class Constants{
  public static final String SERVER_URL = "ws://sen.ddns.net";
//  public static final String SERVER_URL = "ws://192.168.1.30:6969";

  public static final String GAME_CONTROLLER = "GAME_CONTROLLER";

  public static final int TOTAL_GAME_TIME = 60 * 1000;

  public static final int INFINITE_LOAD_SIZE = 20;

  public static final int INFINITE_LOAD_TRIGGER_SIZE = 3;

  //game manager constants
  public static final int TICK_TIME = 1000 / 5; // (1s in ms) / fps
  public static final long GAME_READY_TIME = 5000;
  public static final int MIN_TARGET = -999;
  public static final int MAX_TARGET = 999;
  public static final int MIN_DROP = -9;
  public static final int MAX_DROP = 9;
  public static final float MIN_DROP_SPEED = 0.01f;
  public static final float MAX_DROP_SPEED = 0.05f;
  public static final float DROP_RATE = 225;
  public static final long GAME_START_DELAY = 5000;
  public static long FRAME_TIME = 1000 / 60; // (1s in ms) / fps

  public static long GLOBAL_ITEM_EFFECT_DURATION = 5000;
  public static float EFFECT_ITEM_DROP_CHANCE = 0.04f;

  public enum MESSAGE_TYPE{
    PING,

    //server messages
    CONFIRMATION,
    GAME_INIT,
    GAME_START,
    GAME_FINISH,
    GAME_STATE_UPDATE,
    GAME_FOUND,
    GET_RANKINGS,

    //client messages
    LOGIN,
    REGISTER,
    UPDATE_USER,
    FIND_GAME,
    PLAYER_ACTION
  }

  public enum GAME_TYPE{
    SINGLEPLAYER,
    RANKED,
    GROUP_GAME,
    SECRET_MODE,
    CANCEL
  }

  public enum ITEM_TYPE{
    NUMBER,
    SPEED_INCREASE
  }

  public enum PLAYER_ACTION_TYPE{
    ADDITION,
    SUBTRACTION,
    MULTIPLICATION,
    DIVISION,
    GET_NUMBER,
    USE_ITEM,
    GET_ITEM
  }

  public enum CONFIRMATION_TYPE{
    BLUETOOTH_ACCEPT_GAME,
    USER_CONFIRMATION
  }

  public enum CHARACTER_SPRITE{
    BIRD_1(R.raw.chirp),
    BIRD_2(R.raw.quack),
    BIRD_3(R.raw.hoot);

    private int soundEffectResId;

    CHARACTER_SPRITE(int soundEffectResId){
      this.soundEffectResId = soundEffectResId;
    }

    public int getSoundEffectResId(){
      return soundEffectResId;
    }
  }

  public enum BIRD_DIRECTION{
    LEFT,
    RIGHT
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
