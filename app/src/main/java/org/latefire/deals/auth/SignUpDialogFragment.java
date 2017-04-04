package org.latefire.deals.auth;

import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import org.latefire.deals.R;
import org.latefire.deals.database.Business;
import org.latefire.deals.database.Customer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.databinding.DialogSignUpBinding;
import org.latefire.deals.utils.MiscUtils;

/**
 * Created by dw on 25/03/17.
 */

public class SignUpDialogFragment extends AbsAuthDialogFragment {

  private static final String LOG_TAG = SignUpDialogFragment.class.getSimpleName();

  DialogSignUpBinding b;
  String mEmail;
  String mPassword;

  @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    b = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_sign_up, null, false);

    // Select account type
    b.rgUserType.setOnCheckedChangeListener((group, checkedId) -> updateUi(checkedId));

    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Sign Up")
        .setView(b.getRoot())
        .setPositiveButton("Sign Up", (dialog1, which) -> {
          if (validateInput()) {
            mLoadingListener.onLoadingStart();
            mEmail = b.etEmail.getText().toString();
            mPassword = b.etPassword.getText().toString();
            SignUpFirebaseAuthListener listener = new SignUpFirebaseAuthListener();
            AuthManager.getInstance().createUserWithEmailAndPassword(mEmail, mPassword, listener);
          }
        })
        .setNegativeButton("Cancel", (dialog12, which) -> {})
        .create();
    return dialog;
  }

  private void updateUi(int checkedId) {
    switch (checkedId) {
      case R.id.rbCustomer:
        b.etFirstName.setVisibility(View.VISIBLE);
        b.etLastName.setVisibility(View.VISIBLE);
        b.etBusinessName.setVisibility(View.GONE);
        b.etBusinessName.setText("");
        b.etFirstName.requestFocus();
        break;
      case R.id.rbBusiness:
        b.etFirstName.setVisibility(View.GONE);
        b.etFirstName.setText("");
        b.etLastName.setVisibility(View.GONE);
        b.etLastName.setText("");
        b.etBusinessName.setVisibility(View.VISIBLE);
        b.etBusinessName.requestFocus();
        break;
    }
  }

  // TODO: 26/03/17 Improve input validation for sign-up with email and password (e.g. check password and password confirmation)
  private boolean validateInput() {
    String email = b.etEmail.getText().toString();
    String password = b.etPassword.getText().toString();
    String passwordConfirm = b.etPasswordConfirm.getText().toString();
    String firstName = b.etFirstName.getText().toString();
    String lastName = b.etLastName.getText().toString();
    String businessName = b.etBusinessName.getText().toString();

    // Test for empty fields
    boolean emptyField = false;
    if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) emptyField = true;
    if (isCustomer() && (firstName.isEmpty() || lastName.isEmpty())) emptyField = true;
    if (isBusiness() && businessName.isEmpty()) emptyField = true;
    if (emptyField) {
      MiscUtils.toastS(mActivity, "Please fill in all input fields");
      return false;
    }

    return true;
  }

  private boolean isCustomer() {
    return b.rgUserType.getCheckedRadioButtonId() == R.id.rbCustomer;
  }

  private boolean isBusiness() {
    return b.rgUserType.getCheckedRadioButtonId() == R.id.rbBusiness;
  }

  // Step 1: create a new Firebase user with email/password
  class SignUpFirebaseAuthListener implements AuthManager.FirebaseAuthListener {
    @Override public void onAuthSuccess(Task<AuthResult> task) {
      AuthManager.getInstance().signInWithEmailAndPassword(mEmail, mPassword, new SignInFirebaseAuthListener());
    }
    @Override public void onAuthFailure(Task<AuthResult> task) {
      mLoadingListener.onLoadingEnd();
      MiscUtils.toastL(mActivity, "Error: could not create user");
      Log.d(LOG_TAG, "Could not create a new Firebase user: " + task.getException());
    }
  }

  // Step 2: sign in to Firebase with the newly created user
  class SignInFirebaseAuthListener implements AuthManager.FirebaseAuthListener {
    @Override public void onAuthSuccess(Task<AuthResult> task) {
      createUserInDatabase(task.getResult().getUser().getUid());
    }
    @Override public void onAuthFailure(Task<AuthResult> task) {
      mLoadingListener.onLoadingEnd();
      MiscUtils.toastL(mActivity, "Error: could not sign in");
      Log.d(LOG_TAG, "Could not sign in with newly created user: " + task.getException());
    }
  }

  // Step 3: create a new customer or business object in our database
  private void createUserInDatabase(String userId) {
    DatabaseManager m = DatabaseManager.getInstance();
    if (isCustomer()) {
      Customer customer = new Customer();
      customer.setEmail(b.etEmail.getText().toString());
      customer.setFirstName(b.etFirstName.getText().toString());
      customer.setLastName(b.etLastName.getText().toString());
      m.createCustomer(customer, userId, () -> mAuthListener.onAuthComplete());
    } else if (isBusiness()) {
      Business business = new Business();
      business.setEmail(b.etEmail.getText().toString());
      business.setBusinessName(b.etBusinessName.getText().toString());
      m.createBusiness(business, userId, () -> mAuthListener.onAuthComplete());
    }
  }
}
