package org.latefire.deals.auth;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import org.latefire.deals.R;
import org.latefire.deals.activity.BaseActivity;
import org.latefire.deals.activity.HomeActivityBusiness;
import org.latefire.deals.activity.HomeActivityCustomer;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.databinding.ActivityAuthBinding;
import org.latefire.deals.utils.MiscUtils;

/**
 * This activity is launched by other activities whenever there is no authenticated Firebase user.
 * It allows the user to sign in to an account (currently, Google), and then this account is used
 * to authenticate to Firebase.
 */
public class AuthActivity extends BaseActivity implements GoogleSignInFragment.OnGoogleSignInListener,
    SelectUserTypeDialogFragment.OnUserTypeSelectedListener,
    AbsAuthDialogFragment.OnAuthCompleteListener, AbsAuthDialogFragment.OnLoadingListener {

  private static final String LOG_TAG = AuthActivity.class.getSimpleName();

  private ActivityAuthBinding b;
  private AuthActivity mActivity;
  private AuthManager mAuthManager;
  private DatabaseManager mDatabaseManager;

  private GoogleSignInAccount mGoogleAccount;
  private String mUserId;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_auth);
    setSupportActionBar(b.toolbarInclude.toolbar);
    getSupportActionBar().setTitle(R.string.title_sign_in_activity);
    mActivity = this;
    mAuthManager = AuthManager.getInstance();
    mDatabaseManager = DatabaseManager.getInstance();

    showGoogleSignInFragment();

    b.btnSignUpWithEmailPassword.setOnClickListener(v -> showSignUpDialog());
    
    b.btnSignInWithEmailPassword.setOnClickListener(v -> showSignInDialog());
  }

  private void showSignInDialog() {
    new SignInDialogFragment().show(getFragmentManager(), "SignIn");
  }

  private void showSignUpDialog() {
    new SignUpDialogFragment().show(getFragmentManager(), "SignUp");
  }

  private void showGoogleSignInFragment() {
    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
    t.replace(R.id.googleSignInFragmentContainer, new GoogleSignInFragment());
    t.commit();
  }

  // Callbacks of the GoogleSignInFragment (called when user has selected a Google account)
  @Override public void onGoogleSignInSuccess(GoogleSignInAccount account) {
    mGoogleAccount = account;
    showProgress();
    signInWithGoogleAccount(mGoogleAccount);
  }
  @Override public void onGoogleSignInFailure(GoogleSignInResult result) {
    if (result.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_FAILED) {
      MiscUtils.toastL(this, "Error: could not sign in to your Google account");
      Log.d(LOG_TAG, "Could not sign in to Google: " + result.getStatus());
    }
  }

  private void signInWithGoogleAccount(GoogleSignInAccount account) {
    mAuthManager.signInWithGoogleAccount(account, new AuthManager.FirebaseAuthListener() {
      @Override public void onAuthSuccess(Task<AuthResult> task) {
        mUserId = task.getResult().getUser().getUid();
        mDatabaseManager.isUserSignedUp(mUserId, isUserSignedUp -> {
          if (!isUserSignedUp)
            new SelectUserTypeDialogFragment().show(getFragmentManager(), "tag");
          else authComplete();
        });
      }
      @Override public void onAuthFailure(Task<AuthResult> task) {
        dismissProgress();
        MiscUtils.toastL(mActivity, "Error: could not sign in with your Google account");
        Log.d(LOG_TAG, "Could not sign in with Google: " + task.getException());
      }
    });
  }

  @Override public void onCustomerSelected() {
    Customer customer = new Customer();
    customer.setEmail(mGoogleAccount.getEmail());
    customer.setFirstName(mGoogleAccount.getGivenName());
    customer.setLastName(mGoogleAccount.getFamilyName());
    customer.setProfilePhoto(mGoogleAccount.getPhotoUrl().toString());
    mDatabaseManager.createCustomer(customer, mUserId, this::authComplete);
  }

  @Override public void onBusinessSelected(String businessName) {
    Business business = new Business();
    business.setEmail(mGoogleAccount.getEmail());
    business.setBusinessName(businessName);
    business.setProfilePhoto(mGoogleAccount.getPhotoUrl().toString());
    mDatabaseManager.createBusiness(business, mUserId, this::authComplete);
  }

  protected void authComplete() {
    CurrentUserManager.getInstance().getCurrentUser(user -> {
      Class target = null;
      if (user instanceof Customer) target = HomeActivityCustomer.class;
      else if (user instanceof Business) target = HomeActivityBusiness.class;
      dismissProgress();
      startActivity(new Intent(mActivity, target));
      finish();
    });
  }

  @Override public void onAuthComplete() {
    CurrentUserManager.getInstance().getCurrentUser(user -> {
      Class target;
      if (user instanceof Customer) target = HomeActivityCustomer.class;
      else if (user instanceof Business) target = HomeActivityBusiness.class;
      else throw new IllegalArgumentException("Invalid user type");
      dismissProgress();
      startActivity(new Intent(mActivity, target));
    });
  }

  @Override public void onLoadingStart() {
    showProgress();
  }

  @Override public void onLoadingEnd() {
    dismissProgress();
  }
}
