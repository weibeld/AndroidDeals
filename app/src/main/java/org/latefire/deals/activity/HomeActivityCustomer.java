package org.latefire.deals.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import org.latefire.deals.R;
import org.latefire.deals.adapters.ViewPagerAdapter;
import org.latefire.deals.auth.AuthActivity;
import org.latefire.deals.auth.AuthManager;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.database.AbsUser;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.databinding.ActivityHomeCustomerBinding;

/**
 * Created by phongnguyen on 3/19/17.
 */

public class HomeActivityCustomer extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

  private static final String LOG_TAG = HomeActivityCustomer.class.getSimpleName();

  @BindView(R.id.materialViewPager) MaterialViewPager viewPager;
  private ViewPagerAdapter mViewPagerAdapter;
  private DatabaseManager mDatabaseManager;
  private AuthManager mAuthManager;
  private CurrentUserManager mCurrentUserManager;
  private ActivityHomeCustomerBinding mBinding;
  private TextView mTvName;
  private TextView mTvEmail;
  private ImageView mIvAvatar;
  private DrawerLayout mDrawerLayout;
  GoogleApiClient mGoogleApiClient;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_home_customer);
    ButterKnife.bind(this);
    setSupportActionBar(viewPager.getToolbar());
    getSupportActionBar().setSubtitle(" ");
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, viewPager.getToolbar(),
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API)
        .build();


    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mTvName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_tv_name);
    mTvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_tv_email);
    mIvAvatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nav_header_iv_avatar);

    mDatabaseManager = DatabaseManager.getInstance();
    mAuthManager = AuthManager.getInstance();
    mCurrentUserManager = CurrentUserManager.getInstance();

    mCurrentUserManager.getCurrentUser(user -> setHeaderNav(user));

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

  @Override public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  private void setHeaderNav(AbsUser user) {
    if (user instanceof Customer) {
      Customer customer = (Customer) user;
      mTvName.setText(customer.getFirstName() + " " + customer.getLastName() + " (Customer)");
      mTvEmail.setText(customer.getEmail());
      Glide.with(this)
          .load(customer.getProfilePhoto())
          .asBitmap()
          .placeholder(R.drawable.placeholder)
          .error(R.drawable.image_not_found)
          .into(mIvAvatar);
    } else if (user instanceof Business) {
      Business business = (Business) user;
      mTvName.setText(business.getBusinessName() + " (Business)");
      mTvEmail.setText(business.getEmail());
      Glide.with(this)
          .load(business.getProfilePhoto())
          .asBitmap()
          .placeholder(R.drawable.placeholder)
          .error(R.drawable.image_not_found)
          .into(mIvAvatar);
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    //getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Sign out from Firebase and from the Google account
      case R.id.action_sign_out:

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

  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    mDrawerLayout.closeDrawer(GravityCompat.START);
    switch (item.getItemId()) {
      case R.id.nav_sign_out:
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(this, AuthActivity.class));
        finish();
        return true;
    }
    return false;
  }

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(LOG_TAG, "onConnectionFailed:" + connectionResult);
    Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
  }
}
