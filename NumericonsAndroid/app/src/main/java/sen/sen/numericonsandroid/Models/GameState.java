package sen.sen.numericonsandroid.Models;

import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;

public class GameState{
  private int targetNumber;
  private Constants.GAME_TYPE matchType;
  private List<Player> playerList;
  private boolean isComplete;
  private Player winner;
  private long timeRemaining;
  private List<DroppedItem> droppedItemList;
}
