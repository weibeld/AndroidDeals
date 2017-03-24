package org.latefire.deals.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.latefire.deals.database.AbsModel;

/**
 * Created by dw on 24/03/17.
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
