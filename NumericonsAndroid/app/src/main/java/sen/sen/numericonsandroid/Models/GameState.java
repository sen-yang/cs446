package sen.sen.numericonsandroid.Models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.Helpers;
import sen.sen.numericonsandroid.Networking.CustomGson;

import static sen.sen.numericonsandroid.Global.Constants.TOTAL_GAME_TIME;

public class GameState implements Serializable{
  private String seed;
  private int targetNumber;
  private Constants.GAME_TYPE matchType;
  private boolean isComplete;
  private Player winner;
  private Player loser;
  private long previousTickTime;
  private long delta;
  private long startTime;
  private List<Player> playerList;
  private long timeRemaining;
  private List<DroppedItem> droppedItemList;
  private Constants.ITEM_TYPE globalEffect;
  private long globalEffectTimeRemaining = 0;

  public GameState(String seedString, List<Player> playerList){
    //todo generate from seed
    this.seed = seedString;
    this.targetNumber = Helpers.randomIntInRange(Constants.MIN_TARGET, Constants.MAX_TARGET, seed);
    this.isComplete = false;
    this.winner = null;
    this.loser = null;
    this.timeRemaining = TOTAL_GAME_TIME;
    this.previousTickTime = 0;
    this.delta = 0;
    this.startTime = 0;
    this.playerList = playerList;
  }

  public Player getWinner(){
    Player winner = this.winner;

    if((winner == null) && this.isComplete && (this.playerList != null) && (this.playerList.size() > 1)){
      for(Player player : playerList){
        if(player != this.loser){
          winner = player;
          break;
        }
      }
    }
    return winner;
  }

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

  public String getSeed(){
    return seed;
  }

  public void setSeed(String seed){
    this.seed = seed;
  }

  public Player getLoser(){
    return loser;
  }

  public void setLoser(Player loser){
    this.loser = loser;
  }

  public long getPreviousTickTime(){
    return previousTickTime;
  }

  public void setPreviousTickTime(long previousTickTime){
    this.previousTickTime = previousTickTime;
  }

  public long getDelta(){
    return delta;
  }

  public void setDelta(long delta){
    this.delta = delta;
  }

  public long getStartTime(){
    return startTime;
  }

  public void setStartTime(long startTime){
    this.startTime = startTime;
  }

  public Constants.ITEM_TYPE getGlobalEffect(){
    return globalEffect;
  }

  public void setGlobalEffect(Constants.ITEM_TYPE globalEffect){
    this.globalEffect = globalEffect;
    setGlobalEffectTimeRemaining(Constants.GLOBAL_ITEM_EFFECT_DURATION);
  }

  public long getGlobalEffectTimeRemaining(){
    return globalEffectTimeRemaining;
  }

  public void setGlobalEffectTimeRemaining(long globalEffectTimeRemaining){
    this.globalEffectTimeRemaining = globalEffectTimeRemaining;
  }

  public static class GameStateAdapter implements JsonSerializer<GameState>{
    @Override
    public JsonElement serialize(GameState src, Type typeOfSrc, JsonSerializationContext context){
      JsonObject obj = new JsonObject();
      obj.addProperty("targetNumber", src.getTargetNumber());
      obj.add("playerList", context.serialize(src.getPlayerList()));
      obj.addProperty("isComplete", src.isComplete());
      obj.add("winner", context.serialize(src.getWinner()));
      obj.addProperty("timeRemaining", src.getTimeRemaining());
      obj.add("globalEffect", context.serialize(src.getGlobalEffect()));
      obj.add("droppedItemList", context.serialize(src.getDroppedItemList()));

      return obj;
    }
  }
}
