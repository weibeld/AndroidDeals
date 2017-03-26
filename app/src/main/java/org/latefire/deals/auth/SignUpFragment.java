package org.latefire.deals.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import org.latefire.deals.R;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.databinding.FragmentSignUpBinding;
import org.latefire.deals.utils.MiscUtils;

/**
 * Created by dw on 25/03/17.
 */

public class SignUpFragment extends AbsAuthFragment {
  
  private static final String LOG_TAG = SignUpFragment.class.getSimpleName();

  private FragmentSignUpBinding b;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    b = FragmentSignUpBinding.inflate(inflater, container, false);

    // Select account type
    b.rgUserType.setOnCheckedChangeListener((group, checkedId) -> swapVisibleTextFields(checkedId));

    // Sign up with email/password
    b.btnSignUpWithEmailPassword.setOnClickListener(v -> {
      if (validateInput()) {
        mLoadingListener.onLoadingStart();
        String email = b.etEmail.getText().toString();
        String password = b.etPassword.getText().toString();
        signUpEmailPasswordStep1(email, password);
      }
    });

    // Sign up with Google
    b.btnSignInWithGoogle.setSize(SignInButton.SIZE_WIDE);
    b.btnSignInWithGoogle.setOnClickListener(v -> {
      mLoadingListener.onLoadingStart();
      launchSignInWithGoogle();
    });

    return b.getRoot();
  }

  /*----------------------------------------------------------------------------------------------*
   * Sign up with e-mail and password
   *----------------------------------------------------------------------------------------------*/

  // Step 1: create a new Firebase user with the specified email and password
  private void signUpEmailPasswordStep1(String email, String password) {
    mAuthManager.createUserWithEmailAndPassword(email, password, new AuthManager.FirebaseAuthListener() {
      @Override public void onAuthSuccess(Task<AuthResult> task) {
        signUpEmailPasswordStep2(email, password);
      }
      @Override public void onAuthFailure(Task<AuthResult> task) {
        mLoadingListener.onLoadingEnd();
        MiscUtils.toastL(getActivity(), "Error: " + task.getException().getMessage());
        Log.w(LOG_TAG, "Could not create Firebase user with email/password: ", task.getException());
      }
    });
  }

  // Step 2: sign in to Firebase with newly created user
  private void signUpEmailPasswordStep2(String email, String password) {
    mAuthManager.signInWithEmailAndPassword(email, password, new AuthManager.FirebaseAuthListener() {
      @Override public void onAuthSuccess(Task<AuthResult> task) {
        signUpEmailPasswordStep3(task.getResult().getUser().getUid());
      }
      @Override public void onAuthFailure(Task<AuthResult> task) {
        mLoadingListener.onLoadingEnd();
        MiscUtils.toastL(getActivity(), "Error: could not sign in user");
        Log.w(LOG_TAG, "Could not sign in with email/password: ", task.getException());
      }
    });
  }

  // Step 3: create a Customer or Business from the entered information and save it in our database
  private void signUpEmailPasswordStep3(String userId) {
    if (isSigningUpCustomer()) {
      Customer customer = new Customer();
      customer.setEmail(b.etEmail.getText().toString());
      customer.setFirstName(b.etFirstName.getText().toString());
      customer.setLastName(b.etLastName.getText().toString());
      mDatabaseManager.createCustomer(customer, userId, this::authComplete);
    }
    else if (isSigningUpBusiness()) {
      Business business = new Business();
      business.setEmail(b.etEmail.getText().toString());
      business.setBusinessName(b.etBusinessName.getText().toString());
      mDatabaseManager.createBusiness(business, userId, this::authComplete);
    }
  }

  /*----------------------------------------------------------------------------------------------*
   * Sign up with Google
   *----------------------------------------------------------------------------------------------*/

  // Step 0: sign in to a Google account (select Google account from dialog)
  @Override protected void onGoogleSignInSuccess(GoogleSignInAccount account) {
    signUpGoogleStep1(account);
  }
  @Override protected void onGoogleSignInFailure(GoogleSignInResult result) {
    mLoadingListener.onLoadingEnd();
    MiscUtils.toastL(getActivity(), "Error: could not sign in to your Google account");
    Log.d(LOG_TAG, "Could not sign in to Google: " + result.getStatus());
  }

  // Step 1: sign in to Firebase with the selected Google account
  private void signUpGoogleStep1(GoogleSignInAccount account) {
    mAuthManager.signInWithGoogleAccount(account, new AuthManager.FirebaseAuthListener() {
      @Override public void onAuthSuccess(Task<AuthResult> task) {
        signUpGoogleStep2(task.getResult().getUser().getUid(), account);
      }
      @Override public void onAuthFailure(Task<AuthResult> task) {
        mLoadingListener.onLoadingEnd();
        MiscUtils.toastL(getActivity(), "Error: could not sign in with your Google account");
        Log.d(LOG_TAG, "Could not sign in with Google: " + task.getException());
      }
    });
  }

  // Step 2: create a Customer or Business from the Google account and save it in our database
  private void signUpGoogleStep2(String userId, GoogleSignInAccount account) {
    if (isSigningUpCustomer()) {
      Customer customer = new Customer();
      customer.setEmail(account.getEmail());
      customer.setFirstName(account.getGivenName());
      customer.setLastName(account.getFamilyName());
      mDatabaseManager.createCustomer(customer, userId, this::authComplete);
    }
    else if (isSigningUpBusiness()) {
      Business business = new Business();
      business.setEmail(account.getEmail());
      business.setBusinessName(account.getDisplayName());
      mDatabaseManager.createBusiness(business, userId, this::authComplete);
    }
  }

  /*----------------------------------------------------------------------------------------------*
   * Misc
   *----------------------------------------------------------------------------------------------*/

  private void setUserDisplayName(FirebaseUser user, String name) {
    user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());
  }
  
  private boolean isSigningUpCustomer() {
    return b.rgUserType.getCheckedRadioButtonId() == R.id.rbCustomer;
  }
  
  private boolean isSigningUpBusiness() {
    return !isSigningUpCustomer();
  }

  private void swapVisibleTextFields(int checkedId) {
    switch (checkedId) {
      case R.id.rbCustomer:
        b.etFirstName.setVisibility(View.VISIBLE);
        b.etLastName.setVisibility(View.VISIBLE);
        b.etBusinessName.setVisibility(View.GONE);
        b.etBusinessName.setText("");
        break;
      case R.id.rbBusiness:
        b.etFirstName.setVisibility(View.GONE);
        b.etFirstName.setText("");
        b.etLastName.setVisibility(View.GONE);
        b.etLastName.setText("");
        b.etBusinessName.setVisibility(View.VISIBLE);
        break;
    }
  }

  // TODO: 25/03/17 Implement input validation for sign-up with email and password
  @Override protected boolean validateInput() {
    return true;
  }
}
