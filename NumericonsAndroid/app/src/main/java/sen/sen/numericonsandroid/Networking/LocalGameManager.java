package sen.sen.numericonsandroid.Networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.Helpers;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;

public class LocalGameManager implements Serializable{
  private String seed;
  private GameState gameState;
  private Thread gameLoop;
  private GameListener gameListener;

  public LocalGameManager(String seedString, List<User> userList){
    this.seed = seedString;
    this.gameLoop = null;
    List<Player> playerList = new ArrayList<>();
    gameState = new GameState(seedString, playerList);

    for(User user : userList){
      playerList.add(new Player(gameState.getTargetNumber(), user));
    }
  }

  public void startGame(GameListener gameListener){
    if(gameState != null){
      this.gameListener = gameListener;
      gameState.setStartTime(System.currentTimeMillis());
      gameState.setPreviousTickTime(gameState.getStartTime());
      startTick();
    }
  }

  public boolean isRunning(){
    return (gameLoop != null) && (gameLoop.isAlive());
  }

  public void playerLeft(Player player){
    gameState.setComplete(true);
    gameState.setLoser(player);
  }

  public void setGameListener(GameListener gameListener){
    this.gameListener = gameListener;
  }

  public void playerActionPerformed(String username, PlayerAction playerAction){
    Player player = this.findPlayerByUsername(username);

    if(player != null){
      player.doPlayerAction(playerAction);
    }
  }

  public void userLeft(String username){
    this.gameState.setComplete(true);
    Player player = this.findPlayerByUsername(username);

    if(player != null){
      this.gameState.setLoser(player);
    }
  }

  public GameState getGameState(){
    return gameState;
  }

  public void stopAndClean(){
    if((gameLoop != null) && gameLoop.isAlive()){
      gameLoop.interrupt();
    }
    gameLoop = null;
    gameListener = null;
    gameState = null;
  }

  private void startTick(){
    gameLoop = new Thread(){
      @Override
      public void run(){
        while(interrupted() == false){
          try{
            updateGame();
            if(gameState.isComplete()){
              break;
            }
            Thread.sleep(Constants.TICK_TIME);
          } catch(Exception e){
          }
        }
      }
    };
    gameLoop.start();
  }

  private void updateGame(){
    long now = System.currentTimeMillis();
    gameState.setDelta(now - gameState.getPreviousTickTime());
    gameState.setPreviousTickTime(now);
    gameState.setTimeRemaining(gameState.getTimeRemaining() - gameState.getDelta());

    for(Player player : gameState.getPlayerList()){
      if(player.getCurrentNumber() == gameState.getTargetNumber()){
        gameState.setComplete(true);
        gameState.setWinner(player);
        break;
      }
      else if(player.isLost()){
        gameState.setComplete(true);
        gameState.setLoser(player);
        break;
      }
    }

    if((gameState.getWinner() == null) && (gameState.getLoser() == null) && (gameState.getTimeRemaining() <= 0)){
      gameState.setComplete(true);

      int minDifference = Integer.MAX_VALUE;
      Player minPlayer = null;

      for(Player player : gameState.getPlayerList()){
        int difference = Math.abs(gameState.getTargetNumber() - player.getCurrentNumber());

        if(difference < minDifference){
          minDifference = difference;
          minPlayer = player;
        }
      }
      gameState.setWinner(minPlayer);
    }

    if(gameListener != null){
      if(gameState.isComplete()){
        gameListener.gameFinished(this.gameState);
      }
      if((Helpers.randomFloat(seed) * Constants.DROP_RATE) > gameState.getDelta()){
        List<DroppedItem> droppedItemList = new ArrayList<>();
        droppedItemList.add(generateDrop());
        gameState.setDroppedItemList(droppedItemList);
        gameListener.gameStateUpdated(this.gameState);
      }
      else{
        gameListener.gameStateUpdated(this.gameState);
      }
    }
  }

  private DroppedItem generateDrop(){
    return new DroppedItem(Helpers.randomIntInRange(Constants.MIN_DROP, Constants.MAX_DROP, this.seed), Helpers.randomFloat(this.seed), Helpers.randomFloatInRange(Constants.MIN_DROP_SPEED, Constants.MAX_DROP_SPEED, seed));
  }

  private Player findPlayerByUsername(String username){
    for(Player player : this.gameState.getPlayerList()){
      if(player.getUsername().equals(username)){
        return player;
      }
    }
    return null;
  }
}
