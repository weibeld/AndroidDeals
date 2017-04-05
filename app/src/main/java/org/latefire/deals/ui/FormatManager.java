package org.latefire.deals.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.latefire.deals.utils.DateUtils;

/**
 * Created by dw on 04/04/17.
 */

public class FormatManager {

  private static FormatManager instance;

  private FormatManager() {}

  public static synchronized FormatManager getInstance() {
    if (instance == null) instance = new FormatManager();
    return instance;
  }

  public SimpleDateFormat getDefaultDateFormat() {
    return DateUtils.DMY_TIME_SLASH_FORMAT;
  }

  public String formatTimestamp(long timestamp) {
    return getDefaultDateFormat().format(new Date(timestamp));
  }
}
