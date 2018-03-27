package sen.sen.numericonsandroid.Activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.CustomUI.LeaderBoardRecyclerViewAdaptor;
import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

/**
 * Created by Jennifer on 2018-03-20.
 */

public class LeaderBoardActivity extends AppCompatActivity implements WebsocketController.WebsocketListener{
  private RecyclerView recyclerView;
  private LeaderBoardRecyclerViewAdaptor adaptor;
  private LinearLayoutManager linearLayoutManager;
  private List<User> userList;
  private boolean isLoading;
  private boolean isMoreDataAvailable;


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState){
    super.onCreate(savedInstanceState, persistentState);
    recyclerView = findViewById(R.id.leaderBoardRecyclerView);

    userList = new ArrayList<>();
    isLoading = false;
    isMoreDataAvailable = true;

    linearLayoutManager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(linearLayoutManager);
    adaptor = new LeaderBoardRecyclerViewAdaptor(userList);
    recyclerView.setAdapter(adaptor);
    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState){
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy){
        super.onScrolled(recyclerView, dx, dy);
        if(((linearLayoutManager.findLastVisibleItemPosition() + Constants.INFINITE_LOAD_TRIGGER_SIZE) >= userList.size()) && isMoreDataAvailable && !isLoading) {
          loadMore();
        }

      }
    });
    loadMore();
  }

  public void loadMore() {
    setLoading(true);
    //@TODO: Load more users from server... loadMore(userList.size(),Constants.INFINITE_LOAD_SIZE);
  }

  private void setLoading(boolean loading){
    isLoading = loading;
    adaptor.setLoading(loading);
    adaptor.notifyDataSetChanged();
  }

  @Override
  public void onConnected(){

  }

  @Override
  public void onClose(int code, String reason, boolean remote){
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        Toast.makeText(LeaderBoardActivity.this, R.string.server_connection_closed, Toast.LENGTH_LONG).show();
      }
    });
  }

  @Override
  public void userConfirmed(boolean isConfirmed, User user, String errorMessage){

  }

  @Override
  public void gameInitialized(GameState gameState){

  }

  @Override
  public void newUserList(List<User> newUserList, boolean isError){
    if(isError){
      setLoading(false);
      isMoreDataAvailable = false;
      Toast.makeText(this, R.string.cannot_retrieve_users, Toast.LENGTH_LONG).show();
    }
    else{
      userList.addAll(newUserList);
      setLoading(false);

      if(newUserList.size() < Constants.INFINITE_LOAD_SIZE){
        isMoreDataAvailable = false;
      }
    }
  }
}
