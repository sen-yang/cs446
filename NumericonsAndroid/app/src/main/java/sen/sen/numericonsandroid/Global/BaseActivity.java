package sen.sen.numericonsandroid.Global;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import sen.sen.numericonsandroid.Services.AudioService;

public class BaseActivity extends AppCompatActivity{
  private boolean isBoundToAudioService;
  private AudioService audioService;

  @Override
  public void onStart(){
    super.onStart();

    Intent mIntent = new Intent(this, AudioService.class);

    bindService(mIntent, audioServiceConnection, BIND_AUTO_CREATE);
    setVolumeControlStream(AudioManager.STREAM_MUSIC);
  }

  ServiceConnection audioServiceConnection = new ServiceConnection(){
    @Override
    public void onServiceDisconnected(ComponentName name){
      isBoundToAudioService = false;
      audioService = null;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service){
      isBoundToAudioService = true;
      AudioService.AudioServiceBinder audioServiceBinder = (AudioService.AudioServiceBinder) service;
      audioService = audioServiceBinder.getService();
    }
  };

  @Override
  protected void onStop(){
    super.onStop();

    if(isBoundToAudioService){
      unbindService(audioServiceConnection);
    }
  }

  protected void playSong(int resId){
    if(audioService != null){
      audioService.startSong(resId);
    }
  }

  protected void playSoundEffect(int resId){
    if(audioService != null){
      audioService.playSoundEffect(resId);
    }
  }

  protected void setSoundEnable(boolean isSoundEnabled){
    if(audioService != null){
      audioService.setSoundEnable(isSoundEnabled);
    }
  }
}