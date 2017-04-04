package org.latefire.deals.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import org.latefire.deals.R;
import org.latefire.deals.auth.AuthActivity;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.database.AbsUser;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.databinding.ActivityHomeBusinessBinding;

public class HomeActivityBusiness extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

  private static final String LOG_TAG = HomeActivityBusiness.class.getSimpleName();

  ActivityHomeBusinessBinding b;

  GoogleApiClient mGoogleApiClient;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_home_business);
    setSupportActionBar(b.toolbarInclude.toolbar);
    setUpNavigationDrawer();

    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API)
        .build();


    // FloatingActionButton for creating a new deal
    b.fab.setOnClickListener(view -> startActivity(new Intent(this, CreateDealActivity.class)));
  }

  @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    b.drawerLayout.closeDrawer(GravityCompat.START);
    switch (item.getItemId()) {
      case R.id.action_sign_out:
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(this, AuthActivity.class));
        finish();
        return true;
    }
    return false;
  }

  private void setUpNavigationDrawer() {
    ActionBarDrawerToggle hamburgerIcon = new ActionBarDrawerToggle(this, b.drawerLayout, b.toolbarInclude.toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    b.drawerLayout.setDrawerListener(hamburgerIcon);
    hamburgerIcon.syncState();
    b.navViewInclude.navViewBusiness.setNavigationItemSelectedListener(this);
    CurrentUserManager.getInstance().getCurrentUser(user -> setHeaderNav(user));
  }

  private void setHeaderNav(AbsUser user) {
    NavigationView navView = b.navViewInclude.navViewBusiness;
    TextView mTvName = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_header_tv_name);
    TextView mTvEmail = (TextView) navView.getHeaderView(0).findViewById(R.id.nav_header_tv_email);
    ImageView mIvAvatar = (ImageView) navView.getHeaderView(0).findViewById(R.id.nav_header_iv_avatar);
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

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    // be available.
    Log.d(LOG_TAG, "onConnectionFailed:" + connectionResult);
    Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
  }
}
