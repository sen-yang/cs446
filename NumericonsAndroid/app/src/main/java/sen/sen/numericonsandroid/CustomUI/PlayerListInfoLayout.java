package sen.sen.numericonsandroid.CustomUI;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

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
    //TODO: JUST FOR TESTING...Remove this later...
    for(int i = 0; i < 3; i++) {
      LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
      //if(i < 2) {
        layoutParams.setMargins(0,0,20,0);
      //}
      PlayerInfoView playerInfoView = new PlayerInfoView(getContext());
      addView(playerInfoView, layoutParams);
      //TODO: Set layout param for playerInfoView and add to the linear layout
      playerInfoViewList.add(playerInfoView);
    }
  }

  public void setPlayerList(List<Player> playerList){
    this.playerList = playerList;
    updateView();
  }

  private void updateView(){
    for(int i = 0; i < playerList.size(); i++){
      //TODO CHECK IF i is out of bound!
      if(playerInfoViewList.get(i) != null){
        //If this playerInfoView already exist, update it
        PlayerInfoView playerInfoView = playerInfoViewList.get(i);
        playerInfoView.update(playerList.get(i));
      } else{
        //If not, create a new view and added to playerInfoViewList and multiplayerlayout
        PlayerInfoView playerInfoView = new PlayerInfoView(getContext());
        playerInfoView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
        playerInfoView.setPlayer(playerList.get(i));
        playerInfoViewList.add(playerInfoView);
      }
    }
  }
}
