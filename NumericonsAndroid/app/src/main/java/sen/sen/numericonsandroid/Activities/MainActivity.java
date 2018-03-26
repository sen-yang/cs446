package sen.sen.numericonsandroid.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    progressBar = findViewById(R.id.progressBar);
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
