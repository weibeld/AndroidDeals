package org.latefire.deals.business.home;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import org.latefire.deals.R;
import org.latefire.deals.business.deal.customers.DealCustomersActivity;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;
import org.latefire.deals.ui.FormatManager;
import org.latefire.deals.utils.StringUtils;

import static org.latefire.deals.customer.home.DealDetailsActivity.ARG_DEAL;

/**
 * Created by dw on 04/04/17.
 */

public class BusinessDealsAdapter extends FirebaseRecyclerAdapter<Deal, DealItemViewHolder> {

  private static final String LOG_TAG = BusinessDealsAdapter.class.getSimpleName();

  Context mContext;
  private FormatManager mFormatManager;

  public BusinessDealsAdapter(Context c, String businessId) {
    super(Deal.class, R.layout.item_deal, DealItemViewHolder.class, DatabaseManager.getInstance().getDealsOfBusiness(businessId));
    mContext = c;
    mFormatManager = FormatManager.getInstance();
  }

  // Reverse order (most recently created deal on top of list)
  @Override public Deal getItem(int position) {
    return super.getItem(getItemCount()-1-position);
  }

  @Override
  protected void populateViewHolder(DealItemViewHolder holder, Deal deal, int position) {
    // Title
    holder.tvDealTitle.setText(deal.getTitle());
    // Photo
    Glide.with(mContext)
        .load(deal.getPhoto())
        .asBitmap()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.image_not_found)
        .centerCrop()
        .into(holder.imgDealPhoto);
    // Deal price and regular price
    String regularPrice = String.valueOf(deal.getRegularPrice());
    String dealPrice = String.valueOf(deal.getDealPrice());
    SpannableStringBuilder price = StringUtils.makePriceText(mContext, regularPrice, dealPrice);
    holder.tvDealPrice.setText(price, TextView.BufferType.EDITABLE);
    String beginValidity = mFormatManager.formatTimestamp(deal.getBeginValidity());
    String endValidity = mFormatManager.formatTimestamp(deal.getEndValidity());
    holder.tvDealDate.setText(beginValidity + " - " + endValidity);
    holder.tvDealLocation.setText(deal.getLocationName());

    // On clicking an item, show customers who grabbed this deal
    holder.itemView.setOnClickListener(v -> {
      Intent intent = new Intent(mContext, DealCustomersActivity.class);
      intent.putExtra(ARG_DEAL, deal);
      mContext.startActivity(intent);
    });
  }
}
