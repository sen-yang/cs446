package sen.sen.numericonsandroid.Global;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

import sen.sen.numericonsandroid.R;

public class Helpers{
  public static float randomFloat(String seed){
    //todo implement seed
    return (float) Math.random();
  }

  public static int randomIntInRange(int min, int max, String seed){
    return Math.round(randomFloat(seed) * (max - min)) + min;
  }

  public static float randomFloatInRange(float min, float max, String seed){
    return (randomFloat(seed) * (max - min)) + min;
  }
   // numberView.setImageResource(R.drawable.b1_run_l);
}
