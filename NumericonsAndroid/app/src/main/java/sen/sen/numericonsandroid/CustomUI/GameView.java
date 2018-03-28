package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.Helpers;
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

    void getItem(DroppedItem item);
  }

  ImageView birdImageView;
  Rect clipBounds;
  Rect itemClipBounds;
  Rect itemRect;
  private Basket birdModel;
  private Handler handler;
  private Runnable autoRun;
  private GameViewDelegate delegate;
  private Constants.CHARACTER_SPRITE characterSprite;
  private Map<Integer, Drawable> numberDrawableMap;
  private Drawable speedIncreaseDrawable;
  private Paint effectOverlayPaint;

  private int bird_animation_running_rid;
  private int bird_animation_eating_rid;
  private int bird_static_standing_rid;
  private int itemWidth;
  private int itemHeight;
  private boolean effectInUse;


  boolean runAnimating = false;
  Constants.BIRD_DIRECTION birdDirection = Constants.BIRD_DIRECTION.RIGHT;

  public void setDelegate(GameViewDelegate delegate){
    this.delegate = delegate;
  }

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

    LayoutParams layoutParams = new LayoutParams((int) getResources().getDimension(R.dimen.basket_width), (int) getResources().getDimension(R.dimen.basket_height));
    layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.marginTriple);
    layoutParams.addRule(ALIGN_PARENT_BOTTOM);
    addView(birdImageView, layoutParams);

    birdImageView.setOnTouchListener(basket_onTouchListener());
    birdImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
      @Override
      public void onGlobalLayout(){
        birdImageView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        birdModel = new Basket(birdImageView.getLeft(), birdImageView.getTop());
      }
    });
    droppedItemList = new ArrayList<>();

    clipBounds = new Rect();
    itemClipBounds = new Rect();
    itemRect = new Rect();
    itemWidth = (int) getResources().getDimension(R.dimen.droppedItemWidth);
    itemHeight = (int) getResources().getDimension(R.dimen.droppedItemHeight);
    numberDrawableMap = new HashMap<>();
    for(int i = 9; i > 0; i--){
      numberDrawableMap.put(-i, getResources().getDrawable(Helpers.getResId("neg_" + i, R.drawable.class)));
    }
    for(int i = 0; i <= 9; i++){
      numberDrawableMap.put(i, getResources().getDrawable(Helpers.getResId("pos_" + i, R.drawable.class)));
    }
    //todo change
    speedIncreaseDrawable = getResources().getDrawable(R.drawable.star);
    effectOverlayPaint = new Paint();
    effectOverlayPaint.setARGB(80, 200, 40, 40);
    effectOverlayPaint.setStyle(Paint.Style.FILL);

    handler = new Handler();
    autoRun = new Runnable(){
      public void run(){
        //redraw on screen!
        invalidate();
        handler.postDelayed(this, Constants.FRAME_TIME);
      }
    };
    handler.removeCallbacks(autoRun);
    handler.post(autoRun);
  }

  public void setCharacterSprite(Constants.CHARACTER_SPRITE characterSprite){
    this.characterSprite = characterSprite;
    switch(characterSprite){
      case BIRD_1:
        bird_animation_running_rid = R.drawable.b1_animation_run_right;
        bird_animation_eating_rid = R.drawable.b1_animation_eat_right;
        bird_static_standing_rid = R.drawable.b1_stand_r;
        break;
      case BIRD_2:
        bird_animation_running_rid = R.drawable.b2_animation_run_right;
        bird_animation_eating_rid = R.drawable.b2_animation_eat_right;
        bird_static_standing_rid = R.drawable.b2_stand_r;
        break;
      case BIRD_3:
        bird_animation_running_rid = R.drawable.b3_animation_run_right;
        bird_animation_eating_rid = R.drawable.b3_animation_eat_right;
        bird_static_standing_rid = R.drawable.b3_stand_r;
        break;
    }
    birdImageView.setImageResource(bird_static_standing_rid);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b){
    super.onLayout(changed, l, t, r, b);
    bringChildToFront(birdImageView);
  }

  public void addDroppedItem(DroppedItem item){
    droppedItemList.add(item);
  }

  private float ratioToPixel_Width(float ratio){
    return ratio * itemClipBounds.width() + clipBounds.left;
  }

  private float ratioToPixel_Height(float ratio){
    return ratio * itemClipBounds.height() + clipBounds.top;
  }

  @Override
  protected void onDraw(Canvas canvas){
    super.onDraw(canvas);
    canvas.getClipBounds(clipBounds);
    canvas.getClipBounds(itemClipBounds);
    itemClipBounds.right -= itemWidth;

    if(effectInUse){
      canvas.drawRect(canvas.getClipBounds(), effectOverlayPaint);
    }

    Iterator<DroppedItem> iterator = droppedItemList.iterator();
    while(iterator.hasNext()){
      DroppedItem item = iterator.next();

      item.fall();
      int left = (int) ratioToPixel_Width(item.getxPosition());
      int top = (int) ratioToPixel_Height(item.getyPosition());
      itemRect.set(left, top, left + itemWidth, top + itemHeight);

      Drawable itemDrawable;
      switch(item.getItemType()){
        case NUMBER:
          itemDrawable = numberDrawableMap.get(item.getNumber());
          break;
        case SPEED_INCREASE:
        default:
          itemDrawable = speedIncreaseDrawable;
          break;
      }
      itemDrawable.setBounds(itemRect);
      itemDrawable.draw(canvas);

      if(((itemRect.top - birdModel.getyPosition() <= 10) && checkCollision(item, itemRect)) || (item.getyPosition() >= 1)){
        iterator.remove();
      }
    }
  }

  boolean outOfBound(float x, float halfWidth){
    if(x < clipBounds.left || x + 2 * halfWidth > clipBounds.right){
      return true;
    }
    else{
      return false;
    }
  }

  public void setEffectInUse(boolean effectInUse){
    this.effectInUse = effectInUse;
  }

  private boolean checkCollision(DroppedItem item, Rect itemBounds){
    if(itemBounds.intersect((int) birdModel.getxPosition(),
                            (int) birdModel.getyPosition(),
                            (int) birdModel.getxPosition() + birdImageView.getWidth(),
                            (int) birdModel.getyPosition() + birdImageView.getHeight())){
      bird_eat_animation_start();
      if(this.delegate != null){
        switch(item.getItemType()){
          case NUMBER:
            this.delegate.updateScore(item.getNumber());
            break;
          case SPEED_INCREASE:
          default:
            this.delegate.getItem(item);
            break;
        }
      }
      return true;
    }
    return false;
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
    }
    else{
      birdDirection = Constants.BIRD_DIRECTION.RIGHT;
    }
  }

  void bird_eat_animation_start(){
    birdImageView.setImageResource(bird_animation_eating_rid);
    AnimationDrawable eatAnimation = (AnimationDrawable) birdImageView.getDrawable();
    if(!eatAnimation.isRunning()){
      ((AnimationDrawable) birdImageView.getDrawable()).start();
    }
  }

  void bird_running_animation_start(){
    if(birdDirection == Constants.BIRD_DIRECTION.LEFT){
      // Log.i("LEFT", "onTouch: LEFT!!");
    }
    else{
      // Log.i("RIGHT", "onTouch: RIGHT!!");
    }
    if(!runAnimating){
      birdImageView.setImageResource(bird_animation_running_rid);
      ((AnimationDrawable) birdImageView.getDrawable()).start();
      runAnimating = true;
    }
  }

  void bird_running_animation_stop(){
    if(runAnimating){
      birdImageView.setImageResource(bird_animation_running_rid);
      ((AnimationDrawable) birdImageView.getDrawable()).stop();
      runAnimating = false;
    }
  }

  void bird_stand(){
    birdImageView.setImageResource(bird_static_standing_rid);
  }
}
