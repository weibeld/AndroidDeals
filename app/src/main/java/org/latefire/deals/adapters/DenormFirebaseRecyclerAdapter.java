package org.latefire.deals.adapters;

import android.content.Context;
import android.util.Pair;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.latefire.deals.database.AbsModel;
import org.latefire.deals.database.DatabaseManager;

/**
 * FirebaseRecyclerAdapter for creating AbsModel items from a list of AbsModel IDs (key = ID,
 * value = boolean). These IDs are denormalised relations of 1-n and n-n relationships, e.g. a
 * list of deal IDs saved in a business object. This adapter listens on these IDs and creates
 * list item views from the corresponding real AbsModel objects.
 */
public class DenormFirebaseRecyclerAdapter<T extends AbsModel, VH extends AbsItemViewHolder<T>>
    extends FirebaseRecyclerAdapter<Boolean, VH> {

  private Context mContext;
  private DatabaseManager mDatabaseManager;
  private Class<T> mModelClass;
  private DatabaseReference mModelParentRef;

  public DenormFirebaseRecyclerAdapter(Query idsRef, DatabaseReference modelParentRef,
      Class<T> modelClass, int modelLayout, Class<VH> viewHolderClass, Context context) {
    super(Boolean.class, modelLayout, viewHolderClass, idsRef);
    mContext = context;
    mDatabaseManager = DatabaseManager.getInstance();
    mModelClass = modelClass;
    mModelParentRef = modelParentRef;
  }

  @Override protected void populateViewHolder(VH viewHolder, Boolean itemIdBool, int position) {
    String itemId = getRef(position).getKey();
    DatabaseReference itemRef = mModelParentRef.child(itemId);
    ValueEventListener listener = new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        T item = dataSnapshot.getValue(mModelClass);
        viewHolder.setViewHolderFields(item, mContext);
      }

      @Override public void onCancelled(DatabaseError databaseError) {
      }
    };
    itemRef.addValueEventListener(listener);
    // Attach deal DB ref and listener to the ViewHolder, so that the listener can be removed later
    viewHolder.setTag(new Pair<>(itemRef, listener));
  }

  // Called when the item view is about to be recycled
  @Override public void onViewRecycled(VH holder) {
    super.onViewRecycled(holder);
    // Remove the listener that has been added to the deal's database location in populateViewHolder
    Pair<DatabaseReference, ValueEventListener> tag =
        (Pair<DatabaseReference, ValueEventListener>) holder.getTag();
    tag.first.removeEventListener(tag.second);
  }
}
