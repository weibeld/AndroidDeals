package org.latefire.deals.customer.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.latefire.deals.database.AbsModel;

/**
 * Base type of the ViewHolder required by DenormFirebaseRecyclerAdapter.
 */
public abstract class AbsItemViewHolder<T extends AbsModel> extends RecyclerView.ViewHolder {

  // General-purpose tag
  private Object tag;

  public AbsItemViewHolder(View itemView) {
    super(itemView);
  }

  // Called by populateViewHolder of the corresponding RecyclerView.Adapter
  public abstract void setViewHolderFields(T model, Context context);

  public Object getTag() {
    return tag;
  }

  public void setTag(Object tag) {
    this.tag = tag;
  }
}
