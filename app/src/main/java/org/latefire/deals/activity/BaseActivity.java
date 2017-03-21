package org.latefire.deals.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import org.latefire.deals.controler.BaseControler;
import org.latefire.deals.database.DatabaseManager;

/**
 * Created by phongnguyen on 3/20/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    BaseControler.getInstance().init(DatabaseManager.getInstance());
  }
}
