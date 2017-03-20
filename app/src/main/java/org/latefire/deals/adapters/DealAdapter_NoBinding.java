package org.latefire.deals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import org.latefire.deals.R;
import org.latefire.deals.models.Deal;

/**
 * Created by phongnguyen on 3/20/17.
 */
public class DealAdapter_NoBinding extends RecyclerView.Adapter<DealAdapter_NoBinding.ViewHolder> {

  private static final String TAG = DealAdapter_NoBinding.class.getSimpleName();

  private Context mContext;
  private List<Deal> mData;

  /**
   * Change {@link List} type according to your needs
   */
  public DealAdapter_NoBinding(Context context, List<Deal> data) {
    if (context == null) {
      throw new NullPointerException("context can not be NULL");
    }

    if (data == null) {
      throw new NullPointerException("data list can not be NULL");
    }

    this.mContext = context;
    this.mData = data;
  }

  /**
   * Change the null parameter to a layout resource {@code R.layout.example}
   */
  @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_list_item, parent, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    holder.tvTitle.setText(mData.get(position).getTitle());
  }

  @Override public int getItemCount() {
    return mData.size();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_deal_title) TextView tvTitle;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this,itemView);
    }
  }
}