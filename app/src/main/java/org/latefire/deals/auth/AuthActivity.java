package org.latefire.deals.auth;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import org.latefire.deals.R;
import org.latefire.deals.activity.BaseActivity;
import org.latefire.deals.activity.HomeActivity;
import org.latefire.deals.databinding.ActivityAuthBinding;
import org.latefire.deals.utils.MiscUtils;

/**
 * This activity is launched by other activities whenever there is no authenticated Firebase user.
 * It allows the user to sign in to an account (currently, Google), and then this account is used
 * to authenticate to Firebase.
 */
public class AuthActivity extends BaseActivity implements GoogleSignInFragment.OnGoogleSignInListener {

  private static final String LOG_TAG = AuthActivity.class.getSimpleName();

  private ActivityAuthBinding b;
  private AuthActivity mActivity;
  private AuthManager mAuthManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_auth);
    getSupportActionBar().setTitle(R.string.title_sign_in_activity);
    mActivity = this;
    mAuthManager = AuthManager.getInstance();

    showGoogleSignInFragment();
  }

  private void showGoogleSignInFragment() {
    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
    t.replace(R.id.googleSignInFragmentContainer, new GoogleSignInFragment());
    t.commit();
  }

  @Override public void onSuccess(GoogleSignInAccount account) {
    showProgress();
    signInWithGoogle(account);
  }

  @Override public void onFailure(GoogleSignInResult result) {
    MiscUtils.toastL(this, "Error: could not sign in to your Google account");
    Log.d(LOG_TAG, "Could not sign in to Google: " + result.getStatus());
  }

  private void signInWithGoogle(GoogleSignInAccount account) {
    mAuthManager.signInWithGoogleAccount(account, new AuthManager.FirebaseAuthListener() {
      @Override public void onAuthSuccess(Task<AuthResult> task) {
        dismissProgress();
        authComplete();
      }
      @Override public void onAuthFailure(Task<AuthResult> task) {
        dismissProgress();
        MiscUtils.toastL(mActivity, "Error: could not sign in with your Google account");
        Log.d(LOG_TAG, "Could not sign in with Google: " + task.getException());
      }
    });
  }

  protected void authComplete() {
    startActivity(new Intent(this, HomeActivity.class));
    finish();
  }

  //@Override public void onLoadingStart() {
  //  showProgress();
  //}
  //
  //@Override public void onLoadingEnd() {
  //  dismissProgress();
  //}
}
