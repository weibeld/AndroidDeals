package org.latefire.deals.business.home;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import org.latefire.deals.R;
import org.latefire.deals.customer.home.AbsItemViewHolder;
import org.latefire.deals.customer.home.DealDetailsActivity;
import org.latefire.deals.database.Deal;
import org.latefire.deals.utils.StringUtils;

/**
 * ViewHolder for a deal item in a list of deals, as shown on the HomeActivityCustomer
 */
public class DealItemViewHolder extends AbsItemViewHolder<Deal> {
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

  // To be called by populateViewHolder of an adapter using this ViewHolder
  public void setViewHolderFields(Deal deal, Context context) {
    // Title
    tvDealTitle.setText(deal.getTitle());
    // Photo
    Glide.with(context)
        .load(deal.getPhoto())
        .asBitmap()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.image_not_found)
        .centerCrop()
        .into(imgDealPhoto);
    // Deal price and regular price
    String regularPrice = String.valueOf(deal.getRegularPrice());
    String dealPrice = String.valueOf(deal.getDealPrice());
    SpannableStringBuilder price = StringUtils.makePriceText(context, regularPrice, dealPrice);
    tvDealPrice.setText(price, TextView.BufferType.EDITABLE);
    tvDealDate.setText(deal.getBeginValidity() + " - " + deal.getEndValidity());
    tvDealLocation.setText(deal.getLocationName());
    itemView.setOnClickListener(v -> {
      DealDetailsActivity.start(context, deal);
    });
  }

  public Object getTag() {
    return tag;
  }

  public void setTag(Object tag) {
    this.tag = tag;
  }
}
