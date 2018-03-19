package sen.sen.numericonsandroid.Networking;

import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.User;

public interface ServerListener{
  void onConnected();
  void onClose();
  void loginConfirmed(boolean isConfirmed, User user);
  void InvitedToGame(User hostUser);
  void gameInitialized(GameState gameState);
  void gameStarted(GameState gameState);
  void gameFinished(GameState gameState);
  void gameStateUpdated(GameState gameState);
  void itemDropped(DroppedItem droppedItem);
}
