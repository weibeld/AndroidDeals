package org.latefire.deals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import org.latefire.deals.R;
import org.latefire.deals.StringUtils;
import org.latefire.deals.database.Deal;

/**
 * Created by dw on 20/03/17.
 */

public class DealFirebaseAdapter extends FirebaseRecyclerAdapter<Deal, DealFirebaseAdapter.ViewHolder> {
  private Context context;

  public DealFirebaseAdapter(Context context, Query query) {
    super(Deal.class, R.layout.deal_list_item, ViewHolder.class, query);
    this.context = context;
  }

  @Override protected void populateViewHolder(DealFirebaseAdapter.ViewHolder viewHolder, Deal deal, int position) {
    // Title
    viewHolder.tvDealTitle.setText(deal.getTitle());
    // Photo
    Glide.with(context)
        .load(deal.getPhoto())
        .asBitmap()
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.image_not_found)
        .centerCrop()
        .into(viewHolder.imgDealPhoto);
    // Deal price and regular price
    String regularPrice = String.valueOf(deal.getRegularPrice());
    String dealPrice = String.valueOf(deal.getDealPrice());
    SpannableStringBuilder price = StringUtils.makePriceText(context, regularPrice, dealPrice);
    viewHolder.tvDealPrice.setText(price, TextView.BufferType.EDITABLE);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvDealTitle;
    ImageView imgDealPhoto;
    TextView tvDealDate;
    TextView tvDealLocation;
    TextView tvDealPrice;

    public ViewHolder(View itemView) {
      super(itemView);
      tvDealTitle = (TextView) itemView.findViewById(R.id.tv_deal_title);
      imgDealPhoto = (ImageView) itemView.findViewById(R.id.img_deal_photo);
      tvDealDate = (TextView) itemView.findViewById(R.id.tv_deal_date);
      tvDealLocation = (TextView) itemView.findViewById(R.id.tv_deal_location);
      tvDealPrice = (TextView) itemView.findViewById(R.id.tv_deal_price);
    }
  }
}
