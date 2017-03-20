package org.latefire.deals.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.latefire.deals.R;

/**
 * Created by phongnguyen on 3/20/17.
 */

public class SplashActivity extends BaseActivity {
  private FirebaseAuth mFirebaseAuth;
  private FirebaseUser mFirebaseUser;
  private GoogleApiClient mGoogleApiClient;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseUser = mFirebaseAuth.getCurrentUser();

    Intent t;
    //TODO: can't sign in google so temporality solution is opposite with true case (mFirebaseUser == null)
    if (mFirebaseUser != null) {
      t = new Intent(this, SignInActivity.class);
    } else {
      t = new Intent(this, HomeActivity.class);
    }

    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(t);
  }
}
