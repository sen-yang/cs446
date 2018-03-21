package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-03-21.
 */

public class PlayerInfoView extends View{
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
    playerIconButton = findViewById(R.id.userIconButton);
    playerName = findViewById(R.id.userName);
    playerCurrentNumber = findViewById(R.id.userCurrentNumber);
  }

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
