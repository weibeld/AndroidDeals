package org.latefire.deals.auth;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.latefire.deals.Constants;
import org.latefire.deals.databinding.FragmentSignInBinding;

/**
 * Created by dw on 25/03/17.
 */

public class SignInFragment extends Fragment {

  private static final String LOG_TAG = SignInFragment.class.getSimpleName();

  private FragmentSignInBinding b;
  private int mPageNumber;

  public static SignInFragment newInstance(int pageNumber) {
    Bundle args = new Bundle();
    args.putInt(Constants.ARG_PAGE_NUMBER, pageNumber);
    SignInFragment fragment = new SignInFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mPageNumber = getArguments().getInt(Constants.ARG_PAGE_NUMBER);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    b = FragmentSignInBinding.inflate(inflater, container, false);
    return b.getRoot();
  }

}
