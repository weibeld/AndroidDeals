package org.latefire.deals.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.latefire.deals.R;

/**
 * Created by dw on 23/03/17.
 */
public class DealItemViewHolder extends RecyclerView.ViewHolder {
  TextView tvDealTitle;
  ImageView imgDealPhoto;
  TextView tvDealDate;
  TextView tvDealLocation;
  TextView tvDealPrice;

  // General-purpose tag that can be set to every ViewHolder
  private Object tag;

  public DealItemViewHolder(View itemView) {
    super(itemView);
    tvDealTitle = (TextView) itemView.findViewById(R.id.tv_deal_title);
    imgDealPhoto = (ImageView) itemView.findViewById(R.id.img_deal_photo);
    tvDealDate = (TextView) itemView.findViewById(R.id.tv_deal_date);
    tvDealLocation = (TextView) itemView.findViewById(R.id.tv_deal_location);
    tvDealPrice = (TextView) itemView.findViewById(R.id.tv_deal_price);
  }

  public Object getTag() {
    return tag;
  }

  public void setTag(Object tag) {
    this.tag = tag;
  }
}
