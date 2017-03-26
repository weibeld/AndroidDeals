package org.latefire.deals;

import android.app.Application;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by hung.nguyen on 3/21/2017.
 */

public class MyApplication extends Application {
  private static MyApplication sInstance;

  @Override public void onCreate() {
    super.onCreate();
    sInstance = this;
    CalligraphyConfig.initDefault(
        new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Omnes-Regular.otf")
            .setFontAttrId(R.attr.fontPath)
            .build());
  }

  public synchronized static MyApplication getInstance() {
    return sInstance;
  }
}
