package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Global.SharedPreferencesHelper;
import sen.sen.numericonsandroid.Models.Player;
import sen.sen.numericonsandroid.R;

public class PlayerListInfoLayout extends LinearLayout{
  List<Player> playerList;
  List<PlayerInfoView> playerInfoViewList;

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

  private void init(){
    playerInfoViewList = new ArrayList<>();
    playerList = new ArrayList<>();
  }

  public void setPlayerList(List<Player> playerList){
    for(int i = 0; i < playerList.size(); i++){
      Player player = playerList.get(i);
`      if(!player.getUsername().equals(SharedPreferencesHelper.getUsername())){
        this.playerList.add(player);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(0, 0, 20, 0);
        PlayerInfoView playerInfoView = new PlayerInfoView(getContext());
        playerInfoView.setTag(i);
        addView(playerInfoView, layoutParams);
        playerInfoView.setPlayer(playerList.get(i));
        playerInfoViewList.add(playerInfoView);
      }
    }
  }

  public void updateView(List<Player> playerList){
    for(PlayerInfoView playerInfoView : playerInfoViewList){
      Player player = playerList.get((int)playerInfoView.getTag());

      if(player != null){
        playerInfoView.update(player);
      }
    }
  }
}
