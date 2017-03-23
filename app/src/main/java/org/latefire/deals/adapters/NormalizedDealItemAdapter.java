package org.latefire.deals.adapters;

import android.content.Context;
import android.util.Pair;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.latefire.deals.R;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;

/**
 * FirebaseRecyclerAdapter, creating deal list items, listening to a set of Deal IDs (key = Deal ID,
 * value = boolean) that are saved in a denormalised fashion within another object (e.g. Business).
 */

public class NormalizedDealItemAdapter extends FirebaseRecyclerAdapter<Boolean, DealItemViewHolder>  {
  private Context mContext;
  private DatabaseManager mDatabaseManager;

  public NormalizedDealItemAdapter(Context context, Query query) {
    super(Boolean.class, R.layout.deal_list_item, DealItemViewHolder.class, query);
    mContext = context;
    mDatabaseManager = DatabaseManager.getInstance();
  }

  @Override protected void populateViewHolder(DealItemViewHolder viewHolder, Boolean isSelected, int position) {
    // The adapter listens to the set of Deal ID booleans in the related object (e.g. Business),
    // and not on the deals themselves, so we need to add a listener on the deal of this item to
    // get the deal object and detect changes to it.
    String dealId = getRef(position).getKey();
    DatabaseReference dealRef = mDatabaseManager.getDealRef(dealId);
    ValueEventListener dealChangeListener = new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        Deal deal = dataSnapshot.getValue(Deal.class);
        DealItemAdapter.setViewHolderFields(viewHolder, deal, mContext);
      }
      @Override public void onCancelled(DatabaseError databaseError) {}
    };
    dealRef.addValueEventListener(dealChangeListener);
    // Attach deal DB ref and listener to the ViewHolder, so that the listener can be removed later
    viewHolder.setTag(new Pair<>(dealRef, dealChangeListener));
  }

  // Called when the item view is about to be recycled
  @Override public void onViewRecycled(DealItemViewHolder holder) {
    super.onViewRecycled(holder);
    // Remove the listener that has been added to the deal's database location in populateViewHolder
    Pair<DatabaseReference, ValueEventListener> tag = (Pair<DatabaseReference, ValueEventListener>) holder.getTag();
    tag.first.removeEventListener(tag.second);
  }
}
