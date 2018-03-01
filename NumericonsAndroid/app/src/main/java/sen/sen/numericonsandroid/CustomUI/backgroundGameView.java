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
  ImageView basketImageView;
  Rect clipBounds;
  private Basket basketModel;
  private Handler handler;
  private Runnable autoRun;
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
    clipBounds = new Rect();
    handler = new Handler();

    autoRun = new Runnable(){
      public void run(){
        invalidate();
        handler.postDelayed(this, 1000);
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
    Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mTextPaint.setColor(Color.RED);
    mTextPaint.setTextSize(20);
    canvas.drawText("hi", ratioToPixel_Width(0.5f), ratioToPixel_Height(0.2f), mTextPaint);
    canvas.drawCircle(ratioToPixel_Width(0.7f), ratioToPixel_Height(0.7f), 30, mTextPaint);
  }

  boolean outOfBound(float x, float halfWidth){
    if(x < clipBounds.left || x+2*halfWidth > clipBounds.right) {
      return true;
    } else {
      return false;
    }
  }

  View.OnTouchListener basket_onTouchListener(){
    return new View.OnTouchListener(){
      PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
      PointF StartPT = new PointF(); // Record Start Position of 'img'
      @Override
      public boolean onTouch(View view, MotionEvent motionEvent){
        float halfWidth = basketImageView.getWidth()/2;
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
