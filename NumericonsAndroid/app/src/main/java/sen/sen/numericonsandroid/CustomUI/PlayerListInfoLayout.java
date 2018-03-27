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
      if(!player.getUsername().equals(SharedPreferencesHelper.getUsername())) {
        //TODO CHECK IF i is out of bound!
        this.playerList.add(player);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        layoutParams.setMargins(0,0,20,0);
        PlayerInfoView playerInfoView = new PlayerInfoView(getContext());
        addView(playerInfoView, layoutParams);
        playerInfoView.setPlayer(playerList.get(i));
        playerInfoViewList.add(playerInfoView);
      }
    }
  }

  private void updateView(){
    for(int i = 0; i < playerList.size(); i++){
      //TODO CHECK IF i is out of bound!
      if(i < playerInfoViewList.size()) {
        if(playerInfoViewList.get(i) != null){
          //If this playerInfoView already exist, update it
          PlayerInfoView playerInfoView = playerInfoViewList.get(i);
          playerInfoView.update(playerList.get(i));
        }
      }
    }
  }
}
