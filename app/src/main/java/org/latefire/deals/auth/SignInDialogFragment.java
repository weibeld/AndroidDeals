package org.latefire.deals.auth;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class SignInDialogFragment extends AbsAuthDialogFragment {

  private static final String LOG_TAG = SignInDialogFragment.class.getSimpleName();

  DialogSignInBinding b;

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

  // TODO: 26/03/17 Improve input validation for sign-in with email and password
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
      mAuthListener.onAuthComplete();
    }
    @Override public void onAuthFailure(Task<AuthResult> task) {
      mLoadingListener.onLoadingEnd();
      MiscUtils.toastL(mActivity, "Invalid email and/or password");
      Log.d(LOG_TAG, "Could not sign in with email and password: " + task.getException());
    }
  }
}
