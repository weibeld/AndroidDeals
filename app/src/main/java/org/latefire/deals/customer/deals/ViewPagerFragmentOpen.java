package org.latefire.deals.customer.deals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.GregorianCalendar;
import org.latefire.deals.database.DealAcquired;
import org.latefire.deals.databinding.FragmentCustomerDealsBinding;

import static org.latefire.deals.utils.Constant.ARG_CUSTOMER_ID;

/**
 * Created by dw on 03/04/17.
 */

public class ViewPagerFragmentOpen extends Fragment implements ItemFilter {
  
  private static final String LOG_TAG = ViewPagerFragmentOpen.class.getSimpleName();

  FragmentCustomerDealsBinding b;
  private String mCustomerId;

  public static ViewPagerFragmentOpen newInstance(String customerId) {
    ViewPagerFragmentOpen fragment = new ViewPagerFragmentOpen();
    Bundle args = new Bundle();
    args.putString(ARG_CUSTOMER_ID, customerId);
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    b = FragmentCustomerDealsBinding.inflate(inflater, container, false);
    mCustomerId = getArguments().getString(ARG_CUSTOMER_ID);

    b.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    b.recyclerView.setAdapter(new CustomerDealsRecyclerAdapter(getActivity(), mCustomerId, false, this));

    return b.getRoot();
  }

  @Override public boolean isDisplayItem(DealAcquired dealAcquired) {
    long timestamp = GregorianCalendar.getInstance().getTimeInMillis();
    return dealAcquired.getRedemptionDate() == null && dealAcquired.getDeal().getEndValidity() >= timestamp;
  }
}
