package sen.sen.numericonsandroid.Networking.WebsocketModels;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.GameState;

public class GameStateMessage extends WebsocketMessage{
  private GameState gameState;

  public GameState getGameState(){
    return gameState;
  }
}
