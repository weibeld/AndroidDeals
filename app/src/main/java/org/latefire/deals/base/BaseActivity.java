package org.latefire.deals.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.WindowManager;
import org.latefire.deals.R;
import org.latefire.deals.database.DatabaseManager;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by phongnguyen on 3/20/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BaseControler.getInstance().init(DatabaseManager.getInstance());
  }

  @Override protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  /**
   * A dialog showing a progress indicator
   */
  protected MyProgressDialog mProgressDialog;

  public void showProgress() {

    if (mProgressDialog != null && mProgressDialog.isShowing()) {
      return;
    }
    mProgressDialog = MyProgressDialog.show(this);

    mProgressDialog.show();
  }

  /**
   * cancel progress dialog.
   */
  public void dismissProgress() {
    if (mProgressDialog != null) {
      mProgressDialog.dismiss();
      mProgressDialog = null;
    }
  }

  /**
   * Created by Ken on 3/22/2017.
   */

  public static class MyProgressDialog extends Dialog {

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
}
