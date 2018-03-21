package sen.sen.numericonsandroid.Activities;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sen.sen.numericonsandroid.CustomUI.GameView;
import sen.sen.numericonsandroid.CustomUI.MultiPlayerMode_PlayerInfoView;
import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Networking.GameController;
import sen.sen.numericonsandroid.Networking.GameListener;
import sen.sen.numericonsandroid.R;

import static sen.sen.numericonsandroid.Global.Constants.PLAYER_ACTION_TYPE.GET_NUMBER;
import static sen.sen.numericonsandroid.Global.Constants.TOTAL_GAME_TIME;

public class MainGameActivity extends AppCompatActivity implements GameListener, GameView.GameViewDelegate{

  // View widgets
  //@TODO change GameView to GameView!
  GameView gameView;
  TextView targetNumberTextView;
  TextView totalNumberTextView;
  ProgressBar countDownTimer;

  //Buttons
  Button addButton;
  Button subButton;
  Button multiplyButton;
  Button divideButton;

  //Private GameState Items
  List<DroppedItem> droppedItemList;

  private GameController gameController;
  private Player currentPlayer;
  private Constants.GAME_STAGE gameStage;

  Constants.PLAYER_ACTION_TYPE operationMode = Constants.PLAYER_ACTION_TYPE.ADDITION;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_game);

    //Setup game properties
    gameController = (GameController) getIntent().getSerializableExtra(Constants.GAME_CONTROLLER);
    gameStage = Constants.GAME_STAGE.INIT;

    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    gameView = findViewById(R.id.background);
    gameView.setDelegate(this);

    if(gameController.getGameState().getMatchType() == Constants.GAME_TYPE.RANKED) {
      MultiPlayerMode_PlayerInfoView multiPlayerModePlayerInfoView = new MultiPlayerMode_PlayerInfoView(gameView.getContext(), gameController.getGameState().getPlayerList());
      multiPlayerModePlayerInfoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
      gameView.addView(multiPlayerModePlayerInfoView);
    }

    countDownTimer = findViewById(R.id.countDownTimer);
    //Setup Buttons References
    addButton = findViewById(R.id.buttonAdd);
    subButton = findViewById(R.id.buttonMinus);
    multiplyButton = findViewById(R.id.buttonTimes);
    divideButton = findViewById(R.id.buttonDiv);

    //Setup Event Listener
    addButton.setOnClickListener(addHandler);
    subButton.setOnClickListener(subHandler);
    multiplyButton.setOnClickListener(multHandler);
    divideButton.setOnClickListener(divideHandler);

    targetNumberTextView = findViewById(R.id.targetNumberTextView);
    //@TODO: change "currentNumberTextView" to totalNumberTextView.
    totalNumberTextView = findViewById(R.id.currentNumberTextView);

    droppedItemList = new ArrayList<>();
    gameController.addGameListener(this);
    updateFromServer(gameController.getGameState());
  }

  int randomInt_Range(int min, int max){
    return min + (int) (Math.random() * ((max - min) + 1));
  }

  float randomFloat_Range(float min, float max){
    Random r = new Random();
    return min + r.nextFloat() * (max - min);
  }

  private void updateFromServer(GameState gameState) {
    if(this.isFinishing()){
      return;
    }
    targetNumberTextView.setText(Integer.toString(gameState.getTargetNumber()));

    for(Player player : gameState.getPlayerList()){
      if(player.getUsername().equals(gameController.getUser().getUsername())){
        currentPlayer = player;
        break;
      }
    }
    totalNumberTextView.setText(Integer.toString(currentPlayer.getCurrentNumber()));
    countDownTimer.setProgress((int) (((float) gameState.getTimeRemaining()) / TOTAL_GAME_TIME) * 100);

    //todo show other players

    if(gameStage == Constants.GAME_STAGE.FINISHED && gameState.getWinner() != null){
      AlertDialog.Builder builder;
      builder = new AlertDialog.Builder(this);
      String title = "You lose:(";

      if(gameState.getWinner().getUsername().equals(gameController.getUser().getUsername())){
        title = "You Win!";
      }
      builder.setTitle(title)
          .setPositiveButton("Back", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
              finish();
            }
          })
          .setIcon(android.R.drawable.ic_dialog_alert)
          .show();
    }
  }

  @Override
  public void disconnected(){

  }

  @Override
  public void gameStarted(final GameState gameState){
    gameStage = Constants.GAME_STAGE.RUNNING;
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        updateFromServer(gameState);
      }
    });
  }

  @Override
  public void gameFinished(final GameState gameState){
    gameStage = Constants.GAME_STAGE.FINISHED;
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        updateFromServer(gameState);
      }
    });
  }

  @Override
  public void gameStateUpdated(final GameState gameState){
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        updateFromServer(gameState);
      }
    });
  }

  @Override
  public void itemDropped(final DroppedItem droppedItem){
    runOnUiThread(new Runnable(){
      public void run(){
        gameView.addDroppedItem(droppedItem);
        droppedItemList.add(droppedItem);
      }
    });
  }

  @Override
  public void updateScore(int value){
    //todo
    switch(operationMode){
      case ADDITION:

    }
    playerActionPerformed(GET_NUMBER, value, null);
  }

  View.OnClickListener addHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.ADDITION;
      playerActionPerformed(operationMode, 0, (Button) v);
    }
  };


  View.OnClickListener subHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.SUBTRACTION;
      playerActionPerformed(operationMode, 0, (Button) v);
    }
  };

  View.OnClickListener multHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.MULTIPLICATION;
      playerActionPerformed(operationMode, 0, (Button) v);
    }
  };

  View.OnClickListener divideHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.DIVISION;
      playerActionPerformed(operationMode, 0, (Button) v);
    }
  };

  private void playerActionPerformed(Constants.PLAYER_ACTION_TYPE operationMode, int value, Button selectedButton){
    if(selectedButton != null){
      addButton.setTextColor(Color.DKGRAY);
      subButton.setTextColor(Color.DKGRAY);
      multiplyButton.setTextColor(Color.DKGRAY);
      divideButton.setTextColor(Color.DKGRAY);
      selectedButton.setTextColor(getResources().getColor(R.color.white));
    }
    gameController.sendPlayerAction(new PlayerAction(operationMode, value));
  }
}
