package org.latefire.deals;

import android.app.Application;

/**
 * Created by hung.nguyen on 3/21/2017.
 */

public class MyApplication extends Application {
  private static MyApplication sInstance;

  @Override public void onCreate() {
    super.onCreate();
    sInstance = this;
  }

  public synchronized static MyApplication getInstance() {
    return sInstance;
  }
}
