package org.latefire.deals.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import org.latefire.deals.R;
import org.latefire.deals.databinding.ActivitySignInBinding;

// TODO: add sign-in method "Password/Email"

/**
 * This activity is launched by other activities whenever there is no authenticated Firebase user.
 * It allows the user to sign in to an account (currently, Google), and then this account is used
 * to authenticate to Firebase.
 */
public class SignInActivity extends BaseActivity {

  private static final String LOG_TAG = SignInActivity.class.getSimpleName();
  private static final int REQUEST_CODE_SIGN_IN = 0;

  private ActivitySignInBinding b;
  private FirebaseAuth mFirebaseAuth;
  private GoogleApiClient mGoogleApiClient;
  private SignInActivity mActivity;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
    getSupportActionBar().setTitle(R.string.title_sign_in_activity);
    mActivity = this;
    mFirebaseAuth = FirebaseAuth.getInstance();

    // Configure Google Sign In
    GoogleSignInOptions opts =
        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
            getString(R.string.default_web_client_id)).requestEmail().build();
    mGoogleApiClient =
        new GoogleApiClient.Builder(this).enableAutoManage(this, connectionResult -> {
          Log.d(LOG_TAG, "Could not connect to Google to sign in");
          Toast.makeText(mActivity, "Could not connect to Google to sign in", Toast.LENGTH_SHORT)
              .show();
        }).addApi(Auth.GOOGLE_SIGN_IN_API, opts).build();

    // On clicking the Google Sign In button, sign in to Google
    b.btnSignInWithGoogle.setSize(SignInButton.SIZE_WIDE);
    b.btnSignInWithGoogle.setOnClickListener(v -> {
      Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
      startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    });

    b.btnSignInAnonymously.setOnClickListener(v -> {
      mFirebaseAuth.signInAnonymously().addOnCompleteListener(task ->
              startActivity(new Intent(mActivity, HomeActivity.class)));
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // When returning from "sign in to Google"
    if (requestCode == REQUEST_CODE_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        authToFirebaseWithGoogleAccount(result.getSignInAccount());
      } else {
        Log.e(LOG_TAG, "Google Sign-In failed.");
      }
    }
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
        startActivity(new Intent(mActivity, HomeActivity.class));
        finish();
      } else {
        Log.w(LOG_TAG, "signInWithCredential", task.getException());
        Toast.makeText(mActivity, "Authentication to Firebase failed.", Toast.LENGTH_SHORT).show();
      }
    });
  }
}
