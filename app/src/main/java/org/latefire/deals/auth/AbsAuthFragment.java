package org.latefire.deals.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.Serializable;
import org.latefire.deals.R;
import org.latefire.deals.activity.HomeActivityCustomer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.databinding.FragmentSignUpBinding;
import org.latefire.deals.utils.Constant;

/**
 * Created by dw on 25/03/17.
 */

public abstract class AbsAuthFragment extends Fragment {

  private static final String LOG_TAG = AbsAuthFragment.class.getSimpleName();

  protected FragmentSignUpBinding b;
  protected DatabaseManager mDatabaseManager;
  protected LoadingListener mLoadingListener;
  protected AuthManager mAuthManager;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof LoadingListener)
      mLoadingListener = (LoadingListener) context;
    else
      throw new ClassCastException(context.toString() + " must implement " + LoadingListener.class.getCanonicalName());
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDatabaseManager = DatabaseManager.getInstance();
    mAuthManager = null; //AuthManager.getInstance();
  }

  protected void authComplete() {
    mLoadingListener.onLoadingEnd();
    startActivity(new Intent(getActivity(), HomeActivityCustomer.class));
    getActivity().finish();
  }

  protected abstract boolean validateInput();

  public interface LoadingListener extends Serializable {
    void onLoadingStart();
    void onLoadingEnd();
  }

  // TODO: 26/03/17 Improve organisatino of Google sign in
  /*----------------------------------------------------------------------------------------------*
   * Google sign in (cannot be moved to AuthManager, because it requires onActivityResult)
   *----------------------------------------------------------------------------------------------*/
  protected void launchSignInWithGoogle() {
    // Start the "Select Google Account" activity
    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getGoogleApiClient());
    startActivityForResult(signInIntent, Constant.REQUEST_CODE_GOOGLE_SIGN_IN);
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

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == Constant.REQUEST_CODE_GOOGLE_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess())
        onGoogleSignInSuccess(result.getSignInAccount());
      else
        onGoogleSignInFailure(result);
    }
  }

  protected abstract void onGoogleSignInSuccess(GoogleSignInAccount account);
  protected abstract void onGoogleSignInFailure(GoogleSignInResult result);
}
