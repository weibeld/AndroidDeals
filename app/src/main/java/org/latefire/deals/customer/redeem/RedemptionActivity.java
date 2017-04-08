package org.latefire.deals.customer.redeem;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import org.latefire.deals.R;
import org.latefire.deals.auth.CurrentUserManager;
import org.latefire.deals.customer.deals.CustomerDealsActivity;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;
import org.latefire.deals.databinding.ActivityRedemptionBinding;
import org.latefire.deals.utils.MiscUtils;

import static org.latefire.deals.customer.home.DealDetailsActivity.ARG_DEAL;

public class RedemptionActivity extends AppCompatActivity {

  private ActivityRedemptionBinding b;
  private RedemptionActivity mActivity;
  private Deal mDeal;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    b = DataBindingUtil.setContentView(this, R.layout.activity_redemption);

    mActivity = this;
    mDeal = (Deal) getIntent().getSerializableExtra(ARG_DEAL);
    b.setDeal(mDeal);

    b.btnRedeemDeal.setOnClickListener(v ->
        CurrentUserManager.getInstance().getCurrentUser(user ->
            redeemDeal(user.getId(), mDeal.getId())));
  }

  private void redeemDeal(String userId, String dealId) {
    DatabaseManager.getInstance().redeemDeal(userId, dealId);
    MiscUtils.toastS(mActivity, "Deal redeemed");
    startActivity(new Intent(mActivity, CustomerDealsActivity.class));
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
