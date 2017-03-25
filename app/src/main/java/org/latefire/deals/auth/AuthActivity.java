package org.latefire.deals.auth;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import org.latefire.deals.R;
import org.latefire.deals.activity.BaseActivity;
import org.latefire.deals.databinding.ActivityAuthBinding;

// TODO: add sign-in method "Password/Email"

/**
 * This activity is launched by other activities whenever there is no authenticated Firebase user.
 * It allows the user to sign in to an account (currently, Google), and then this account is used
 * to authenticate to Firebase.
 */
public class AuthActivity extends BaseActivity {

  private static final String LOG_TAG = AuthActivity.class.getSimpleName();
  private static final int REQUEST_CODE_SIGN_IN = 0;

  private ActivityAuthBinding b;
  private FirebaseAuth mFirebaseAuth;
  private AuthActivity mActivity;
  private FirebaseAuth.AuthStateListener mAuthListener;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_auth);
    getSupportActionBar().setTitle(R.string.title_sign_in_activity);
    getSupportActionBar().setElevation(0);
    mActivity = this;
    mFirebaseAuth = FirebaseAuth.getInstance();

    b.viewPager.setAdapter(new AuthFragmentPagerAdapter(getSupportFragmentManager(), new SignUpFragment.LoadingListener() {
      @Override public void onLoadingStart() {
        b.progressBar.setVisibility(View.VISIBLE);
      }

      @Override public void onLoadingEnd() {
        b.progressBar.setVisibility(View.GONE);
      }
    }, this));
    b.tabLayout.setupWithViewPager(b.viewPager);

    //// Sign in with Google
    //b.btnSignInWithGoogle.setSize(SignInButton.SIZE_WIDE);
    //b.btnSignInWithGoogle.setOnClickListener(v -> {
    //  Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
    //  startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    //});
    //
    //// Sign in anonymously (just for testing purposes)
    //b.btnSignInAnonymously.setOnClickListener(v ->
    //    mFirebaseAuth.signInAnonymously().addOnCompleteListener(task ->
    //        startActivity(new Intent(mActivity, HomeActivity.class))));
    //
    //// Sign in with email and password
    //b.btnSignInWithEmailPassword.setOnClickListener(v -> {
    //  String email = b.etEmail.getText().toString();
    //  String password = b.etPassword.getText().toString();
    //  mFirebaseAuth.createUserWithEmailAndPassword(email, password)
    //      .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
    //        @Override
    //        public void onComplete(@NonNull Task<AuthResult> task) {
    //          Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
    //          mFirebaseAuth.signInWithEmailAndPassword(email, password);
    //          if (!task.isSuccessful()) {
    //            Log.w(LOG_TAG, "signInWithEmail:failed", task.getException());
    //            Toast.makeText(AuthActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
    //          }
    //        }
    //      });
    //});


    // If user is now signed-in, start HomeActivity
    //mAuthListener = firebaseAuth -> {
    //  if (firebaseAuth.getCurrentUser() != null) {
    //    startActivity(new Intent(mActivity, HomeActivity.class));
    //    finish();  // Prevent AuthActivity from being added to the back stack
    //  }
    //};
  }

  @Override
  public void onStart() {
    super.onStart();
    //mFirebaseAuth.addAuthStateListener(mAuthListener);
  }

  @Override
  public void onStop() {
    super.onStop();
    //mFirebaseAuth.removeAuthStateListener(mAuthListener);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // For Google sign-in: when returning from the Google account selection dialog
    if (requestCode == REQUEST_CODE_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        authToFirebaseWithGoogleAccount(result.getSignInAccount());
      } else {
        Log.e(LOG_TAG, "Google Sign-In failed.");
      }
    }
  }

  // Get the API client for handling the sign in with Google
  private GoogleApiClient getGoogleApiClient() {
    // Configure Google Sign In
    GoogleSignInOptions opts =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
            getString(R.string.default_web_client_id)).requestEmail().build();
    return new GoogleApiClient.Builder(this).enableAutoManage(this, connectionResult -> {
      Log.d(LOG_TAG, "Could not connect to Google to sign in");
      Toast.makeText(mActivity, "Could not connect to Google to sign in", Toast.LENGTH_SHORT).show();
    }).addApi(Auth.GOOGLE_SIGN_IN_API, opts).build();
  }

  // Authenticate to Firebase with an authenticated Google account
  private void authToFirebaseWithGoogleAccount(GoogleSignInAccount account) {
    showProgress();
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
      dismissProgress();
      if (task.isSuccessful()) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        Log.d(LOG_TAG, "User: " + user.getDisplayName() + ", UID: " + user.getUid());
      } else {
        Log.w(LOG_TAG, "signInWithCredential", task.getException());
        Toast.makeText(mActivity, "Authentication to Firebase failed.", Toast.LENGTH_SHORT).show();
      }
    });
  }

}
