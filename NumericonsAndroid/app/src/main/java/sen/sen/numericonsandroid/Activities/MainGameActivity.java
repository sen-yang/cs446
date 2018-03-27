package sen.sen.numericonsandroid.Activities;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sen.sen.numericonsandroid.CustomUI.GameView;
import sen.sen.numericonsandroid.CustomUI.PlayerListInfoLayout;
import sen.sen.numericonsandroid.CustomUI.PowerUpListView;
import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.SharedPreferencesHelper;
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
  //View widgets
  GameView gameView;
  TextView targetNumberTextView;
  TextView totalNumberTextView;
  ProgressBar countDownTimer;

  //Buttons
  ImageView addButton;
  ImageView subButton;
  ImageView multiplyButton;
  ImageView divideButton;

  //Private GameState Items
  List<DroppedItem> droppedItemList;
  private GameController gameController;
  private Player currentPlayer;
  private Constants.GAME_STAGE gameStage;
  private boolean finished;

  Constants.PLAYER_ACTION_TYPE operationMode = Constants.PLAYER_ACTION_TYPE.ADDITION;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_game);

    //Setup game properties
    gameController = (GameController) getIntent().getSerializableExtra(Constants.GAME_CONTROLLER);
    gameStage = Constants.GAME_STAGE.INIT;

    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    gameView = findViewById(R.id.gameView);
    gameView.setDelegate(this);


    //IF game type is multiPlayer, append multiPlayer layoutView
    //@TODO: PUT IF AND setPlayerList() BACK LATER! JUST FOR TESTING
    // if(gameController.getGameState().getMatchType() == Constants.GAME_TYPE.RANKED) {
    setMultiplayerListView();
    // }

    countDownTimer = findViewById(R.id.countDownTimer);
    //Setup Buttons References
    //Assume add is the default
    addButton = findViewById(R.id.addButton);
    addButton.setBackgroundColor(getResources().getColor(R.color.brightGreen));
    subButton = findViewById(R.id.minusButton);
    multiplyButton = findViewById(R.id.multiplyButton);
    divideButton = findViewById(R.id.divideButton);

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
    //gameView.setCharacterSprite(SharedPreferencesHelper.getSavedUser().getCharacterSprite());
    gameView.setCharacterSprite(Constants.CHARACTER_SPRITE.BIRD_3);
  }

  @Override
  protected void onStop(){
    super.onStop();
    gameController.removeGameListener(this);
  }

  private void setMultiplayerListView(){
    RelativeLayout wrapperLayout = new RelativeLayout(gameView.getContext());
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, R.id.countDownTimer);
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
    wrapperLayout.setLayoutParams(layoutParams);

    PlayerListInfoLayout multiPlayerModePlayerInfoView = new PlayerListInfoLayout(wrapperLayout.getContext());
    LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
    multiPlayerModePlayerInfoView.setGravity(Gravity.END);
    multiPlayerModePlayerInfoView.setLayoutParams(linearLayoutParams);

    //@TODO: Remove this later, just for testing
    if(gameController.getGameState().getPlayerList().size() == 2) {
      gameController.getGameState().getPlayerList().get(0).setCharacterSprite(Constants.CHARACTER_SPRITE.BIRD_1);
      gameController.getGameState().getPlayerList().get(1).setCharacterSprite(Constants.CHARACTER_SPRITE.BIRD_1);
    }
    multiPlayerModePlayerInfoView.setPlayerList(gameController.getGameState().getPlayerList());
    wrapperLayout.addView(multiPlayerModePlayerInfoView);
    gameView.addView(wrapperLayout);
  }

  private void updateFromServer(GameState gameState){
    if(this.isFinishing()){
      return;
    }

    if(gameState.getDroppedItemList() != null){
      for(DroppedItem droppedItem : gameState.getDroppedItemList()){
        gameView.addDroppedItem(droppedItem);
        droppedItemList.add(droppedItem);
      }
    }
    targetNumberTextView.setText(Integer.toString(gameState.getTargetNumber()));

    for(Player player : gameState.getPlayerList()){
      if(player.getUsername().equals(SharedPreferencesHelper.getUsername())){
        currentPlayer = player;
        break;
      }
    }
    totalNumberTextView.setText(Integer.toString(currentPlayer.getCurrentNumber()));
    //TOTAL_GAME_TIME
    countDownTimer.setProgress((int)((((float) gameState.getTimeRemaining()) / TOTAL_GAME_TIME) * 100));

    //todo show other players
    if(gameStage == Constants.GAME_STAGE.FINISHED){
      AlertDialog.Builder builder;
      builder = new AlertDialog.Builder(this);
      String title = getString(R.string.you_lose);

      if((gameState.getWinner() != null) && gameState.getWinner().getUsername().equals(SharedPreferencesHelper.getUsername())){
        title = getString(R.string.you_win);
      }
      builder.setTitle(title)
             .setPositiveButton(R.string.back, new DialogInterface.OnClickListener(){
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
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        if(gameStage != Constants.GAME_STAGE.FINISHED){
          new AlertDialog.Builder(MainGameActivity.this)
              .setTitle(R.string.you_have_been_disconnected)
              .setPositiveButton(R.string.back, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i){
                  finish();
                }
              })
              .show();
        }
      }
    });
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
      playerActionPerformed(operationMode, 0, (ImageView) v);
    }
  };


  View.OnClickListener subHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.SUBTRACTION;
      playerActionPerformed(operationMode, 0, (ImageView) v);
    }
  };

  View.OnClickListener multHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.MULTIPLICATION;
      playerActionPerformed(operationMode, 0, (ImageView) v);
    }
  };

  View.OnClickListener divideHandler = new View.OnClickListener(){
    public void onClick(View v){
      operationMode = Constants.PLAYER_ACTION_TYPE.DIVISION;
      playerActionPerformed(operationMode, 0, (ImageView) v);
    }
  };

  private void playerActionPerformed(Constants.PLAYER_ACTION_TYPE operationMode, int value, ImageView selectedButton){
    if(selectedButton != null){
      int darkBrown = getResources().getColor(R.color.darkBrown);
      addButton.setBackgroundColor(darkBrown);
      subButton.setBackgroundColor(darkBrown);
      multiplyButton.setBackgroundColor(darkBrown);
      divideButton.setBackgroundColor(darkBrown);
      selectedButton.setBackgroundColor(getResources().getColor(R.color.brightGreen));
    }
    gameController.sendPlayerAction(new PlayerAction(operationMode, value));
  }
}
