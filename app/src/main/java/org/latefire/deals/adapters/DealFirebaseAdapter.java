package org.latefire.deals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import org.latefire.deals.R;
import org.latefire.deals.StringUtils;
import org.latefire.deals.managers.DatabaseManager;
import org.latefire.deals.models.Deal;

/**
 * Created by dw on 20/03/17.
 */

public class DealFirebaseAdapter
    extends FirebaseRecyclerAdapter<Deal, DealFirebaseAdapter.ViewHolder> {
  private Context context;

  public DealFirebaseAdapter(Context context) {
    super(Deal.class, R.layout.deal_list_item, ViewHolder.class,
        DatabaseManager.getInstance().getDealsReference());
    this.context = context;
  }

  @Override protected void populateViewHolder(DealFirebaseAdapter.ViewHolder viewHolder, Deal deal,
      int position) {
    if (deal.getTitle() != null) {
      viewHolder.tvDealTitle.setText(deal.getTitle());
      Glide.with(context).load(R.drawable.dummy).centerCrop().into(viewHolder.imgDealPhoto);
      viewHolder.tvDealPrice.setText(
          StringUtils.makePriceText(context, String.valueOf(deal.getRegularPrice()),
              String.valueOf(deal.getDealPrice())), TextView.BufferType.EDITABLE);
    }
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
