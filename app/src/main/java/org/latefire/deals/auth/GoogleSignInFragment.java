package org.latefire.deals.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import org.latefire.deals.R;
import org.latefire.deals.utils.Constant;

/**
 * A fragment containing a "Sign in with Google" button. Clicking the button allows the user to
 * select a Google account and sign in to it. The result of the sign in is communicated upwards
 * through the {@link OnGoogleSignInListener} which must be implemented by the enclosing activity
 * or fragment.
 */
public class GoogleSignInFragment extends Fragment {

  private static final String LOG_TAG = GoogleSignInFragment.class.getSimpleName();

  SignInButton mButton;
  OnGoogleSignInListener mListener;

  // Required empty public constructor
  public GoogleSignInFragment() {}

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mButton = (SignInButton) inflater.inflate(R.layout.fragment_google_sign_in, container, false);
    mButton.setSize(SignInButton.SIZE_WIDE);
    mButton.setOnClickListener(v -> launchSignInWithGoogle());
    return mButton;
  }

  // Start the "Select Google Account" activity
  private void launchSignInWithGoogle() {
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
    startActivityForResult(signInIntent, Constant.REQUEST_CODE_GOOGLE_SIGN_IN);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Constant.REQUEST_CODE_GOOGLE_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        mListener.onGoogleSignInSuccess(result.getSignInAccount());
      } else {
        mListener.onGoogleSignInFailure(result);
      }
    }
  }

  // Get Google Sign In API client
  private GoogleApiClient getGoogleApiClient() {
    GoogleSignInOptions opts = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id)).requestEmail()
        .build();
    GoogleApiClient apiClient = new GoogleApiClient.Builder(getActivity())
        .enableAutoManage(getActivity(), connectionResult -> Log.d(LOG_TAG, "Could not connect to Google API"))
        .addApi(Auth.GOOGLE_SIGN_IN_API, opts).build();
    return apiClient;
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnGoogleSignInListener) {
      mListener = (OnGoogleSignInListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement " + OnGoogleSignInListener.class.getCanonicalName());
    }
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public interface OnGoogleSignInListener {
    void onGoogleSignInSuccess(GoogleSignInAccount account);
    void onGoogleSignInFailure(GoogleSignInResult result);
  }
}
