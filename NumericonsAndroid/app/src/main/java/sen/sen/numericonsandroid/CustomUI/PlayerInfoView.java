package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-03-21.
 */

public class PlayerInfoView extends RelativeLayout{
  Player player;
  ImageButton playerIconButton;
  TextView playerName;
  TextView playerCurrentNumber;

  public PlayerInfoView(Context context){
    super(context);
    init();
  }

  public PlayerInfoView(Context context, @Nullable AttributeSet attrs){
    super(context, attrs);
    init();
  }

  public PlayerInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init(){
    //@TODO: JUST for testing, Make the UI for PlayerInfoView!
    playerIconButton = new ImageButton(getContext());
    playerIconButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
    playerIconButton.setId(R.id.multiPlayerImageButton);
    playerIconButton.setImageResource(R.drawable.happy);

    playerName = new TextView(getContext());
    playerName.setText("Cool Name");
    playerName.setId(R.id.multiPlayerName);

    playerCurrentNumber = new TextView(getContext());
    playerCurrentNumber.setText("73");
    playerCurrentNumber.setId(R.id.multiPlayerCurrentNumber);

    RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(100, 100);
    RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    imageParams.addRule(CENTER_HORIZONTAL);

    nameParams.addRule(RelativeLayout.BELOW, playerIconButton.getId());
    nameParams.addRule(CENTER_HORIZONTAL);

    numberParams.addRule(RelativeLayout.BELOW, playerName.getId());
    numberParams.addRule(CENTER_HORIZONTAL);

    addView(playerIconButton, imageParams);
    addView(playerName, nameParams);
    addView(playerCurrentNumber, numberParams);
  }

  //@TODO: might want to remove this one, cuz its redundant..
  public void setPlayer(Player player) {
    update(player);
  }

  public void update(Player player){
    this.player = player;
    //TODO: update view and icon
    playerName.setText(player.getUsername());
    playerCurrentNumber.setText(player.getCurrentNumber());
  }
}
