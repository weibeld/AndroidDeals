package org.latefire.deals.auth;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import org.latefire.deals.R;
import org.latefire.deals.activity.BaseActivity;
import org.latefire.deals.databinding.ActivityAuthBinding;

import static org.latefire.deals.auth.AbsAuthFragment.LoadingListener;

/**
 * This activity is launched by other activities whenever there is no authenticated Firebase user.
 * It allows the user to sign in to an account (currently, Google), and then this account is used
 * to authenticate to Firebase.
 */
public class AuthActivity_copy extends BaseActivity implements LoadingListener {

  private static final String LOG_TAG = AuthActivity_copy.class.getSimpleName();

  private ActivityAuthBinding b;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_auth);
    getSupportActionBar().setTitle(R.string.title_sign_in_activity);
    getSupportActionBar().setElevation(0);
    //b.viewPager.setAdapter(new AuthFragmentPagerAdapter(this, getSupportFragmentManager()));
    //b.tabLayout.setupWithViewPager(b.viewPager);
  }

  @Override public void onLoadingStart() {
    showProgress();
  }

  @Override public void onLoadingEnd() {
    dismissProgress();
  }
}
