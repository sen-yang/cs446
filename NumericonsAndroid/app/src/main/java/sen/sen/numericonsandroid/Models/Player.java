package sen.sen.numericonsandroid.Models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.Serializable;
import java.lang.reflect.Type;

import sen.sen.numericonsandroid.Global.Constants;

public class Player implements Serializable{
  private int targetNumber;
  private int currentNumber = 0;
  private Constants.PLAYER_ACTION_TYPE currentOperation;
  private String username;
  private String imageUrlThumbnail;
  private DroppedItem itemInInventory;
  private boolean lost;
  private Constants.CHARACTER_SPRITE characterSprite;

  public interface ItemUsedCallback{
    void itemUsed(Constants.ITEM_TYPE itemType);
  }

  public Player(int targetNumber, User user){
    this.targetNumber = targetNumber;
    this.username = user.getUsername();
    this.characterSprite = user.getCharacterSprite();
    currentOperation = Constants.PLAYER_ACTION_TYPE.ADDITION;
    currentNumber = 0;
    lost = false;
    itemInInventory = null;
  }

  public void doPlayerAction(PlayerAction playerAction, ItemUsedCallback itemUsedCallback){
    switch(playerAction.getCommandType()){
      case ADDITION:
      case SUBTRACTION:
      case MULTIPLICATION:
      case DIVISION:
        this.currentOperation = playerAction.getCommandType();
        break;
      case GET_NUMBER:
        this.updateCurrentNumber(playerAction.getValue());
        break;
      case USE_ITEM:
        if(itemInInventory != null && itemUsedCallback != null){
          itemUsedCallback.itemUsed(itemInInventory.getItemType());
          itemInInventory = null;
        }
        break;
      case GET_ITEM:
        this.catchItem(playerAction.getItem());
        break;
    }
  }

  public void updateCurrentNumber(int newNumber){
    switch(this.currentOperation){
      case ADDITION:
        this.currentNumber += newNumber;
        break;
      case SUBTRACTION:
        this.currentNumber -= newNumber;
        break;
      case MULTIPLICATION:
        this.currentNumber *= newNumber;
        break;
      case DIVISION:
        if(newNumber == 0){
          this.lost = true;
          return;
        }
        this.currentNumber /= newNumber;
        break;
    }
  }

  public void catchItem(DroppedItem droppedItem){
    this.itemInInventory = droppedItem;
  }

  public int getTargetNumber(){
    return targetNumber;
  }

  public int getCurrentNumber(){
    return currentNumber;
  }

  public Constants.CHARACTER_SPRITE getCharacterSprite(){
    return characterSprite;
  }

  public void setCharacterSprite(Constants.CHARACTER_SPRITE characterSprite){
    this.characterSprite = characterSprite;
  }

  public Constants.PLAYER_ACTION_TYPE getCurrentOperation(){
    return currentOperation;
  }

  public String getUsername(){
    return username;
  }

  public String getImageUrlThumbnail(){
    return imageUrlThumbnail;
  }

  public boolean isLost(){
    return lost;
  }

  public void setLost(boolean lost){
    this.lost = lost;
  }

  public DroppedItem getItemInInventory(){
    return itemInInventory;
  }

  public static class PlayerAdapter implements JsonSerializer<Player>{
    @Override
    public JsonElement serialize(Player src, Type typeOfSrc, JsonSerializationContext context){
      JsonObject obj = new JsonObject();
      obj.addProperty("currentNumber", src.getCurrentNumber());
      obj.addProperty("username", src.getUsername());
      obj.addProperty("characterSprite", src.getCharacterSprite().toString());
      obj.add("itemInInventory", context.serialize(src.itemInInventory));

      return obj;
    }
  }
}
