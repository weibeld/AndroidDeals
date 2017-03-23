package org.latefire.deals.adapters;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import org.latefire.deals.R;
import org.latefire.deals.StringUtils;
import org.latefire.deals.database.Deal;

/**
 * FirebaseRecyclerAdapter listening to a set of Deals and creating deal list items.
 */

public class DealItemAdapter extends FirebaseRecyclerAdapter<Deal, DealItemViewHolder> {
  private Context context;

  public DealItemAdapter(Context context, Query query) {
    super(Deal.class, R.layout.deal_list_item, DealItemViewHolder.class, query);
    this.context = context;
  }

  @Override protected void populateViewHolder(DealItemViewHolder viewHolder, Deal deal, int position) {
    setViewHolderFields(viewHolder, deal, context);
  }

  static void setViewHolderFields(DealItemViewHolder viewHolder, Deal deal, Context c) {
    // Title
    viewHolder.tvDealTitle.setText(deal.getTitle());
    // Photo
    Glide.with(c)
        .load(deal.getPhoto())
        .asBitmap()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.image_not_found)
        .centerCrop()
        .into(viewHolder.imgDealPhoto);
    // Deal price and regular price
    String regularPrice = String.valueOf(deal.getRegularPrice());
    String dealPrice = String.valueOf(deal.getDealPrice());
    SpannableStringBuilder price = StringUtils.makePriceText(c, regularPrice, dealPrice);
    viewHolder.tvDealPrice.setText(price, TextView.BufferType.EDITABLE);
  }
}
