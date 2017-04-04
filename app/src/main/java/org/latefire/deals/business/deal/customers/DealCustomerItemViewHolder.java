package org.latefire.deals.business.deal.customers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.latefire.deals.R;

/**
 * ViewHolder for a deal item in a list of deals, as shown on the HomeActivityCustomer
 */
class DealCustomerItemViewHolder extends RecyclerView.ViewHolder {
  ImageView ivProfileImage;
  TextView tvName;
  TextView tvEmail;
  TextView tvAcquisitionDate;
  TextView tvRedemptionDate;

  public DealCustomerItemViewHolder(View itemView) {
    super(itemView);
    tvName = (TextView) itemView.findViewById(R.id.tvName);
    tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
    tvAcquisitionDate = (TextView) itemView.findViewById(R.id.tvAcquisitionDate);
    tvRedemptionDate = (TextView) itemView.findViewById(R.id.tvRedemptionDate);
    ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
  }
}
