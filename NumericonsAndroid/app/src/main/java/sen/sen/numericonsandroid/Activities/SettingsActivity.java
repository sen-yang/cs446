package sen.sen.numericonsandroid.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.SharedPreferencesHelper;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

public class SettingsActivity extends AppCompatActivity implements WebsocketController.WebsocketListener{
  private List<ImageView> spriteSelectImageViewList;
  private ToggleButton soundToggleButton;
  private Constants.CHARACTER_SPRITE selectedSprite;
  private Button loginButton;
  private AlertDialog alertDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    spriteSelectImageViewList = new ArrayList<>();
    ImageView bird1ImageView = findViewById(R.id.bird1SelectImageView);
    bird1ImageView.setTag(Constants.CHARACTER_SPRITE.BIRD_1);
    spriteSelectImageViewList.add(bird1ImageView);
    ImageView bird2ImageView = findViewById(R.id.bird2SelectImageView);
    bird2ImageView.setTag(Constants.CHARACTER_SPRITE.BIRD_2);
    spriteSelectImageViewList.add(bird2ImageView);
    ImageView bird3ImageView = findViewById(R.id.bird3SelectImageView);
    bird2ImageView.setTag(Constants.CHARACTER_SPRITE.BIRD_3);
    spriteSelectImageViewList.add(bird3ImageView);
    loginButton = findViewById(R.id.loginButton);
    soundToggleButton = findViewById(R.id.soundToggleButton);

    soundToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
      @Override
      public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked){
        if(isChecked){
          SharedPreferencesHelper.setSoundEnabled(isChecked);
        }
      }
    });
  }

  @Override
  protected void onResume(){
    super.onResume();
    WebsocketController.getInstance().removeWebsocketListener(this);
    setUser(SharedPreferencesHelper.getSavedUser());
    soundToggleButton.setChecked(SharedPreferencesHelper.GetSoundEnabled());
  }

  @Override
  protected void onPause(){
    super.onPause();
    WebsocketController.getInstance().removeWebsocketListener(this);
  }

  public void spriteSelectImageViewPressed(View view){
    selectSprite((Constants.CHARACTER_SPRITE)view.getTag(), true);
  }

  public void loginButtonPressed(View view){
    View loginDialogView = LayoutInflater.from(this).inflate(R.layout.layout_login_dialog, null);
    final EditText usernameEditText = loginDialogView.findViewById(R.id.usernameEditText);
    final EditText passwordEditText = loginDialogView.findViewById(R.id.passwordEditText);

    alertDialog = new AlertDialog.Builder(this)
        .setTitle("Login")
        .setView(view)
        .setPositiveButton("Login", new DialogInterface.OnClickListener(){
          @Override
          public void onClick(DialogInterface dialogInterface, int i){

          }
        }).
        setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
          @Override
          public void onClick(DialogInterface dialogInterface, int i){
            alertDialog.dismiss();
          }
        })
        .setNeutralButton("Sign Up", new DialogInterface.OnClickListener(){
          @Override
          public void onClick(DialogInterface dialogInterface, int i){

          }
        })
        .show();
  }

  private void setUser(User user){


    selectSprite(user.getCharacterSprite(), false);
  }

  private void selectSprite(Constants.CHARACTER_SPRITE newSelectedSprite, boolean sendToServer){
    for(ImageView imageView : spriteSelectImageViewList){
      if(imageView.getTag() == newSelectedSprite){
        imageView.setBackgroundColor(Color.TRANSPARENT);
      }
      else{
        imageView.setBackgroundColor(getResources().getColor(R.color.selectedGreen));
      }
    }

    if((selectedSprite != newSelectedSprite)){
      selectedSprite = newSelectedSprite;

      if(sendToServer){
        //todo
      }
    }
  }

  @Override
  public void onConnected(){

  }

  @Override
  public void onClose(int code, String reason, boolean remote){

  }

  @Override
  public void userConfirmed(boolean isConfirmed, User user){

  }

  @Override
  public void gameInitialized(GameState gameState){

  }
}
