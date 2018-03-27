package sen.sen.numericonsandroid.Global;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.UUID;

import sen.sen.numericonsandroid.Models.User;

public class SharedPreferencesHelper{
  private static final String USER = "USER";
  private static final String SESSION_ID = "SESSION_ID";
  private static final String IS_SOUND_ENABLED = "IS_SOUND_ENABLED";
  private static User inMemoryUser;
  private static String sessionID;
  private static Boolean isSoundEnabled;

  public static User getSavedUser(){
    if(inMemoryUser != null){
      return inMemoryUser;
    }
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
    Gson gson = new Gson();
    String json = sharedPreferences.getString(USER, "");

    if(json.length() > 0){
      inMemoryUser = gson.fromJson(json, User.class);
      return inMemoryUser;
    }
    else{
      User user = new User(UUID.randomUUID().toString(), Constants.CHARACTER_SPRITE.BIRD_1);
      saveUser(user);
      return user;
    }
  }

  public static void saveUser(User user){
    inMemoryUser = user;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    Gson gson = new Gson();
    String json = gson.toJson(user);
    editor.putString(USER, json);
    editor.apply();
  }

  public static String getSavedSessionID(){
    if(sessionID != null){
      return sessionID;
    }
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
    sessionID = sharedPreferences.getString(SESSION_ID, "");
    return sessionID;
  }

  public static void saveSessionID(String sessionID){
    SharedPreferencesHelper.sessionID = sessionID;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(SESSION_ID, sessionID);
    editor.apply();
  }

  public static boolean GetSoundEnabled(){
    if(isSoundEnabled != null){
      return isSoundEnabled;
    }
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
    isSoundEnabled = sharedPreferences.getBoolean(IS_SOUND_ENABLED, true);
    return isSoundEnabled;
  }

  public static void setSoundEnabled(boolean isSoundEnabled){
    SharedPreferencesHelper.isSoundEnabled = isSoundEnabled;
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getAppContext());
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putBoolean(IS_SOUND_ENABLED, isSoundEnabled);
    editor.apply();

  }

  public static String getUsername(){
    return getSavedUser().getUsername();
  }
}
