package org.latefire.deals.business.deal.customers;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import org.latefire.deals.R;
import org.latefire.deals.database.Deal;
import org.latefire.deals.databinding.ActivityDealCustomersBinding;

import static org.latefire.deals.customer.home.DealDetailsActivity.ARG_DEAL;

public class DealCustomersActivity extends AppCompatActivity {

  ActivityDealCustomersBinding b;
  Deal mDeal;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_deal_customers);
    setSupportActionBar(b.toolbarInclude.toolbar);

    mDeal = (Deal) getIntent().getSerializableExtra(ARG_DEAL);
    getSupportActionBar().setTitle(mDeal.getTitle());
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    b.recyclerView.setAdapter(new DealCustomersAdapter(this, mDeal.getId()));
  }
}
