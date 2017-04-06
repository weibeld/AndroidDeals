package org.latefire.deals.base;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.android.databinding.library.baseAdapters.BR;
import org.latefire.deals.R;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.database.AbsUser;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.databinding.ActivityUserProfileBinding;

public class UserProfileActivity extends BaseActivity {

  private UiHandler mUiHandler;
  private MenuItem mMenuDone;

  public static void start(Activity activity) {
    Intent intent = new Intent(activity, UserProfileActivity.class);
    activity.startActivity(intent);
  }

  ActivityUserProfileBinding b;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //getSupportActionBar().setTitle("Sample: Show Customer");
    b = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
    mUiHandler = new UiHandler();
    mUiHandler.setMode(UiHandler.MODE_VIEW);
    b.setUi(mUiHandler);
    setSupportActionBar(b.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    CurrentUserManager.getInstance().getCurrentUser(user -> {
      setFields(user);
    });
  }

  @Override protected void onStart() {
    super.onStart();
  }

  @Override protected void onStop() {
    super.onStop();
  }

  private void setFields(AbsUser user) {
    if (user instanceof Business) {
      Business business = (Business) user;
      b.setBusiness(business);
      mUiHandler.setUserType(UiHandler.USER_TYPE_BUSINESS);
    }
    if (user instanceof Customer) {
      Customer customer = (Customer) user;
      b.setCustomer(customer);
      mUiHandler.setUserType(UiHandler.USER_TYPE_CUSTOMER);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.user_profile, menu);
    mMenuDone = menu.findItem(R.id.user_profile_done);
    return super.onCreateOptionsMenu(menu);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        supportFinishAfterTransition();
        return true;
      case R.id.user_profile_edit:
        mUiHandler.setMode(UiHandler.MODE_EDIT);
        item.setVisible(false);
        mMenuDone.setVisible(true);
        return true;
      case R.id.user_profile_done:
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public class UiHandler extends BaseObservable {
    public static final int MODE_VIEW = 1;
    public static final int MODE_EDIT = 2;
    public static final int USER_TYPE_BUSINESS = 3;
    public static final int USER_TYPE_CUSTOMER = 4;

    private int mode;
    private int userType;

    public int getMode() {
      return mode;
    }

    public void setMode(int mode) {
      this.mode = mode;
      notifyPropertyChanged(BR._all);
    }

    public int getUserType() {
      return userType;
    }

    public void setUserType(int userType) {
      this.userType = userType;
      notifyPropertyChanged(BR._all);
    }
  }
}
