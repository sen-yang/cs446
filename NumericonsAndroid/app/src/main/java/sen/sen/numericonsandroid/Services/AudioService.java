package sen.sen.numericonsandroid.Services;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.SparseArray;

import java.io.IOException;

import sen.sen.numericonsandroid.Global.SharedPreferencesHelper;
import sen.sen.numericonsandroid.R;

public class AudioService extends Service{
  private static final String TAG = "AudioService";
  private static final int MAX_SOUND_STREAMDS = 5;
  private static final float MUSIC_VOLUME = 0.5f;

  private MediaPlayer mediaPlayer;
  private SoundPool soundPool;
  private SparseArray<Integer> soundIds;
  private boolean isSoundEnabled;
  private IBinder binder;

  @Override
  public IBinder onBind(Intent arg0){
    return binder;
  }

  @Override
  public void onRebind(Intent intent){
    super.onRebind(intent);
  }

  @Override
  public boolean onUnbind(Intent intent){
    return true;
  }

  public class AudioServiceBinder extends Binder{
    public AudioService getService(){
      return AudioService.this;
    }
  }

  @Override
  public void onCreate(){
    super.onCreate();
    binder = new AudioServiceBinder();
    mediaPlayer = MediaPlayer.create(this, R.raw.background_music);
    mediaPlayer.setLooping(true); // Set looping
    mediaPlayer.setVolume(MUSIC_VOLUME, MUSIC_VOLUME);

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
      AudioAttributes attrs = new AudioAttributes.Builder()
          .setUsage(AudioAttributes.USAGE_GAME)
          .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
          .build();
      soundPool = new SoundPool.Builder()
          .setMaxStreams(MAX_SOUND_STREAMDS)
          .setAudioAttributes(attrs)
          .build();
    }
    else{
      soundPool = new SoundPool(MAX_SOUND_STREAMDS, AudioManager.STREAM_MUSIC, 0);
    }
    soundIds = new SparseArray<>();
    soundIds.append(R.raw.peck, soundPool.load(this, R.raw.peck, 1));
    soundIds.append(R.raw.chirp, soundPool.load(this, R.raw.chirp, 1));
    soundIds.append(R.raw.quack, soundPool.load(this, R.raw.quack, 1));
    soundIds.append(R.raw.hoot, soundPool.load(this, R.raw.hoot, 1));
    setSoundEnable(SharedPreferencesHelper.GetSoundEnabled());
    mediaPlayer.start();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId){
    return START_STICKY;
  }

  @Override
  public void onStart(Intent intent, int startId){
    // TO DO
  }

  public IBinder onUnBind(Intent arg0){
    // TO DO Auto-generated method
    return null;
  }

  public void playSoundEffect(int soundId){
    if(isSoundEnabled){
      soundPool.play(soundIds.get(soundId), 1, 1, 1, 0, 1.0f);
    }
  }

  public void setSoundEnable(boolean isSoundEnabled){
    this.isSoundEnabled = isSoundEnabled;

    if(isSoundEnabled){
      mediaPlayer.setVolume(MUSIC_VOLUME, MUSIC_VOLUME);
    }
    else{
      mediaPlayer.setVolume(0, 0);
    }
  }

  public void startSong(int soundId){
    mediaPlayer.reset();
    AssetFileDescriptor afd = getResources().openRawResourceFd(soundId);
    if(afd == null){
      return;
    }
    try{
      mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
      afd.close();
    } catch(IOException e){
      e.printStackTrace();
    }
    mediaPlayer.start();
  }

  @Override
  public void onDestroy(){
    mediaPlayer.stop();
    mediaPlayer.release();
    soundPool.release();
  }

  @Override
  public void onLowMemory(){
    mediaPlayer.stop();
    mediaPlayer.release();
    soundPool.release();
  }
}
