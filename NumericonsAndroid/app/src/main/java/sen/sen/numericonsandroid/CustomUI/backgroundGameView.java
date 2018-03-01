package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Models.Basket;
import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-02-28.
 */

public class backgroundGameView extends RelativeLayout{

  public interface BackgroundGameViewDelegate{
    void updateScore(int value);
  }

  ImageView basketImageView;
  Rect clipBounds;
  Paint textPaint;
  private Basket basketModel;
  private Handler handler;
  private Runnable autoRun;
  private BackgroundGameViewDelegate delegate;

  public void setDelegate(BackgroundGameViewDelegate delegate){
    this.delegate = delegate;
  }

  //@TODO: Remove this later...just for testing
  private float textSize;
  List<DroppedItem> droppedItemList;

  public backgroundGameView(Context context){
    super(context);
    init(context);
  }

  public backgroundGameView(Context context, AttributeSet attrs){
    super(context, attrs);
    init(context);
  }

  private void init(Context context){
    basketImageView = new ImageView(getContext());
    basketImageView.setImageResource(R.drawable.basket2);

    LayoutParams layoutParams = new LayoutParams((int)getResources().getDimension(R.dimen.basket_width),(int)getResources().getDimension(R.dimen.basket_height));
    layoutParams.bottomMargin = (int)getResources().getDimension(R.dimen.basket_margin_bottom);
    layoutParams.addRule(ALIGN_PARENT_BOTTOM);
    addView(basketImageView, layoutParams);

    basketImageView.setOnTouchListener(basket_onTouchListener());
    basketModel = new Basket(0, 0);
    droppedItemList = new ArrayList<>();
    textSize = 60;

    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    textPaint.setColor(Color.rgb(250,150,200));
    textPaint.setTextSize(textSize);

    clipBounds = new Rect();
    handler = new Handler();
    autoRun = new Runnable(){
      public void run(){
        invalidate();
        handler.postDelayed(this, 100);
      }
    };
    handler.removeCallbacks(autoRun);
    handler.post(autoRun);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b){
    super.onLayout(changed, l, t, r, b);
    bringChildToFront(basketImageView);
  }

  public void addDroppedItem(DroppedItem item){
    Log.d("Add item", "addDroppedItem: " + item.getxPosition() + ", " + item.getyPosition());
    item.setAlive(true);
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

    for(DroppedItem item: droppedItemList) {
      if(item.isAlive()) {

        item.fall();
        if(!checkCollision(item)) {
          canvas.drawText(Integer.toString(item.getNumber()), ratioToPixel_Width(item.getxPosition()),
                          ratioToPixel_Height(item.getyPosition()), textPaint);
        } else {
          canvas.drawCircle(ratioToPixel_Width(item.getxPosition()),ratioToPixel_Height(item.getyPosition()), 30, textPaint);
          if(this.delegate != null){
            this.delegate.updateScore(item.getNumber());
          }
        }
      }
    }
  }

  boolean outOfBound(float x, float halfWidth){
    if(x < clipBounds.left || x+2*halfWidth > clipBounds.right) {
      return true;
    } else {
      return false;
    }
  }

  boolean checkCollision(DroppedItem itemNumber) {

    float item_YPixel_position = ratioToPixel_Height(itemNumber.getyPosition());
    float item_XPixel_position = ratioToPixel_Width(itemNumber.getxPosition());

//    Log.i("item_Pixel_position: ", Float.toString(item_XPixel_position) + ", " + Float.toString(item_YPixel_position) + ", " +  Float.toString(clipBounds.height()));
//    Log.i("Basket_position: ", Float.toString(basketModel.getxPosition()) + ", " +
//          Float.toString(basketImageView.getWidth())/ + ", " + Float.toString(basketModel.getyPosition()));

    if(itemNumber == null) {
      return false;
    }

    //Hasn't passed the varietal line, not worry about it yet.
    if(item_YPixel_position < basketModel.getyPosition()){
      return false;
    }

    //Within the range, Collision.
    if(item_XPixel_position >= basketModel.getxPosition() &&
       item_XPixel_position+textSize <= basketModel.getxPosition()+basketImageView.getWidth()) {
      if(itemNumber.isAlive()){
        itemNumber.setAlive(false);
        //@TODO: Do your operation.....AT HERE~~~~
      }
      return true;
    }
    if(item_YPixel_position > clipBounds.bottom) {
      itemNumber.setAlive(false);
      return false;
    }
    else {
      return false;
    }
  }

  View.OnTouchListener basket_onTouchListener(){
    return new View.OnTouchListener(){
      PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
      PointF StartPT = new PointF(); // Record Start Position of 'img'
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent){
        //@TODO: Remove these after...
        float halfWidth = basketImageView.getWidth()/2;
        basketModel.setyPosition(basketImageView.getY());
        switch (motionEvent.getAction())
        {
          case MotionEvent.ACTION_MOVE :
            //@TODO: Change basketImageView.getWidth()/2 into a constant instead.
            float xPosition = StartPT.x + motionEvent.getX() - DownPT.x;
            if(!outOfBound(xPosition, halfWidth)) {
              basketModel.setxPosition(xPosition);
              basketImageView.setX(basketModel.getxPosition());
              StartPT.set((basketModel.getxPosition() - halfWidth), basketModel.getyPosition());
            }
            break;
          default :
            break;
        }
        return true;
      }
    };
  }
}
