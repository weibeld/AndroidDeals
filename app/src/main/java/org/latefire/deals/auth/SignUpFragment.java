package org.latefire.deals.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import java.io.Serializable;
import org.latefire.deals.Constants;
import org.latefire.deals.R;
import org.latefire.deals.activity.HomeActivity;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.databinding.FragmentSignUpBinding;

/**
 * Created by dw on 25/03/17.
 */

public class SignUpFragment extends Fragment {
  
  private static final String LOG_TAG = SignUpFragment.class.getSimpleName();

  private FragmentSignUpBinding b;
  private FirebaseAuth mFirebaseAuth;
  private DatabaseManager mDatabaseManager;
  private LoadingListener mLoadingListener;

  public static SignUpFragment newInstance(LoadingListener listener) {
    Bundle args = new Bundle();
    args.putSerializable(Constants.ARG_FRAG_LISTENER, listener);
    SignUpFragment fragment = new SignUpFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLoadingListener = (LoadingListener) getArguments().getSerializable(Constants.ARG_FRAG_LISTENER);
    mFirebaseAuth = FirebaseAuth.getInstance();
    mDatabaseManager = DatabaseManager.getInstance();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    b = FragmentSignUpBinding.inflate(inflater, container, false);

    b.rgUserType.setOnCheckedChangeListener((group, checkedId) -> {
      switch (checkedId) {
        case R.id.rbCustomer:
          showEditText(b.etFirstName);
          showEditText(b.etLastName);
          hideEditText(b.etBusinessName);
          break;
        case R.id.rbBusiness:
          hideEditText(b.etFirstName);
          hideEditText(b.etLastName);
          showEditText(b.etBusinessName);
          break;
      }
    });

    b.btnSignUpWithEmailPassword.setOnClickListener(v -> {
      if (!validateInput()) return;
      mLoadingListener.onLoadingStart();
      String email = b.etEmail.getText().toString();
      String password = b.etPassword.getText().toString();
      // Create a new Firebase user
      mFirebaseAuth.createUserWithEmailAndPassword(email, password)
          .addOnCompleteListener(getActivity(), createUserTask -> {
            if (createUserTask.isSuccessful()) {
              // Sign in newly created user
              mFirebaseAuth.signInWithEmailAndPassword(email, password)
                  .addOnCompleteListener(getActivity(), signInTask -> {
                    if (signInTask.isSuccessful()) {
                      FirebaseUser user = mFirebaseAuth.getCurrentUser();
                      if (isSignUpCustomer()) {
                        String firstName = b.etFirstName.getText().toString();
                        String lastName = b.etLastName.getText().toString();
                        setUserDisplayName(user, firstName + " " + lastName);
                        Customer customer = new Customer();
                        customer.setEmail(email);
                        customer.setFirstName(firstName);
                        customer.setLastName(lastName);
                        mDatabaseManager.signUpCustomer(customer, user.getUid(), this::signUpComplete);
                      }
                      else if (isSignUpBusiness()) {
                        String businessName = b.etBusinessName.getText().toString();
                        setUserDisplayName(user, businessName);
                        Business business = new Business();
                        business.setEmail(email);
                        business.setBusinessName(businessName);
                        mDatabaseManager.signUpBusiness(business, user.getUid(), this::signUpComplete);
                      }
                    } else {
                      mLoadingListener.onLoadingEnd();
                      Log.w(LOG_TAG, "Could not sign in with email/password: ", createUserTask.getException());
                    }
                  });
            } else {
              mLoadingListener.onLoadingEnd();
              Log.w(LOG_TAG, "Could not create Firebase user with email/password: ", createUserTask.getException());
            }
          });
    });

    // TODO: 25/03/17 Sign up with Google
    // Sign in with Google
    b.btnSignInWithGoogle.setSize(SignInButton.SIZE_WIDE);
    b.btnSignInWithGoogle.setOnClickListener(v -> {
      Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
      startActivityForResult(signInIntent, Constants.REQUEST_CODE_GOOGLE_SIGN_IN);
    });



    return b.getRoot();
  }

  // Get the API client for handling the sign in with Google
  private GoogleApiClient getGoogleApiClient() {
    // Configure Google Sign In
    GoogleSignInOptions opts = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
    return new GoogleApiClient.Builder(getActivity()).enableAutoManage(getActivity(), connectionResult -> {
      Log.d(LOG_TAG, "Could not connect to Google to sign in");
    }).addApi(Auth.GOOGLE_SIGN_IN_API, opts).build();
  }

  // Authenticate to Firebase with an authenticated Google account
  private void authToFirebaseWithGoogleAccount(GoogleSignInAccount account) {
    mLoadingListener.onLoadingStart();
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), task -> {
      if (task.isSuccessful()) {
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        signUpComplete();
        Log.d(LOG_TAG, "User: " + user.getDisplayName() + ", UID: " + user.getUid());
      } else {
        Log.w(LOG_TAG, "signInWithCredential", task.getException());
      }
    });
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    // For Google sign-in: when returning from the Google account selection dialog
    if (requestCode == Constants.REQUEST_CODE_GOOGLE_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        authToFirebaseWithGoogleAccount(result.getSignInAccount());
      } else {
        Log.e(LOG_TAG, "Google Sign-In failed.");
      }
    }
  }



  private void signUpComplete() {
    mLoadingListener.onLoadingEnd();
    startActivity(new Intent(getActivity(), HomeActivity.class));
    getActivity().finish();
  }

  private void setUserDisplayName(FirebaseUser user, String name) {
    user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());

  }
  
  private boolean isSignUpCustomer() {
    return b.rgUserType.getCheckedRadioButtonId() == R.id.rbCustomer;
  }
  
  private boolean isSignUpBusiness() {
    return !isSignUpCustomer();
  }
  
  private void hideEditText(EditText et) {
    et.setText("");
    et.setVisibility(View.GONE);
  }
  
  private void showEditText(EditText et) {
    et.setVisibility(View.VISIBLE);
  }

  // TODO: 25/03/17 Validate sign up input
  private boolean validateInput() {
    return true;
  }

  public interface LoadingListener extends Serializable{
    void onLoadingStart();
    void onLoadingEnd();
  }
}
