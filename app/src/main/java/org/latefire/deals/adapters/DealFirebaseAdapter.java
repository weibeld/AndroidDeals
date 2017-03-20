package org.latefire.deals.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import org.latefire.deals.R;
import org.latefire.deals.managers.DatabaseManager;
import org.latefire.deals.models.Deal;

/**
 * Created by dw on 20/03/17.
 */

public class DealFirebaseAdapter extends FirebaseRecyclerAdapter<Deal, DealFirebaseAdapter.ViewHolder> {

  public DealFirebaseAdapter() {
    super(Deal.class, R.layout.deal_list_item, ViewHolder.class, DatabaseManager.getInstance().getDealsReference());
  }

  @Override
  protected void populateViewHolder(DealFirebaseAdapter.ViewHolder viewHolder, Deal deal, int position) {
    if (deal.getTitle() != null) {
      viewHolder.tvDealTitle.setText(deal.getTitle());
    }
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    TextView tvDealTitle;
    public ViewHolder(View itemView) {
      super(itemView);
      tvDealTitle = (TextView) itemView.findViewById(R.id.tv_deal_title);
    }
  }
}
