package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-03-21.
 */

public class PlayerInfoView extends RelativeLayout{
  Player player;
  ImageView playerImageView;
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
    playerImageView = new ImageView(getContext());
    playerImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    playerImageView.setId(R.id.multiPlayerImageButton);
    playerImageView.setImageResource(R.drawable.bird2);

    //@TODO: change these later!
    playerName = new TextView(getContext());
    playerName.setText("Cool Name");
    playerName.setId(R.id.multiPlayerName);
    playerName.setTextSize(15);
    playerName.setTextColor(Color.WHITE);
    playerName.setFilters( new InputFilter[] {new InputFilter.LengthFilter(15) } );

    playerCurrentNumber = new TextView(getContext());
    playerCurrentNumber.setText("73");
    playerCurrentNumber.setTextColor(Color.WHITE);
    playerCurrentNumber.setTextSize(15);
    playerCurrentNumber.setId(R.id.multiPlayerCurrentNumber);

    RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams((int) getResources().getDimension(R.dimen.playerInfoImageWidth), (int) getResources().getDimension(R.dimen.playerInfoImageHeight));
    RelativeLayout.LayoutParams nameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    RelativeLayout.LayoutParams numberParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    imageParams.addRule(CENTER_HORIZONTAL);

    nameParams.addRule(RelativeLayout.BELOW, playerImageView.getId());
    nameParams.addRule(CENTER_HORIZONTAL);

    numberParams.addRule(RelativeLayout.BELOW, playerName.getId());
    numberParams.addRule(CENTER_HORIZONTAL);

    addView(playerImageView, imageParams);
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
    int bird_rid;
    //@TODO: CHnage this later...
    switch(player.getCharacterSprite()) {
      case BIRD_1:
        bird_rid = R.drawable.bird1;
        break;
      case BIRD_2:
        bird_rid = R.drawable.bird2;
        break;
      case BIRD_3:
        bird_rid = R.drawable.bird3;
        break;
      default:
        bird_rid = R.drawable.bird1;
          break;
    }
    playerImageView.setImageResource(bird_rid);
    playerName.setText(player.getUsername());
    playerCurrentNumber.setText(Integer.toString(player.getCurrentNumber()));
  }
}
