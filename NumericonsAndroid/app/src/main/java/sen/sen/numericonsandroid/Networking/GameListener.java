package sen.sen.numericonsandroid.Networking;

import sen.sen.numericonsandroid.Models.GameState;

public interface GameListener{
  void disconnected();
  void gameStarted(GameState gameState);
  void gameFinished(GameState gameState);
  void gameStateUpdated(GameState gameState);
}
