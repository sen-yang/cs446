package sen.sen.numericonsandroid.Global;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application{
  private static Context context;

  public void onCreate() {
    super.onCreate();
    BaseApplication.context = getApplicationContext();
  }

  public static Context getAppContext() {
    return BaseApplication.context;
  }
}
