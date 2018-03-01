package sen.sen.numericonsandroid.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.graphics.PointF;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sen.sen.numericonsandroid.CustomUI.backgroundGameView;
import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.Basket;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.Models.PlayerAction;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

import static sen.sen.numericonsandroid.Global.Constants.PLAYER_ACTION_TYPE.GET_NUMBER;
import static sen.sen.numericonsandroid.Global.Constants.TOTAL_GAME_TIME;

public class MainGameActivity extends AppCompatActivity implements WebsocketController.WebsocketListener, backgroundGameView.BackgroundGameViewDelegate{

  // View widgets
  backgroundGameView backgroundLayoutView;
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

  private GameState gameState;
  private Player currentPlayer;
  private Constants.GAME_STAGE gameStage;

  Constants.PLAYER_ACTION_TYPE operationMode = Constants.PLAYER_ACTION_TYPE.ADDITION;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_game);

    gameState = (GameState) getIntent().getSerializableExtra(Constants.GAME_STATE);
    gameStage = Constants.GAME_STAGE.INIT;

    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    backgroundLayoutView = findViewById(R.id.background);
    backgroundLayoutView.setDelegate(this);

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
    WebsocketController.getInstance().addWebsocketListener(this);
    updateFromServer(gameState);
  }

  int randomInt_Range(int min, int max){
    return min + (int) (Math.random() * ((max - min) + 1));
  }

  float randomFloat_Range(float min, float max){
    Random r = new Random();
    return min + r.nextFloat() * (max - min);
  }

  private void updateFromServer(GameState gameState){
    if(this.isFinishing()){
      return;
    }
    targetNumberTextView.setText(Integer.toString(gameState.getTargetNumber()));

    for(Player player : gameState.getPlayerList()){
      if(player.getUsername().equals(WebsocketController.getInstance().getUser().getUsername())){
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

      if(gameState.getWinner().getUsername().equals(WebsocketController.getInstance().getUser().getUsername())){
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
  public void onConnected(){

  }

  @Override
  public void onClose(){

  }

  @Override
  public void loginConfirmed(boolean isConfirmed, User user){

  }

  @Override
  public void gameInitialized(GameState gameState){

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
        backgroundLayoutView.addDroppedItem(droppedItem);
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
      addButton.setBackgroundColor(Color.RED);
      subButton.setBackgroundColor(Color.DKGRAY);
      multiplyButton.setBackgroundColor(Color.DKGRAY);
      divideButton.setBackgroundColor(Color.DKGRAY);
      operationMode = Constants.PLAYER_ACTION_TYPE.ADDITION;
      playerActionPerformed(operationMode, 0, (Button) v);
    }
  };


  View.OnClickListener subHandler = new View.OnClickListener(){
    public void onClick(View v){
      subButton.setBackgroundColor(Color.RED);
      addButton.setBackgroundColor(Color.DKGRAY);
      multiplyButton.setBackgroundColor(Color.DKGRAY);
      divideButton.setBackgroundColor(Color.DKGRAY);

      operationMode = Constants.PLAYER_ACTION_TYPE.SUBTRACTION;
      playerActionPerformed(operationMode, 0, (Button) v);
    }
  };

  View.OnClickListener multHandler = new View.OnClickListener(){
    public void onClick(View v){
      multiplyButton.setBackgroundColor(Color.RED);
      subButton.setBackgroundColor(Color.DKGRAY);
      addButton.setBackgroundColor(Color.DKGRAY);
      divideButton.setBackgroundColor(Color.DKGRAY);

      operationMode = Constants.PLAYER_ACTION_TYPE.ADDITION;
      operationMode = Constants.PLAYER_ACTION_TYPE.MULTIPLICATION;
      playerActionPerformed(operationMode, 0, (Button) v);
    }
  };

  View.OnClickListener divideHandler = new View.OnClickListener(){
    public void onClick(View v){
      divideButton.setBackgroundColor(Color.RED);
      subButton.setBackgroundColor(Color.DKGRAY);
      addButton.setBackgroundColor(Color.DKGRAY);
      multiplyButton.setBackgroundColor(Color.DKGRAY);

      operationMode = Constants.PLAYER_ACTION_TYPE.DIVISION;
      playerActionPerformed(operationMode, 0, (Button) v);
    }
  };

  private void playerActionPerformed(Constants.PLAYER_ACTION_TYPE operationMode, int value, Button selectedButton){
    addButton.setTextColor(getResources().getColor(R.color.black));
    subButton.setTextColor(getResources().getColor(R.color.black));
    multiplyButton.setTextColor(getResources().getColor(R.color.black));
    divideButton.setTextColor(getResources().getColor(R.color.black));
    if(selectedButton != null){
      selectedButton.setTextColor(getResources().getColor(R.color.white));
    }
    WebsocketController.getInstance().sendPlayerAction(new PlayerAction(operationMode, value));
  }
}
