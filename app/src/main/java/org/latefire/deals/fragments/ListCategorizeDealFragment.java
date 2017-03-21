package org.latefire.deals.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;

import org.latefire.deals.R;
import org.latefire.deals.adapters.DealFirebaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by phongnguyen on 3/19/17.
 */
public class ListCategorizeDealFragment extends BaseFrament {

  private static final String TAG = ListCategorizeDealFragment.class.getSimpleName();
  @BindView(R.id.rv_deal_list) RecyclerView rvDealList;
  public ListCategorizeDealFragment() {
  }

  public static ListCategorizeDealFragment newInstance() {
    ListCategorizeDealFragment fragment = new ListCategorizeDealFragment();
    return fragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  /**
   * Change the null parameter in {@code inflater.inflate()}
   * to a layout resource {@code R.layout.example}
   */
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_categorize_deal_list, container, false);
    ButterKnife.bind(this, rootView);

    rvDealList.setLayoutManager(new LinearLayoutManager(getContext()));
    rvDealList.addItemDecoration(new MaterialViewPagerHeaderDecorator());
    rvDealList.setAdapter(new DealFirebaseAdapter(getContext()));
    return rootView;
  }

  @Override public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
  }

  @Override public void onResume() {
    super.onResume();
  }

  @Override public void onPause() {
    super.onPause();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }
}