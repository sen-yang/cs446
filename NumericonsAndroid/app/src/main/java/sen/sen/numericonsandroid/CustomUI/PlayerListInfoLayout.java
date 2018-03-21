package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-03-21.
 */

public class PlayerListInfoLayout extends LinearLayout {
  List<Player> playerList;
  List<PlayerInfoView> playerInfoViewList;
  LinearLayout multiplayerlayout;

  public PlayerListInfoLayout(Context context){
    super(context);
    init();
  }

  public PlayerListInfoLayout(Context context, @Nullable AttributeSet attrs){
    super(context, attrs);
    init();
  }

  public PlayerListInfoLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    multiplayerlayout = findViewById(R.id.multiplayerlayout);
    playerInfoViewList = new ArrayList<>();
    for(Player player: playerList) {
      PlayerInfoView playerInfoView = new PlayerInfoView(getContext());
      playerInfoView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
      playerInfoView.setPlayer(player);
      multiplayerlayout.addView(playerInfoView);
    }
  }

  public void setPlayerList(List<Player> playerList) {
    this.playerList = playerList;
    updateView();
  }

  private void updateView() {
    for(int i = 0; i < playerList.size(); i++){
      //TODO CHECK IF i is out of bound!
      PlayerInfoView playerInfoView = playerInfoViewList.get(i);
      playerInfoView.update(playerList.get(i));
    }
  }
}
