package sen.sen.numericonsandroid.Activities;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.GameController;
import sen.sen.numericonsandroid.Networking.LocalGameManager;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

public class MainActivity extends AppCompatActivity implements WebsocketController.WebsocketListener{
  private ProgressBar progressBar;
  private AlertDialog alertDialog;

  private ConstraintLayout mainActivityLayout;
  private LinearLayout linearLayoutMain;
  private LinearLayout linearLayoutLocal;
  private LinearLayout linearLayoutOnline;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    progressBar = findViewById(R.id.progressBar);
    mainActivityLayout = findViewById(R.id.mainActivityLayout);
    linearLayoutMain = findViewById(R.id.linearLayoutMain);
    linearLayoutLocal = findViewById(R.id.linearLayoutLocal);
    linearLayoutOnline = findViewById(R.id.linearLayoutOnline);

    Animator scaleDown = ObjectAnimator.ofPropertyValuesHolder((Object)null, PropertyValuesHolder.ofFloat("translateX", 1, 0), PropertyValuesHolder.ofFloat("scaleY", 1, 0));
    scaleDown.setDuration(300);
    scaleDown.setInterpolator(new OvershootInterpolator());

    Animator scaleUp = ObjectAnimator.ofPropertyValuesHolder((Object)null, PropertyValuesHolder.ofFloat("scaleX", 0, 1), PropertyValuesHolder.ofFloat("scaleY", 0, 1));
    scaleUp.setDuration(300);
    scaleUp.setStartDelay(300);
    scaleUp.setInterpolator(new OvershootInterpolator());

    LayoutTransition itemLayoutTransition = new LayoutTransition();
    itemLayoutTransition.setAnimator(LayoutTransition.APPEARING, scaleUp);
    itemLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, scaleDown);

    mainActivityLayout.setLayoutTransition(itemLayoutTransition);
  }

  @Override
  protected void onResume(){
    super.onResume();

    WebsocketController.getInstance().addWebsocketListener(this);
    if(WebsocketController.getInstance().isConnected()){
      progressBar.setVisibility(View.GONE);
    }
  }

  @Override
  protected void onPause(){
    super.onPause();
    WebsocketController.getInstance().removeWebsocketListener(this);
  }

  public void onRankedGameButtonPressed(View view){
    WebsocketController.getInstance().lookForMatch(Constants.GAME_TYPE.RANKED);
    showSearching();
  }

  public void onGroupGameButtonPressed(View view){
    WebsocketController.getInstance().lookForMatch(Constants.GAME_TYPE.GROUP_GAME);
    showSearching();
  }


  public void onSinglePlayerButtonPressed(View view){
    GameController gameController = new GameController(LocalGameManager.class);
    Intent intent = new Intent(MainActivity.this, MainGameActivity.class);
    intent.putExtra(Constants.GAME_CONTROLLER, gameController);
    startActivity(intent);

    if((alertDialog != null) && (alertDialog.isShowing())){
      alertDialog.dismiss();
    }
  }

  private void showSearching(){
    alertDialog = new AlertDialog.Builder(this)
            .setTitle("Searching...")
            .setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
              @Override
              public void onClick(DialogInterface dialogInterface, int i){
                cancelSearch();
                dialogInterface.dismiss();
              }
            })
            .show();
  }

  private void cancelSearch(){
    WebsocketController.getInstance().lookForMatch(Constants.GAME_TYPE.CANCEL);
  }

  public void onBluetoothButtonPressed(View view){
    Intent intent = new Intent(this, BluetoothConnectionActivity.class);
    startActivity(intent);
  }
  public void onLeaderBoardButtonPressed(View view){
    Intent intent = new Intent(this, LeaderBoardActivity.class);
    startActivity(intent);
  }

  public void changeNameButtonPressed(View view){
    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    alert.setTitle("Set Name");
    final EditText input = new EditText(this);
    alert.setView(input);

    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int whichButton){
        WebsocketController.getInstance().login(input.getText().toString());
      }
    });
    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog, int whichButton){
      }
    });
    alertDialog = alert.show();
  }

  public void onMainActivityButtonPressed(View view){
    linearLayoutLocal.setVisibility(View.INVISIBLE);
    linearLayoutOnline.setVisibility(View.INVISIBLE);
    linearLayoutMain.setVisibility(View.VISIBLE);
  }

  public void onOnlineGameButtonPressed(View view){
    linearLayoutMain.setVisibility(View.INVISIBLE);
    linearLayoutLocal.setVisibility(View.INVISIBLE);
    linearLayoutOnline.setVisibility(View.VISIBLE);
  }

  public void onLocalGameButtonPressed(View view){
    linearLayoutMain.setVisibility(View.INVISIBLE);
    linearLayoutLocal.setVisibility(View.VISIBLE);
    linearLayoutOnline.setVisibility(View.INVISIBLE);
  }

  @Override
  public void onConnected(){
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        progressBar.setVisibility(View.GONE);
      }
    });
  }

  @Override
  public void onClose(int code, String reason, boolean remote){

  }

  @Override
  public void userConfirmed(boolean isConfirmed, User user){
    if(isConfirmed){
      //todo login confirmed
    }
  }

  @Override
  public void gameInitialized(final GameState gameState){
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        GameController gameController = new GameController(WebsocketController.class);
        Intent intent = new Intent(MainActivity.this, MainGameActivity.class);
        intent.putExtra(Constants.GAME_CONTROLLER, gameController);
        startActivity(intent);

        if((alertDialog != null) && (alertDialog.isShowing())){
          alertDialog.dismiss();
        }
      }
    });
  }
}
