package org.latefire.deals.auth;

import android.app.DialogFragment;
import android.content.Context;

/**
 * Created by dw on 02/04/17.
 */

public abstract class AbsAuthDialogFragment extends DialogFragment {

  AuthActivity mActivity;
  AbsAuthDialogFragment.OnAuthCompleteListener mAuthListener;
  AbsAuthDialogFragment.OnLoadingListener mLoadingListener;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof AuthActivity) {
      mActivity = (AuthActivity) context;
      mAuthListener = (AbsAuthDialogFragment.OnAuthCompleteListener) context;
      mLoadingListener = (AbsAuthDialogFragment.OnLoadingListener) context;
    } else {
      throw new ClassCastException(context.toString() + " must implement " + OnAuthCompleteListener.class.getCanonicalName());
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
