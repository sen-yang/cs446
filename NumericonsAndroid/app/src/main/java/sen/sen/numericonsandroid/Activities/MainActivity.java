package sen.sen.numericonsandroid.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

public class MainActivity extends AppCompatActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onButtonPressed(View view){
    Intent intent = new Intent(this, MainGameActivity.class);
    startActivity(intent);
  }
}
