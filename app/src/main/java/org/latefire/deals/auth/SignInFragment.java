package org.latefire.deals.auth;

/**
 * Created by dw on 25/03/17.
 */

public class SignInFragment extends AbsAuthFragment {
  //@Override protected boolean validateInput() {
  //  return false;
  //}
  //
  //@Override protected void onGoogleSignInSuccess(GoogleSignInAccount account) {
  //
  //}
  //
  //@Override protected void onGoogleSignInFailure(GoogleSignInResult result) {
  //
  //}

  //private static final String LOG_TAG = SignInFragment.class.getSimpleName();
  //
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
  //
  ///*----------------------------------------------------------------------------------------------*
  // * Misc
  // *----------------------------------------------------------------------------------------------*/
  //
  //// TODO: 26/03/17 Implement input validation for sign-in with email and password
  //@Override protected boolean validateInput() {
  //  return true;
  //}


}
