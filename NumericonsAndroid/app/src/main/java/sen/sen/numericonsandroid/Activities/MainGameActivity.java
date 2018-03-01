package sen.sen.numericonsandroid.Activities;

import android.content.pm.ActivityInfo;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sen.sen.numericonsandroid.CustomUI.backgroundGameView;
import sen.sen.numericonsandroid.Models.Basket;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

public class MainGameActivity extends AppCompatActivity implements WebsocketController.WebsocketListener{

  // View widgets
  backgroundGameView backgroundLayoutView;
  TextView targetNumberTextView;
  TextView totalNumberTextView;

  //Buttons
  Button addButton;
  Button subButton;
  Button multiplyButton;
  Button divideButton;

  //Private GameState Items
  List<DroppedItem> droppedItemList;

  //@TODO: this is shit, but just for demo use for now..
  int operationMode = 1; //+ : 1, - : 2, * : 3, and / : 4;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_game);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    backgroundLayoutView = findViewById(R.id.background);
//    backgroundLayoutView.setDelegate(this);

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

    //targetNumberTextView.findViewById(R.id.targetNumberTextView);
    //@TODO: change "currentNumberTextView" to totalNumberTextView.
//    totalNumberTextView.findViewById(R.id.currentNumberTextView);

    droppedItemList = new ArrayList<>();
    initDroppedItemList(20);
    WebsocketController.getInstance().addWebsocketListener(this);
  }

  int randomInt_Range(int min, int max) {
    return min + (int)(Math.random() * ((max - min) + 1));
  }

  float randomFloat_Range(float min, float max) {
    Random r = new Random();
    return min + r.nextFloat() * (max - min);
  }
  void initDroppedItemList(int amount) {
    float backgroundX = backgroundLayoutView.getX();
    float backgroundWidth = backgroundLayoutView.getWidth();
    float backgroundEndX = backgroundX + backgroundWidth;

    for(int i = 0; i < amount; i++) {
      DroppedItem item = new DroppedItem(randomInt_Range(0,9),
                                         randomFloat_Range(0,0.9f),
                                         randomFloat_Range(0.005f,0.01f));
      droppedItemList.add(item);
      backgroundLayoutView.addDroppedItem(item);
    }
  }

  @Override
  public void onConnected(){

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
  public void itemDropped(final DroppedItem droppedItem){
    runOnUiThread(new Runnable() {
      public void run() {
       //....droppedItem
        backgroundLayoutView.addDroppedItem(droppedItem);
        droppedItemList.add(droppedItem);
      }
    });
  }

//  @Override
//  public void updateScore(){
//    //todo
//    Log.i("updateScore", "updateScore: !!");
//
//  }

  View.OnClickListener addHandler = new View.OnClickListener() {
    public void onClick(View v) {
      operationMode = 1;
    }
  };


  View.OnClickListener subHandler = new View.OnClickListener() {
    public void onClick(View v) {
      operationMode = 2;
    }
  };

  View.OnClickListener multHandler = new View.OnClickListener() {
    public void onClick(View v) {
      operationMode = 3;
    }
  };

  View.OnClickListener divideHandler = new View.OnClickListener() {
    public void onClick(View v) {
      operationMode = 4;
    }
  };
}
