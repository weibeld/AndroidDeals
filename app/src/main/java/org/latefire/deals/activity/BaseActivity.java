package org.latefire.deals.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import org.latefire.deals.controler.BaseControler;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.dialog.MyProgressDialog;

/**
 * Created by phongnguyen on 3/20/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BaseControler.getInstance().init(DatabaseManager.getInstance());
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
}
