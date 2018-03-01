package sen.sen.numericonsandroid.Activities;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
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
  private User user;
  private Constants.GAME_STAGE gameStage;

  Constants.PLAYER_ACTION_TYPE operationMode = Constants.PLAYER_ACTION_TYPE.ADDITION;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_game);

    gameState = (GameState) getIntent().getSerializableExtra(Constants.GAME_STATE);
    user = (User) getIntent().getSerializableExtra(Constants.USER);
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
    targetNumberTextView.setText(Integer.toString(gameState.getTargetNumber()));

    for(Player player : gameState.getPlayerList()){
      if(player.getUsername().equals(user.getUsername())){
        currentPlayer = player;
        break;
      }
    }
    totalNumberTextView.setText(Integer.toString(currentPlayer.getCurrentNumber()));
    countDownTimer.setProgress((int) (((float) gameState.getTimeRemaining()) / TOTAL_GAME_TIME) * 100);
    //todo show other players

    if(gameStage == Constants.GAME_STAGE.FINISHED){
      AlertDialog.Builder builder;
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
      }
      else{
        builder = new AlertDialog.Builder(this);
      }
      String title = "You lose:(";

      if(gameState.getWinner().getUsername().equals(user.getUsername())){
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
        Log.d("asdf", droppedItem.getNumber() + " " + droppedItem.getxPosition() + " " + droppedItem.getyPosition() + " " + droppedItem.getySpeed());
        backgroundLayoutView.addDroppedItem(droppedItem);
        droppedItemList.add(droppedItem);
        Log.d("asdf1","aaaaa");

      }
    });
  }

  @Override
  public void updateScore(int value){
    //todo
    switch(operationMode){
      case ADDITION:

    }
    playerActionPerformed(GET_NUMBER, value);
  }

  View.OnClickListener addHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.ADDITION;
      playerActionPerformed(operationMode, 0);
    }
  };


  View.OnClickListener subHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.SUBTRACTION;
      playerActionPerformed(operationMode, 0);
    }
  };

  View.OnClickListener multHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.MULTIPLICATION;
      playerActionPerformed(operationMode, 0);
    }
  };

  View.OnClickListener divideHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.DIVISION;
      playerActionPerformed(operationMode, 0);
    }
  };

  private void playerActionPerformed(Constants.PLAYER_ACTION_TYPE operationMode, int value){
    WebsocketController.getInstance().sendPlayerAction(new PlayerAction(operationMode, value));
  }
}
