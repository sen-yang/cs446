package sen.sen.numericonsandroid.Models;

import java.io.Serializable;
import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;

public class GameState implements Serializable{
  private int targetNumber;
  private Constants.GAME_TYPE matchType;
  private List<Player> playerList;
  private boolean isComplete;
  private Player winner;
  private long timeRemaining;
  private List<DroppedItem> droppedItemList;
  private Basket basket;

  public int getTargetNumber(){
    return targetNumber;
  }

  public void setTargetNumber(int targetNumber){
    this.targetNumber = targetNumber;
  }

  public Constants.GAME_TYPE getMatchType(){
    return matchType;
  }

  public void setMatchType(Constants.GAME_TYPE matchType){
    this.matchType = matchType;
  }

  public List<Player> getPlayerList(){
    return playerList;
  }

  public void setPlayerList(List<Player> playerList){
    this.playerList = playerList;
  }

  public boolean isComplete(){
    return isComplete;
  }

  public void setComplete(boolean complete){
    isComplete = complete;
  }

  public Player getWinner(){
    return winner;
  }

  public void setWinner(Player winner){
    this.winner = winner;
  }

  public long getTimeRemaining(){
    return timeRemaining;
  }

  public void setTimeRemaining(long timeRemaining){
    this.timeRemaining = timeRemaining;
  }

  public List<DroppedItem> getDroppedItemList(){
    return droppedItemList;
  }

  public void setDroppedItemList(List<DroppedItem> droppedItemList){
    this.droppedItemList = droppedItemList;
  }

  public Basket getBasket(){
    return basket;
  }

  public void setBasket(Basket basket){
    this.basket = basket;
  }
}
