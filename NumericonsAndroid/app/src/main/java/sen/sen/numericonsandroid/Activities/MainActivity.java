package sen.sen.numericonsandroid.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

public class MainActivity extends AppCompatActivity{
  private ProgressBar progressBar;
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    progressBar = findViewById(R.id.progressBar);

    WebsocketController.getInstance().addWebsocketListener(new WebsocketController.WebsocketListener(){
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
      public void onClose(){

      }

      @Override
      public void loginConfirmed(boolean isConfirmed, User user){
        if(isConfirmed){
          MainActivity.this.user = user;
        }
      }

      @Override
      public void gameInitialized(final GameState gameState){
        runOnUiThread(new Runnable(){
          @Override
          public void run(){
            Log.d("asdf", "gameInitialized");
            Intent intent = new Intent(MainActivity.this, MainGameActivity.class);
            intent.putExtra(Constants.GAME_STATE, gameState);
            intent.putExtra(Constants.USER, user);
            startActivity(intent);
          }
        });
      }

      @Override
      public void gameStarted(GameState gameState){

      }

      @Override
      public void gameFinished(GameState gameState){

      }

      @Override
      public void gameStateUpdated(GameState gameState){

      }

      @Override
      public void itemDropped(DroppedItem droppedItem){

      }
    });
    if(WebsocketController.getInstance().isConnected()){
      progressBar.setVisibility(View.GONE);
    }
  }

  public void onFindGameButtonPressed(View view){
    WebsocketController.getInstance().lookForMatch(Constants.GAME_TYPE.RANKED);
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
    alert.show();
  }
}
