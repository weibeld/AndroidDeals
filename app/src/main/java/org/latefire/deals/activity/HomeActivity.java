package org.latefire.deals.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.latefire.deals.R;
import org.latefire.deals.adapters.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by phongnguyen on 3/19/17.
 */

public class HomeActivity extends BaseActivity {

  private static final String LOG_TAG = HomeActivity.class.getSimpleName();

  @BindView(R.id.materialViewPager) MaterialViewPager viewPager;
  private ViewPagerAdapter adapter;
  private FirebaseAuth mFirebaseAuth;
  private GoogleApiClient mGoogleApiClient;
  private FirebaseUser mFirebaseUser;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseUser = mFirebaseAuth.getCurrentUser();

    Log.d(LOG_TAG, "Firebase user UID: " + mFirebaseUser.getUid());

    mGoogleApiClient = getGoogleApiClient();

    final Toolbar toolbar = viewPager.getToolbar();
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      if (mFirebaseUser != null) {

        String userMsg =
            String.format(getString(R.string.current_user), mFirebaseUser.getDisplayName());
        getSupportActionBar().setSubtitle(userMsg);
      }
    }

    // Testing
    // *********************************************************************************************
    //String id = "-KfjF6oBnZSR02m4Oc3Z";
    //DatabaseManager.getInstance().getDeal(id, new DatabaseManager.SingleQueryCallback() {
    //  @Override public void yourResult(AbsModel model) {
    //    Deal deal = (Deal) model;
    //    Log.d(LOG_TAG, "Result of getDeal ID " + id + ": " + deal.toString());
    //  }
    //});

    //DatabaseManager mgr2 = DatabaseManager.getInstance();
    //String businessID = mgr2.createBusiness(TestUtils.getDummyBusiness());
    //String dealId = mgr2.createDeal(TestUtils.getDummyDeal(businessID));
    //Log.d(LOG_TAG, "Test deal: " + dealId);
    // *********************************************************************************************

    adapter = new ViewPagerAdapter(getSupportFragmentManager());
    viewPager.getViewPager().setAdapter(adapter);
    viewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
      @Override public HeaderDesign getHeaderDesign(int page) {
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
      }
    });

    viewPager.getViewPager()
        .setOffscreenPageLimit(viewPager.getViewPager().getAdapter().getCount());
    viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());

    final View logo = findViewById(R.id.logo_white);
    if (logo != null) {
      logo.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          viewPager.notifyHeaderChanged();
          Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT)
              .show();
        }
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
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(this, SignInActivity.class));
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
