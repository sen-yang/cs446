package sen.sen.numericonsandroid.Global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.UUID;

import sen.sen.numericonsandroid.Models.User;

public class SharedPreferencesHelper{
  public static final String USER = "USER";
  private static User inMemoryUser;

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
      User user = new User(UUID.randomUUID().toString(), Constants.USER_CHARACTER.BIRD_1);
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

  public static String getUsername(){
    return getSavedUser().getUsername();
  }
}
