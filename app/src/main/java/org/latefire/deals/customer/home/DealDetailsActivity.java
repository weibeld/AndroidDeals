package org.latefire.deals.customer.home;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import org.latefire.deals.R;
import org.latefire.deals.base.BaseActivity;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;
import org.latefire.deals.databinding.ActivityDealDetailsBinding;
import org.latefire.deals.utils.MiscUtils;

public class DealDetailsActivity extends BaseActivity {

  public static final String ARG_DEAL = "deal";
  private ActivityDealDetailsBinding b;
  private DealDetailsActivity mActivity;
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

    mActivity = this;
    mDeal = (Deal) getIntent().getSerializableExtra(ARG_DEAL);
    b.setDeal(mDeal);

    b.btnGetDeal.setOnClickListener(v ->
        CurrentUserManager.getInstance().getCurrentUser(user ->
            acquireDeal(user.getId(), mDeal.getId())));
  }

  private void acquireDeal(String userId, String dealId) {
    DatabaseManager.getInstance().acquireDeal(userId, dealId);
    MiscUtils.toastS(mActivity, "Deal grabbed");
    startActivity(new Intent(mActivity, HomeActivityCustomer.class));
  }
}
