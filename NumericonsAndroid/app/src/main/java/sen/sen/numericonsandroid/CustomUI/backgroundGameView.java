package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import android.os.Handler;

import sen.sen.numericonsandroid.Models.DroppedItem;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-02-28.
 */

public class backgroundGameView extends RelativeLayout{
  View rootView;
  private Handler handler;
  private Runnable autoRun;


  public backgroundGameView(Context context){
    super(context);
    init(context);
  }

  public backgroundGameView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  private void init(Context context) {
    handler = new Handler();
    autoRun = new Runnable(){
      public void run() {
        invalidate();
        handler.postDelayed(this, 1000);
      }
    };
  }

  public void addDroppedItem(DroppedItem item) {
    //...
  }
}
