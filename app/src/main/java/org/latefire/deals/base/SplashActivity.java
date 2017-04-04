package org.latefire.deals.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import org.latefire.deals.R;
import org.latefire.deals.auth.AuthActivity;
import org.latefire.deals.auth.AuthManager;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.business.home.HomeActivityBusiness;
import org.latefire.deals.customer.home.HomeActivityCustomer;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;

/**
 * Created by phongnguyen on 3/20/17.
 */

public class SplashActivity extends BaseActivity {

  private SplashActivity mActivity;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    mActivity = this;
    if (AuthManager.getInstance().isSignedIn()) startHomeActivity();
    else startActivity(new Intent(this, AuthActivity.class));
  }

  private void startHomeActivity() {
    CurrentUserManager.getInstance().getCurrentUser(user -> {
      if (user instanceof Customer)
        startActivity(new Intent(mActivity, HomeActivityCustomer.class));
      else if (user instanceof Business)
        startActivity(new Intent(mActivity, HomeActivityBusiness.class));
    });
  }
}
