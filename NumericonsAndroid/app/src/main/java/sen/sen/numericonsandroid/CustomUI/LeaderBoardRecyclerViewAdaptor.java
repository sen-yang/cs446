package sen.sen.numericonsandroid.CustomUI;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.R;

public class LeaderBoardRecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
  public final static int TYPE_USER = 0;
  public final static int TYPE_LOAD = 1;

  private List<User> userList;
  private boolean isLoading;

  public LeaderBoardRecyclerViewAdaptor(List<User> userList){
    this.userList = userList;
  }

  public void setLoading(boolean loading){
    isLoading = loading;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view;

    if(viewType == TYPE_USER){
      view = inflater.inflate(R.layout.cell_leaderboard_userinfo, parent, false);
      return new UserHolder(view);
    }
    else if(viewType == TYPE_LOAD){
      //todo make cell layout
      view = inflater.inflate(R.layout.cell_loader, parent, false);
      return new LoadHolder(view);
    }
    return null;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
    if(holder instanceof UserHolder){
      ((UserHolder) holder).bindData(position);
    }
  }

  @Override
  public int getItemCount(){
    if(isLoading){
      return userList.size() + 1;
    }
    return userList.size();
  }

  @Override
  public int getItemViewType(int position){
    if(position < userList.size()){
      return TYPE_USER;
    }
    else{
      return TYPE_LOAD;
    }
  }

  class UserHolder extends RecyclerView.ViewHolder{
    private User user;
    TextView userName;
    TextView userScore;
    TextView userRank;

    public UserHolder(View itemView){
      super(itemView);
      userName = itemView.findViewById(R.id.userName);
      userScore = itemView.findViewById(R.id.userScore);
      userRank = itemView.findViewById(R.id.userRank);
    }

    void bindData(int position){
      user = userList.get(position);
      //todo bind views based on user info
      userName.setText(user.getUsername());
      userScore.setText(Integer.toString(user.getRankRating()));
      userRank.setText(Integer.toString(user.getRank()));
      //userScore.setText(user.get);
    }
  }

  class LoadHolder extends RecyclerView.ViewHolder{
    public LoadHolder(View itemView){
      super(itemView);
    }
  }
}
