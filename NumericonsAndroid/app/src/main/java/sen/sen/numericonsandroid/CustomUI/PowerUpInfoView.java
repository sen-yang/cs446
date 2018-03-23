package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sen.sen.numericonsandroid.Models.PowerUp;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-03-21.
 */

public class PowerUpInfoView extends RelativeLayout{
  PowerUp powerUp;
  ImageButton powerUPImage;
  TextView powerUpName;

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
    //@TODO: JUST for testing, Make the UI for PowerupItem!
    powerUPImage = new ImageButton(getContext());
    powerUPImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
    powerUPImage.setId(R.id.powerUpImageButton);
    powerUPImage.setImageResource(R.drawable.basket2);

    powerUpName = new TextView(getContext());
    powerUpName.setText("COOL STUFF!");

    RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(150, 150);
    RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    imageParams.addRule(CENTER_HORIZONTAL);
    nameParams.addRule(RelativeLayout.BELOW, powerUPImage.getId());
    nameParams.addRule(CENTER_HORIZONTAL);

    addView(powerUPImage, imageParams);
    addView(powerUpName, nameParams);
  }

  public void update(PowerUp powerUp){
    this.powerUp = powerUp;
    //@TODO: update UI accordingly
  }
}
