package org.latefire.deals.customer.deals;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

  private String mCustomerId;

  public ViewPagerAdapter(FragmentManager fm, String customerId) {
    super(fm);
    mCustomerId = customerId;
  }

  @Override public Fragment getItem(int position) {
    if (position == 0) return ViewPagerFragmentOpen.newInstance(mCustomerId);
    else if (position == 1) return ViewPagerFragmentClosed.newInstance(mCustomerId);
    else throw new IllegalArgumentException("Invalid ViewPager page: " + position);
  }

  @Override public int getCount() {
    return 2;
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return "Open";
      case 1:
        return "Closed";
      default:
        return null;
    }
  }
}
