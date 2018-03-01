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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.CustomUI.backgroundGameView;
import sen.sen.numericonsandroid.Models.Basket;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

public class MainGameActivity extends AppCompatActivity implements WebsocketController.WebsocketListener{

  // View widgets
//  ImageView basketImageView;
  backgroundGameView backgroundLayoutView;

  //Private GameState Items
  List<DroppedItem> droppedItemList;
//  Basket basketModel;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_game);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//    basketImageView = findViewById(R.id.basket);
//    basketImageView.setOnTouchListener(basket_onTouchListener());
//    basketModel = new Basket(basketImageView.getX(), basketImageView.getY());
    backgroundLayoutView = findViewById(R.id.background);
    droppedItemList = new ArrayList<>();
    //initDroppedItemList(100);
    WebsocketController.getInstance().addWebsocketListener(this);
  }
//
//  private View.OnTouchListener basket_onTouchListener() {
//    return new View.OnTouchListener(){
//      PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
//      PointF StartPT = new PointF(); // Record Start Position of 'img'
//      @Override
//      public boolean onTouch(View view, MotionEvent motionEvent){
//        switch (motionEvent.getAction())
//        {
//          case MotionEvent.ACTION_MOVE :
//            //@TODO: Change basketImageView.getWidth()/2 into a constant instead.
//            basketModel.setxPosition(StartPT.x + motionEvent.getX() - DownPT.x);
//            basketImageView.setX(basketModel.getxPosition());
//            StartPT.set((basketModel.getxPosition() - basketImageView.getWidth()/2), basketModel.getyPosition());
//            break;
//          default :
//            break;
//        }
//        return true;
//      }
//    };
//  }


  int randomInt_Range(int min, int max) {
    return min + (int)(Math.random() * ((max - min) + 1));
  }
//  void initDroppedItemList(int amount) {
//    float backgroundX = backgroundLayoutView.getX();
//    float backgroundWidth = backgroundLayoutView.getWidth();
//    float backgroundEndX = backgroundX + backgroundWidth;
//
//    for(int i = 0; i < amount; i++) {
//      DroppedItem item = new DroppedItem(randomInt_Range(0,9),
//                                         randomInt_Range((int)backgroundX,(int)backgroundEndX),
//                                         100);
//      droppedItemList.add(item);
//    }
//  }

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
}
