package org.latefire.deals.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by dw on 26/03/17.
 */

public class MiscUtils {

  public static void toastS(Activity a, String msg) {
    Toast.makeText(a, msg, Toast.LENGTH_SHORT).show();
  }

  public static void toastL(Activity a, String msg) {
    Toast.makeText(a, msg, Toast.LENGTH_LONG).show();
  }
}
