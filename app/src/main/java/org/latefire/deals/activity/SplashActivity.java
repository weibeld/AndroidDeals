package org.latefire.deals.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.latefire.deals.R;
import org.latefire.deals.auth.AuthActivity;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;

/**
 * Created by phongnguyen on 3/20/17.
 */

public class SplashActivity extends BaseActivity {
  private FirebaseAuth mFirebaseAuth;
  private FirebaseUser mFirebaseUser;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    mFirebaseAuth = FirebaseAuth.getInstance();
    mFirebaseUser = mFirebaseAuth.getCurrentUser();

    //TODO: can't sign in google so temporality solution is opposite with true case (mFirebaseUser == null)
    new Handler().postDelayed(() -> {
      if (mFirebaseUser == null) {
        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      } else {
        CurrentUserManager.getInstance().getCurrentUser(user -> {
          if (user instanceof Business) {
            Intent intent = new Intent(SplashActivity.this, HomeActivityBusiness.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          } else if (user instanceof Customer) {
            Intent intent = new Intent(SplashActivity.this, HomeActivityCustomer.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
          }
        });
      }
    }, 1500);
  }
}
