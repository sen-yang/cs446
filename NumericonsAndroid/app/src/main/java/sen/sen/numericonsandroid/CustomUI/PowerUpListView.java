package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Models.PowerUp;
import sen.sen.numericonsandroid.R;

public class PowerUpListView extends LinearLayout{
  List<PowerUp> powerUpList;
  List<PowerUpInfoView> powerUpInfoViewList;

  public PowerUpListView(Context context){
    super(context);
    init();
  }

  public PowerUpListView(Context context, @Nullable AttributeSet attrs){
    super(context, attrs);
    init();
  }

  public PowerUpListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init(){
    powerUpList = new ArrayList<>();
    powerUpInfoViewList = new ArrayList<>();

    //TODO: JUST FOR TESTING...Remove this later...
    for(int i = 0; i < 3; i++) {
      LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
      PowerUpInfoView powerUpInfoView = new PowerUpInfoView(getContext());
      addView(powerUpInfoView, layoutParams);
      //TODO: Set layout param for powerUpInfoView and add to the linear layout
      powerUpInfoViewList.add(powerUpInfoView);
    }
  }

  public void setPowerUPList(List<PowerUp> powerUpList){
    this.powerUpList = powerUpList;
    updateView();
  }

  public void updateView(){
    for(int i = 0; i < powerUpList.size(); i++){
      //TODO CHECK IF i is out of bound!
      if(powerUpInfoViewList.get(i) != null){
        //If this powerUpInfoView already exist, update it
        PowerUpInfoView powerUpInfoView = powerUpInfoViewList.get(i);
        powerUpInfoView.update(powerUpList.get(i));
      } else{
        //If not, create a new view and added to powerUpInfoViewList and ...
        PowerUpInfoView powerUpInfoView = new PowerUpInfoView(getContext());

        //TODO: Set layout param for powerUpInfoView and add to the linear layout, Use code in init as example!
        powerUpInfoView.update(powerUpList.get(i));
        powerUpInfoViewList.add(powerUpInfoView);
      }
    }
  }
}
