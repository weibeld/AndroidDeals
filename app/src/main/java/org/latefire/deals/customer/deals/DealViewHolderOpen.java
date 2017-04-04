package org.latefire.deals.customer.deals;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.latefire.deals.R;

/**
 * Created by dw on 02/04/17.
 */

public class DealViewHolderOpen extends RecyclerView.ViewHolder {

  TextView tvDealTitle;
  ImageView imgDealPhoto;
  TextView tvDealDate;
  TextView tvDealLocation;
  TextView tvDealPrice;

  public DealViewHolderOpen(View itemView) {
    super(itemView);
    tvDealTitle = (TextView) itemView.findViewById(R.id.tv_deal_title);
    imgDealPhoto = (ImageView) itemView.findViewById(R.id.img_deal_photo);
    tvDealDate = (TextView) itemView.findViewById(R.id.tv_deal_date);
    tvDealLocation = (TextView) itemView.findViewById(R.id.tv_deal_location);
    tvDealPrice = (TextView) itemView.findViewById(R.id.tv_deal_price);
  }

}
