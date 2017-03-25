package org.latefire.deals.auth;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import org.latefire.deals.R;

/**
 * Created by dw on 25/03/17.
 */

public class AuthFragmentPagerAdapter extends FragmentPagerAdapter {

  private final int PAGE_COUNT = 2;

  private Context mContext;
  private SignUpFragment.LoadingListener mLoadingListener;

  public AuthFragmentPagerAdapter(FragmentManager fm, SignUpFragment.LoadingListener listener, Context c) {
    super(fm);
    mContext = c;
    mLoadingListener = listener;
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return SignUpFragment.newInstance(mLoadingListener);
      case 1:
        return SignUpFragment.newInstance(mLoadingListener);
      default:
        throw new IllegalArgumentException(position + " is not a valid fragment position");
    }
  }

  @Override public int getCount() {
    return PAGE_COUNT;
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return mContext.getString(R.string.title_sign_up_fragment);
      case 1:
        return mContext.getString(R.string.title_sign_in_fragment);
      default:
        throw new IllegalArgumentException(position + " is not a valid fragment position");
    }
  }
}
