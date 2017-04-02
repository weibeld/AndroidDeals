package org.latefire.deals.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import org.latefire.deals.R;
import org.latefire.deals.databinding.DialogSignInBinding;
import org.latefire.deals.utils.MiscUtils;

/**
 * Created by dw on 25/03/17.
 */

public class SignInDialogFragment extends DialogFragment {

  private static final String LOG_TAG = SignInDialogFragment.class.getSimpleName();

  DialogSignInBinding b;
  AuthActivity mActivity;
  OnAuthCompleteListener mAuthListener;
  OnLoadingListener mLoadingListener;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof AuthActivity) {
      mActivity = (AuthActivity) context;
      mAuthListener = (OnAuthCompleteListener) context;
      mLoadingListener = (OnLoadingListener) context;
    } else {
      throw new ClassCastException(context.toString() + " must implement " + OnAuthCompleteListener.class.getCanonicalName());
    }
  }

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    b = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.dialog_sign_in, null, false);

    AlertDialog dialog = new AlertDialog.Builder(mActivity).setTitle("Sign In")
        .setView(b.getRoot())
        .setPositiveButton("Sign In", (dialog1, which) -> {
          if (validateInput()) {
            mLoadingListener.onLoadingStart();
            String email = b.etEmail.getText().toString();
            String password = b.etPassword.getText().toString();
            SignInFirebaseAuthListener listener = new SignInFirebaseAuthListener();
            AuthManager.getInstance().signInWithEmailAndPassword(email, password, listener);
          }
        })
        .setNegativeButton("Cancel", (dialog12, which) -> {})
        .create();
    return dialog;
  }

  //FragmentSignInBinding b;
  //
  //@Nullable @Override
  //public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
  //  b = FragmentSignInBinding.inflate(inflater, container, false);
  //
  //  // Sign in with email and password (Firebase account must already exist)
  //  b.btnSignInWithEmailPassword.setOnClickListener(v -> {
  //    if (validateInput()) {
  //      mLoadingListener.onLoadingStart();
  //      String email = b.etEmail.getText().toString();
  //      String password = b.etPassword.getText().toString();
  //      signInWithEmailAndPassword(email, password);
  //    }
  //  });
  //
  //  // Sign in with Google account (Firebase account will be created if it doesn't exist)
  //  b.btnSignInWithGoogle.setSize(SignInButton.SIZE_WIDE);
  //  b.btnSignInWithGoogle.setOnClickListener(v -> {
  //    mLoadingListener.onLoadingStart();
  //    launchSignInWithGoogle();
  //  });
  //
  //  return b.getRoot();
  //}
  //
  ///*----------------------------------------------------------------------------------------------*
  // * Sign in with email and password
  // *----------------------------------------------------------------------------------------------*/
  //
  //private void signInWithEmailAndPassword(String email, String password) {
  //  mAuthManager.signInWithEmailAndPassword(email, password, new AuthManager.FirebaseAuthListener() {
  //    @Override public void onAuthSuccess(Task<AuthResult> task) {
  //      authComplete();
  //    }
  //    @Override public void onAuthFailure(Task<AuthResult> task) {
  //      mLoadingListener.onLoadingEnd();
  //      MiscUtils.toastL(getActivity(), "Invalid email and/or password");
  //      Log.d(LOG_TAG, "Could not sign in with email and password: " + task.getException());
  //    }
  //  });
  //}
  //
  ///*----------------------------------------------------------------------------------------------*
  // * Sign in with Google
  // *----------------------------------------------------------------------------------------------*/
  //
  //// Step 0: sign in to a Google account (select Google account from dialog)
  //@Override protected void onGoogleSignInSuccess(GoogleSignInAccount account) {
  //  signInWithGoogle(account);
  //}
  //@Override protected void onGoogleSignInFailure(GoogleSignInResult result) {
  //  mLoadingListener.onLoadingEnd();
  //  MiscUtils.toastL(getActivity(), "Error: could not sign in to your Google account");
  //  Log.d(LOG_TAG, "Could not sign in to Google: " + result.getStatus());
  //}
  //
  //// Step 1: sign in to Firebase with the selected Google account
  //private void signInWithGoogle(GoogleSignInAccount account) {
  //  mAuthManager.signInWithGoogleAccount(account, new AuthManager.FirebaseAuthListener() {
  //    @Override public void onAuthSuccess(Task<AuthResult> task) {
  //      authComplete();
  //    }
  //    @Override public void onAuthFailure(Task<AuthResult> task) {
  //      mLoadingListener.onLoadingEnd();
  //      MiscUtils.toastL(getActivity(), "Error: could not sign in with your Google account");
  //      Log.d(LOG_TAG, "Could not sign in with Google: " + task.getException());
  //    }
  //  });
  //}

  /*----------------------------------------------------------------------------------------------*
   * Misc
   *----------------------------------------------------------------------------------------------*/

  // TODO: 26/03/17 Immprove input validation for sign-in with email and password
  private boolean validateInput() {
    String email = b.etEmail.getText().toString();
    String password = b.etPassword.getText().toString();
    if (email.isEmpty() || password.isEmpty()) {
      MiscUtils.toastS(mActivity, "Email or password must not be empty");
      return false;
    }
    return true;
  }

  class SignInFirebaseAuthListener implements AuthManager.FirebaseAuthListener {
    @Override public void onAuthSuccess(Task<AuthResult> task) {
      //CurrentUserManager.getInstance().getCurrentUser(user -> {
      //  Class target = null;
      //  if (user instanceof Customer) target = Customer.class;
      //  else if (user instanceof Business) target = Business.class;
      //  else throw new IllegalArgumentException("Invalid user type");
      //  startActivity(new Intent(mActivity, target));
      //});
      mAuthListener.onAuthComplete();
    }
    @Override public void onAuthFailure(Task<AuthResult> task) {
      mLoadingListener.onLoadingEnd();
      MiscUtils.toastL(mActivity, "Invalid email and/or password");
      Log.d(LOG_TAG, "Could not sign in with email and password: " + task.getException());
    }
  }

  public interface OnAuthCompleteListener {
    void onAuthComplete();
  }

  public interface OnLoadingListener {
    void onLoadingStart();
    void onLoadingEnd();
  }
}
