package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Models.DroppedItem;

public class GameDroppedItemMessage extends WebsocketMessage{
  private DroppedItem droppedItem;

  public DroppedItem getDroppedItem(){
    return droppedItem;
  }
}
