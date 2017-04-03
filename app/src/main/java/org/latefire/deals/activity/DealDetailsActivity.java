package org.latefire.deals.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import org.latefire.deals.R;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;
import org.latefire.deals.databinding.ActivityDealDetailsBinding;

public class DealDetailsActivity extends BaseActivity {

  public static final String ARG_DEAL = "deal";
  private ActivityDealDetailsBinding b;
  private Deal mDeal;

  public static void start(Context context, Deal deal) {
    Intent intent = new Intent(context, DealDetailsActivity.class);
    intent.putExtra(ARG_DEAL, deal);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_deal_details);
    supportPostponeEnterTransition();
    setSupportActionBar(b.toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mDeal = (Deal) getIntent().getSerializableExtra(ARG_DEAL);
    b.setDeal(mDeal);

    b.btnGetDeal.setOnClickListener(v ->
        CurrentUserManager.getInstance().getCurrentUser(user ->
            DatabaseManager.getInstance().acquireDeal(user.getId(), mDeal.getId())));
  }
}
