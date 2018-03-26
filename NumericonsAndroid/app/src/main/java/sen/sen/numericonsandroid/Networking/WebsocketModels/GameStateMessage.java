package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.GameState;

public class GameStateMessage extends WebsocketMessage{
  private GameState gameState;

  public GameStateMessage(Constants.MESSAGE_TYPE messageType, GameState gameState){
    super(messageType);
    this.gameState = gameState;
  }

  public GameState getGameState(){
    return gameState;
  }
}
