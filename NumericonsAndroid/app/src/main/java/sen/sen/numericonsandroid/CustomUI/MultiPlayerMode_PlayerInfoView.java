package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import sen.sen.numericonsandroid.Models.Player;

/**
 * Created by Jennifer on 2018-03-21.
 */

public class MultiPlayerMode_PlayerInfoView extends LinearLayout {
  List<Player> playerList;

  public MultiPlayerMode_PlayerInfoView(Context context, List<Player> playerList){
    super(context);
    this.playerList = playerList;
    init(context);
  }

  public MultiPlayerMode_PlayerInfoView(Context context, @Nullable AttributeSet attrs, List<Player> playerList){
    super(context, attrs);
    this.playerList = playerList;
    init(context);
  }

  public MultiPlayerMode_PlayerInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, List<Player> playerList){
    super(context, attrs, defStyleAttr);
    this.playerList = playerList;
    init(context);
  }

  private void init(Context context) {
    //...
    

  }
}
