package org.latefire.deals.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import org.latefire.deals.R;
import org.latefire.deals.adapters.ViewPagerAdapter;
import org.latefire.deals.auth.AuthActivity;
import org.latefire.deals.auth.AuthManager;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.utils.Constant;

/**
 * Created by phongnguyen on 3/19/17.
 */

public class HomeActivity extends BaseActivity {

  private static final String LOG_TAG = HomeActivity.class.getSimpleName();

  @BindView(R.id.materialViewPager) MaterialViewPager viewPager;
  private ViewPagerAdapter mViewPagerAdapter;
  private GoogleApiClient mGoogleApiClient;
  private DatabaseManager mDatabaseManager;
  private AuthManager mAuthManager;
  private String mCurrentUserId;
  private int mCurrentUserType;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    setSupportActionBar(viewPager.getToolbar());
    getSupportActionBar().setSubtitle(" ");

    mDatabaseManager = DatabaseManager.getInstance();
    mAuthManager = AuthManager.getInstance();
    mGoogleApiClient = getGoogleApiClient();
    mCurrentUserId = mAuthManager.getCurrentUserId();

    // Determine type of current user and set ActionBar subtitle
    mDatabaseManager.getUserType(mCurrentUserId, result -> {
      mCurrentUserType = result;
      setActionBarSubtitle();
    });

    mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
    viewPager.getViewPager().setAdapter(mViewPagerAdapter);
    viewPager.setMaterialViewPagerListener(page -> {
      switch (page) {
        case 0:
          return HeaderDesign.fromColorResAndUrl(R.color.green,
              "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
        case 1:
          return HeaderDesign.fromColorResAndUrl(R.color.blue,
              "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
        case 2:
          return HeaderDesign.fromColorResAndUrl(R.color.cyan,
              "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
        case 3:
          return HeaderDesign.fromColorResAndUrl(R.color.red,
              "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
      }

      //execute others actions if needed (ex : modify your header logo)
      return null;
    });

    viewPager.getViewPager()
        .setOffscreenPageLimit(viewPager.getViewPager().getAdapter().getCount());
    viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());

    final View logo = findViewById(R.id.logo_white);
    if (logo != null) {
      logo.setOnClickListener(v -> {
        viewPager.notifyHeaderChanged();
        Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT)
            .show();
      });
    }
  }

  private void setActionBarSubtitle() {
    if (mCurrentUserType == Constant.USER_TYPE_CUSTOMER) {
      mDatabaseManager.getCustomer(mCurrentUserId, model -> {
        Customer customer = (Customer) model;
        getSupportActionBar().setSubtitle(customer.getFirstName() + " " + customer.getLastName() + " (Customer)");
      });
    }
    else if (mCurrentUserType == Constant.USER_TYPE_BUSINESS) {
      mDatabaseManager.getBusiness(mCurrentUserId, model -> {
        Business business = (Business) model;
        getSupportActionBar().setSubtitle(business.getBusinessName() + " (Business)");
      });
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Sign out from Firebase and from the Google account
      case R.id.action_sign_out:
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(this, AuthActivity.class));
        return true;
      case R.id.action_create_deal:
        startActivity(new Intent(this, CreateDealActivity.class));
        return true;
      case R.id.action_sample_show_customer:
        Intent intent = new Intent(this, SampleShowCustomerActivity.class);
        intent.putExtra(getString(R.string.extra_customer_id), "dummy-customer");
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private GoogleApiClient getGoogleApiClient() {
    return new GoogleApiClient.Builder(this).enableAutoManage(this, connectionResult -> {
      Log.d("FB error", "Could not connect to Google for signing in");
      Toast.makeText(this, "Could not connect to Google for signing in", Toast.LENGTH_SHORT).show();
    }).addApi(Auth.GOOGLE_SIGN_IN_API).build();
  }
}
