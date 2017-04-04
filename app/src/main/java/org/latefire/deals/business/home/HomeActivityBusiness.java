package org.latefire.deals.business.home;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import org.latefire.deals.R;
import org.latefire.deals.auth.AuthActivity;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.base.BaseActivity;
import org.latefire.deals.customer.home.DealItemViewHolder;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Deal;
import org.latefire.deals.databinding.ActivityHomeBusinessBinding;

public class HomeActivityBusiness extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

  private static final String LOG_TAG = HomeActivityBusiness.class.getSimpleName();

  private ActivityHomeBusinessBinding b;
  private GoogleApiClient mGoogleApiClient;
  private Business mBusiness;
  private FirebaseRecyclerAdapter<Deal, DealItemViewHolder> mAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_home_business);
    setSupportActionBar(b.toolbarInclude.toolbar);

    // For Google sign out
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
        .addApi(Auth.GOOGLE_SIGN_IN_API)
        .build();

    // FloatingActionButton for creating a new deal
    b.fab.setOnClickListener(view -> startActivity(new Intent(this, CreateDealActivity.class)));

    // Get current user and run tasks that depend on the current user
    setUpNavigationDrawer();
    CurrentUserManager.getInstance().getCurrentUser(user -> {
      mBusiness = (Business) user;
      onCurrentUserRetrieved();
    });
  }

  private void onCurrentUserRetrieved() {
    setupNavigationHeader();
    b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    b.recyclerView.setAdapter(new BusinessDealsAdapter(this, mBusiness.getId()));
  }

  private void setUpNavigationDrawer() {
    ActionBarDrawerToggle hamburger = new ActionBarDrawerToggle(this, b.drawerLayout, b.toolbarInclude.toolbar,
        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    b.drawerLayout.setDrawerListener(hamburger);
    hamburger.syncState();
    b.navViewInclude.navViewBusiness.setNavigationItemSelectedListener(this);
  }

  private void setupNavigationHeader() {
    View v = b.navViewInclude.navViewBusiness.getHeaderView(0);
    TextView tvName = (TextView) v.findViewById(R.id.nav_header_tv_name);
    TextView tvEmail = (TextView) v.findViewById(R.id.nav_header_tv_email);
    ImageView ivAvatar = (ImageView) v.findViewById(R.id.nav_header_iv_avatar);
    tvName.setText(mBusiness.getBusinessName() + " (Business)");
    tvEmail.setText(mBusiness.getEmail());
    Glide.with(this)
        .load(mBusiness.getProfilePhoto())
        .asBitmap()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.image_not_found)
        .into(ivAvatar);
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

  @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    // An unresolvable error has occurred and Google APIs (including Sign-In) will no be available.
    Log.d(LOG_TAG, "onConnectionFailed:" + connectionResult);
    Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
  }
}
