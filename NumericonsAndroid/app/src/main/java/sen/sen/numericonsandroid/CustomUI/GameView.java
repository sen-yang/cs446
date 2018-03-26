package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.Basket;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-02-28.
 */

public class GameView extends RelativeLayout{

  public interface GameViewDelegate{
    void updateScore(int value);
  }

  ImageView birdImageView;
  Rect clipBounds;
  Paint textPaint;
  private Player currentPlayer;
  private Basket birdModel;
  private Handler handler;
  private Runnable autoRun;
  private GameViewDelegate delegate;
  private GameState gameState;

  boolean runAnimating = false;
  Constants.BIRD_DIRECTION birdDirection = Constants.BIRD_DIRECTION.RIGHT;

  public void setDelegate(GameViewDelegate delegate){
    this.delegate = delegate;
  }

  //@TODO: Remove this later...just for testing
  private float textSize;
  List<DroppedItem> droppedItemList;

  public GameView(Context context){
    super(context);
    init(context);
  }

  public GameView(Context context, AttributeSet attrs){
    super(context, attrs);
    init(context);
  }

  private void init(Context context){
    birdImageView = new ImageView(getContext());
    birdImageView.setImageResource(R.drawable.b1_run_r);

    LayoutParams layoutParams = new LayoutParams((int) getResources().getDimension(R.dimen.basket_width), (int) getResources().getDimension(R.dimen.basket_height));
    layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.marginTriple);
    layoutParams.addRule(ALIGN_PARENT_BOTTOM);
    addView(birdImageView, layoutParams);

    birdImageView.setOnTouchListener(basket_onTouchListener());
    birdModel = new Basket(0, 0);
    droppedItemList = new ArrayList<>();
    textSize = getResources().getDimension(R.dimen.dropTextSize);

    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.rgb(180, 40, 130));
    textPaint.setTextSize(textSize);

    clipBounds = new Rect();
    handler = new Handler();
    autoRun = new Runnable(){
      public void run(){
        //redraw on screen!
        invalidate();
        handler.postDelayed(this, 50);
      }
    };
    handler.removeCallbacks(autoRun);
    handler.post(autoRun);
  }

  public void setCurrentPlayer(Player currentPlayer) {
    Log.d("setCurrentPlayer", "currentPlayer" + currentPlayer.getUsername());
   this.currentPlayer =  currentPlayer;
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b){
    super.onLayout(changed, l, t, r, b);
    bringChildToFront(birdImageView);
  }

  public void addDroppedItem(DroppedItem item){
    Log.d("Add item", "addDroppedItem: " + item.getxPosition() + ", " + item.getyPosition());
    item.setAlive(true);
    item.setNumberDrawable(getNumberDrawable(item.getNumber()));
    item.setNumberBound((int) ratioToPixel_Width(item.getxPosition()),(int) ratioToPixel_Height(item.getyPosition()));
    droppedItemList.add(item);
  }

  private float ratioToPixel_Width(float ratio){
    return ratio * clipBounds.width() + clipBounds.left;
  }

  private float ratioToPixel_Height(float ratio){

    return ratio * clipBounds.height() + clipBounds.top;
  }

  @Override
  protected void onDraw(Canvas canvas){
    super.onDraw(canvas);
    canvas.getClipBounds(clipBounds);

    for(DroppedItem item : droppedItemList){
      if(item.isAlive()){
        item.fall();
        int itemBoundLeft = (int) ratioToPixel_Width(item.getxPosition());
        int itemBoundTop = (int) ratioToPixel_Height(item.getyPosition());
        item.setNumberBound(itemBoundLeft,itemBoundTop);

        if(itemBoundTop - birdModel.getyPosition() <= 10) {
          checkCollision(item, canvas);
        }
        Drawable itemDrawable = item.getNumberDrawable();
        itemDrawable.draw(canvas);
      }
    }
  }

  boolean outOfBound(float x, float halfWidth){
    if(x < clipBounds.left || x + 2 * halfWidth > clipBounds.right){
      return true;
    } else{
      return false;
    }
  }

  void checkCollision(DroppedItem item, Canvas canvas) {
    if(item.getNumberBound().intersect((int) birdModel.getxPosition(),
                                       (int) birdModel.getyPosition(),
                                       (int) birdModel.getxPosition() + birdImageView.getWidth(),
                                       (int) birdModel.getyPosition() + birdImageView.getHeight())){
      item.setAlive(false);
      bird_eat_animation_start();
      if(this.delegate != null){
        this.delegate.updateScore(item.getNumber());
      }
    }
  }

  View.OnTouchListener basket_onTouchListener(){
    return new View.OnTouchListener(){
      PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
      PointF StartPT = new PointF(); // Record Start Position of 'img'
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent){
        //@TODO: Remove these after...
        float halfWidth = birdImageView.getWidth() / 2;
        birdModel.setyPosition(birdImageView.getY());

        switch(motionEvent.getAction()){
          case MotionEvent.ACTION_MOVE:
            //@TODO: Change birdImageView.getWidth()/2 into a constant instead.
            float xPosition = StartPT.x + motionEvent.getX() - DownPT.x;
            if(!outOfBound(xPosition, halfWidth)){
              updateDirection(xPosition);
              bird_running_animation_start();
              birdModel.setxPosition(xPosition);
              birdImageView.setX(birdModel.getxPosition());
              StartPT.set((birdModel.getxPosition() - halfWidth), birdModel.getyPosition());
            }
            break;
          case MotionEvent.ACTION_UP:
            bird_running_animation_stop();
            bird_stand();
          default:
            break;
        }
        return true;
      }
    };
  }

  void updateDirection(float xPosition){
    if(birdModel.getxPosition() > xPosition){
      birdDirection = Constants.BIRD_DIRECTION.LEFT;
    } else{
      birdDirection = Constants.BIRD_DIRECTION.RIGHT;
    }
  }

  void bird_eat_animation_start(){
    birdImageView.setImageResource(R.drawable.b1_animation_eat_right);
    AnimationDrawable eatAnimation = (AnimationDrawable) birdImageView.getDrawable();
    if(!eatAnimation.isRunning()){
      ((AnimationDrawable) birdImageView.getDrawable()).start();
    }
  }

  void bird_running_animation_start(){
    if(birdDirection == Constants.BIRD_DIRECTION.LEFT){
     // Log.i("LEFT", "onTouch: LEFT!!");
    } else{
     // Log.i("RIGHT", "onTouch: RIGHT!!");
    }
    if(!runAnimating){
      birdImageView.setImageResource(R.drawable.b1_animation_run_right);
      ((AnimationDrawable) birdImageView.getDrawable()).start();
      runAnimating = true;
    }
  }

  void bird_running_animation_stop(){
    if(runAnimating){
      birdImageView.setImageResource(R.drawable.b1_animation_run_right);
      ((AnimationDrawable) birdImageView.getDrawable()).stop();
      runAnimating = false;
    }
  }

  void bird_stand(){
    birdImageView.setImageResource(R.drawable.b1_stand_r);
  }

  public Drawable getNumberDrawable(int number){
    Drawable numberDrawable = getResources().getDrawable(R.drawable.pos_0);
    switch(number){
      case -9:
        numberDrawable = getResources().getDrawable(R.drawable.neg_9);
        break;
      case -8:
        numberDrawable = getResources().getDrawable(R.drawable.neg_8);
        break;
      case -7:
        numberDrawable = getResources().getDrawable(R.drawable.neg_7);
        break;
      case -6:
        numberDrawable = getResources().getDrawable(R.drawable.neg_6);
        break;
      case -5:
        numberDrawable = getResources().getDrawable(R.drawable.neg_5);
        break;
      case -4:
        numberDrawable = getResources().getDrawable(R.drawable.neg_4);
        break;
      case -3:
        numberDrawable = getResources().getDrawable(R.drawable.neg_3);
        break;
      case -2:
        numberDrawable = getResources().getDrawable(R.drawable.neg_2);
        break;
      case -1:
        numberDrawable = getResources().getDrawable(R.drawable.neg_1);
        break;
      case 0:
        numberDrawable = getResources().getDrawable(R.drawable.pos_0);
        break;
      case 1:
        numberDrawable = getResources().getDrawable(R.drawable.pos_1);
        break;
      case 2:
        numberDrawable = getResources().getDrawable(R.drawable.pos_2);
        break;
      case 3:
        numberDrawable = getResources().getDrawable(R.drawable.pos_3);
        break;
      case 4:
        numberDrawable = getResources().getDrawable(R.drawable.pos_4);
        break;
      case 5:
        numberDrawable = getResources().getDrawable(R.drawable.pos_5);
        break;
      case 6:
        numberDrawable = getResources().getDrawable(R.drawable.pos_6);
        break;
      case 7:
        numberDrawable = getResources().getDrawable(R.drawable.pos_7);
        break;
      case 8:
        numberDrawable = getResources().getDrawable(R.drawable.pos_8);
        break;
      case 9:
        numberDrawable = getResources().getDrawable(R.drawable.pos_9);
        break;
      default:
        break;
    }
    return numberDrawable;
  }
}
