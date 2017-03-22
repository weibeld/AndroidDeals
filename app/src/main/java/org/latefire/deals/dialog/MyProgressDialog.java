package org.latefire.deals.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;
import org.latefire.deals.R;

/**
 * Created by Ken on 3/22/2017.
 */

public class MyProgressDialog extends Dialog {

  public MyProgressDialog(@NonNull Context context) {
    super(context);
  }

  public static MyProgressDialog show(Context context) {
    MyProgressDialog dialog = new MyProgressDialog(context);
    dialog.setContentView(R.layout.dialog_progress);
    dialog.setCancelable(false);
    dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
    lp.dimAmount = 0.2f;
    dialog.getWindow().setAttributes(lp);
     dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    dialog.show();
    return dialog;
  }
}
