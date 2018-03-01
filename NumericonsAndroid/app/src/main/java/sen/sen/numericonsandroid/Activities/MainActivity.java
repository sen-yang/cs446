package sen.sen.numericonsandroid.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

public class MainActivity extends AppCompatActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    WebsocketController.getInstance().addWebsocketListener(new WebsocketController.WebsocketListener(){
      @Override
      public void onConnected(){
        WebsocketController.getInstance().login("sen");
      }

      @Override
      public void onClose(){

      }

      @Override
      public void loginConfirmed(boolean isConfirmed){

      }

      @Override
      public void gameInitialized(GameState gameState){

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
  }

  public void onButtonPressed(View view){
    Intent intent = new Intent(this, MainGameActivity.class);
    startActivity(intent);
  }
}
