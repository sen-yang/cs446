package sen.sen.numericonsandroid.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Global.Helpers;
import sen.sen.numericonsandroid.Global.SharedPreferencesHelper;
import sen.sen.numericonsandroid.Models.GameState;
import sen.sen.numericonsandroid.Models.User;
import sen.sen.numericonsandroid.Networking.WebsocketController;
import sen.sen.numericonsandroid.R;

public class SettingsActivity extends AppCompatActivity implements WebsocketController.WebsocketListener{
  public static final String TAG = "SettingsActivity";
  private LinearLayout settingsLayout;
  private List<ImageView> spriteSelectImageViewList;
  private ToggleButton soundToggleButton;
  private Constants.CHARACTER_SPRITE selectedSprite;
  private Button loginButton;
  private TextView usernameTextView;
  private AlertDialog alertDialog;
  private TextView errorTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    settingsLayout = findViewById(R.id.settingsLayout);
    spriteSelectImageViewList = new ArrayList<>();
    ImageView bird1ImageView = findViewById(R.id.bird1SelectImageView);
    bird1ImageView.setTag(Constants.CHARACTER_SPRITE.BIRD_1);
    spriteSelectImageViewList.add(bird1ImageView);
    ImageView bird2ImageView = findViewById(R.id.bird2SelectImageView);
    bird2ImageView.setTag(Constants.CHARACTER_SPRITE.BIRD_2);
    spriteSelectImageViewList.add(bird2ImageView);
    ImageView bird3ImageView = findViewById(R.id.bird3SelectImageView);
    bird3ImageView.setTag(Constants.CHARACTER_SPRITE.BIRD_3);
    spriteSelectImageViewList.add(bird3ImageView);
    loginButton = findViewById(R.id.loginButton);
    usernameTextView = findViewById(R.id.usernameTextView);
    soundToggleButton = findViewById(R.id.soundToggleButton);
    errorTextView = findViewById(R.id.errorTextView);

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
    selectSprite((Constants.CHARACTER_SPRITE) view.getTag(), true);
  }

  public void loginButtonPressed(View view){
    View loginDialogView = LayoutInflater.from(this).inflate(R.layout.layout_login_dialog, settingsLayout, false);
    final EditText usernameEditText = loginDialogView.findViewById(R.id.usernameEditText);
    final EditText passwordEditText = loginDialogView.findViewById(R.id.passwordEditText);

    alertDialog = new AlertDialog.Builder(this)
        .setTitle(R.string.login_or_sign_up)
        .setView(loginDialogView)
        .setPositiveButton(R.string.login, null)
        .setNegativeButton(R.string.cancel, null)
        .setNeutralButton(R.string.sign_up, null)
        .show();
    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        setErrorMessage("");
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(Helpers.isValidUsername(username) && Helpers.isValidPassword(password)){
          WebsocketController.getInstance().login(username, password);
        }
        else{
          setErrorMessage(getString(R.string.invalid_username_or_password));
        }
      }
    });
    alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        setErrorMessage("");
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(Helpers.isValidUsername(username) && Helpers.isValidPassword(password)){
          WebsocketController.getInstance().register(username, password);
        }
        else{
          setErrorMessage(getString(R.string.invalid_username_or_password));
        }
      }
    });
    alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        setErrorMessage("");
        alertDialog.dismiss();
      }
    });
  }

  private void setUser(User user){
    if(user.isTemporary()){
      usernameTextView.setText("");
      loginButton.setText(R.string.login);
    }
    else{
      usernameTextView.setText(user.getUsername());
      loginButton.setText(R.string.switch_accounts);
    }
    selectSprite(user.getCharacterSprite(), false);
  }

  private void selectSprite(Constants.CHARACTER_SPRITE newSelectedSprite, boolean sendToServer){
    for(ImageView imageView : spriteSelectImageViewList){
      if(imageView.getTag() == newSelectedSprite){
        imageView.setBackgroundColor(getResources().getColor(R.color.selectedGreen));
      }
      else{
        imageView.setBackgroundColor(Color.TRANSPARENT);
      }
    }

    if((selectedSprite != newSelectedSprite)){
      selectedSprite = newSelectedSprite;
      User user = new User(SharedPreferencesHelper.getUsername(), selectedSprite);

      if(sendToServer){
        WebsocketController.getInstance().updateUser(user);
      }
    }
  }

  private void setErrorMessage(String errorMessage){
    errorTextView.setText(errorMessage);

    if(Helpers.isNonEmptyString(errorMessage)){
      if(alertDialog.isShowing()){
        alertDialog.setMessage(errorMessage);
      }
      errorTextView.setVisibility(View.VISIBLE);
    }
    else{
      errorTextView.setVisibility(View.GONE);
    }
  }

  @Override
  public void onConnected(){

  }

  @Override
  public void onClose(int code, String reason, boolean remote){
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        Toast.makeText(SettingsActivity.this, R.string.server_connection_closed, Toast.LENGTH_LONG).show();
      }
    });
  }

  @Override
  public void userConfirmed(final boolean isConfirmed, final User user, final String errorMessage){
    runOnUiThread(new Runnable(){
      @Override
      public void run(){
        if(isConfirmed){
          alertDialog.dismiss();
          setUser(user);
          setErrorMessage("");
          Toast.makeText(SettingsActivity.this, R.string.user_updated, Toast.LENGTH_LONG).show();
        }
        else{
          setErrorMessage(errorMessage);
        }
      }
    });
  }

  @Override
  public void gameInitialized(GameState gameState){

  }

  @Override
  public void newUserList(List<User> newUserList, boolean isError){

  }
}
