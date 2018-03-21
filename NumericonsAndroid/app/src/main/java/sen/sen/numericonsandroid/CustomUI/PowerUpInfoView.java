package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import sen.sen.numericonsandroid.Models.PowerUp;

/**
 * Created by Jennifer on 2018-03-21.
 */

public class PowerUpInfoView extends View{
  PowerUp powerUp;

  public PowerUpInfoView(Context context){
    super(context);
    init();
  }

  public PowerUpInfoView(Context context, @Nullable AttributeSet attrs){
    super(context, attrs);
    init();
  }

  public PowerUpInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init(){
    //@TODO: Make the UI for PowerupItem
  }

  public void update(PowerUp powerUp){
    this.powerUp = powerUp;
    //@TODO: update UI accordingly
  }
}
