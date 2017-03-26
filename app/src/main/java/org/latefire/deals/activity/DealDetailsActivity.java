package org.latefire.deals.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import org.latefire.deals.R;
import org.latefire.deals.database.Deal;
import org.latefire.deals.databinding.ActivityDealDetailsBinding;

public class DealDetailsActivity extends BaseActivity {

  public static final String DEAL = "deal";
  private Toolbar mToolbar;
  private ActivityDealDetailsBinding mBinding;
  private Deal mDeal;

  public static void start(Context context, Deal deal) {
    Intent intent = new Intent(context, DealDetailsActivity.class);
    intent.putExtra(DEAL, deal);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_deal_details);
    supportPostponeEnterTransition();

    mToolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mDeal = (Deal) getIntent().getSerializableExtra(DEAL);
    if (mDeal != null){
      mBinding.setDeal(mDeal);
    }
  }
}
