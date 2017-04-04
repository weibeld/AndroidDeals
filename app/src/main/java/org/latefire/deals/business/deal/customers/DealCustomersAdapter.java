package org.latefire.deals.business.deal.customers;

import android.app.Activity;
import android.content.Context;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import org.latefire.deals.R;
import org.latefire.deals.database.Customer;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.utils.MiscUtils;

/**
 * Created by dw on 04/04/17.
 */

class DealCustomersAdapter extends FirebaseRecyclerAdapter<DealCustomerInfo, DealCustomerItemViewHolder> {

  Context mContext;

  public DealCustomersAdapter(Context c, String dealId) {
    super(DealCustomerInfo.class, R.layout.item_deal_customer, DealCustomerItemViewHolder.class, DatabaseManager.getInstance().getCustomerIdsOfDeal(dealId));
    mContext = c;
  }

  // Reverse order (most recently grabbed deal on top of list)
  @Override public DealCustomerInfo getItem(int position) {
    return super.getItem(getItemCount()-1-position);
  }

  @Override
  protected void populateViewHolder(DealCustomerItemViewHolder holder, DealCustomerInfo model, int position) {
    String customerId = getRef(position).getKey();
    Long acquisitionDate = model.getAcquisitionDate();
    Long redemptionDate = model.getRedemptionDate();

    DatabaseManager.getInstance().getCustomer(customerId, model1 -> {
      Customer customer = (Customer) model1;

      // Name and email
      holder.tvName.setText(customer.getFirstName() + " " + customer.getLastName());
      holder.tvEmail.setText(customer.getEmail());

      // Grabbing date
      String grabDate = String.format(mContext.getString(R.string.deal_customer_grab_date), acquisitionDate.toString());
      holder.tvAcquisitionDate.setText(grabDate);

      // Redemption date (if already redeemed)
      if (redemptionDate != null) {
        String redeemDate = String.format(mContext.getString(R.string.deal_customer_redeem_date), redemptionDate.toString());
        holder.tvRedemptionDate.setText(redeemDate);
      }

      // Profile image
      Glide.with(mContext)
          .load(customer.getProfilePhoto())
          .asBitmap()
          .placeholder(R.drawable.placeholder)
          .error(R.drawable.image_not_found)
          .centerCrop()
          .into(holder.ivProfileImage);

      // Click listener
      holder.itemView.setOnClickListener(v -> {
        MiscUtils.toastS((Activity) mContext, "TODO: show profile of customer");
      });
    });
  }
}
