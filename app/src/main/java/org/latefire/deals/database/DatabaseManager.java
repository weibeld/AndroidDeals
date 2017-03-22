package org.latefire.deals.database;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dw on 19/03/17.
 */

public class DatabaseManager {

  private static final String LOG_TAG = DatabaseManager.class.getSimpleName();
  private static DatabaseManager instance;

  // Key names for 1-n and n-n relationships saved in a denormalised (redundant) way
  private final String BUSINESS_DEALS = "deals";
  private final String CUSTOMER_ACQUISITIONS = "acquisitions";
  private final String DEAL_ACQUISITIONS = "acquisitions";

  // Shorthand database references
  private DatabaseReference mCustomersRef;
  private DatabaseReference mBusinessesRef;
  private DatabaseReference mDealsRef;
  private DatabaseReference mAcquisitionsRef;


  private DatabaseManager() {
    DatabaseReference rootDbRef = FirebaseDatabase.getInstance().getReference();
    mCustomersRef = rootDbRef.child("customers");
    mBusinessesRef = rootDbRef.child("businesses");
    mDealsRef = rootDbRef.child("deals");
    mAcquisitionsRef = rootDbRef.child("acquisitions");
  }

  public static synchronized DatabaseManager getInstance() {
    if (instance == null) instance = new DatabaseManager();
    return instance;
  }


  /*----------------------------------------------------------------------------------------------*
   * Create single object
   *----------------------------------------------------------------------------------------------*/

  public String createCustomer(Customer customer) {
    return pushAndReturnId(mCustomersRef, customer);
  }

  public String createBusiness(Business business) {
    return pushAndReturnId(mBusinessesRef, business);
  }

  // Save deal in database and add its ID to the list of deals of the corresponding business
  public String createDeal(Deal deal) {
    String dealId = pushAndReturnId(mDealsRef, deal);
    mBusinessesRef.child(deal.getBusinessId()).child(BUSINESS_DEALS).child(dealId).setValue(true);
    return dealId;
  }

  // Save an acquisition (of a deal by a customer) to the DB and add its ID to both the acquisitions
  // list of the corresponding user, and of the corresponding deal
  public String createAcquisition(Acquisition acq) {
    String acqId = pushAndReturnId(mAcquisitionsRef, acq);
    mCustomersRef.child(acq.getCustomerId()).child(CUSTOMER_ACQUISITIONS).child(acqId).setValue(true);
    mDealsRef.child(acq.getDealId()).child(DEAL_ACQUISITIONS).child(acqId).setValue(true);
    return acqId;
  }

  private String pushAndReturnId(DatabaseReference ref, AbsModel model) {
    DatabaseReference child = ref.push();
    String id = child.getKey();
    model.setId(id);
    child.setValue(model);
    return id;
  }


  /*----------------------------------------------------------------------------------------------*
   * Get single object
   *----------------------------------------------------------------------------------------------*/

  public void getDeal(String id, SingleQueryCallback callback) {
    queryById(mDealsRef, id, callback, Deal.class);
  }

  public void getCustomer(String id, SingleQueryCallback callback) {
    queryById(mCustomersRef, id, callback, Customer.class);
  }

  public void getBusiness(String id, SingleQueryCallback callback) {
    queryById(mBusinessesRef, id, callback, Business.class);
  }

  public void getAcquisition(String id, SingleQueryCallback callback) {
    queryById(mAcquisitionsRef, id, callback, Acquisition.class);
  }

  // Retrieve an object by its ID (key) and pass it to the SingleQueryCallback's method. If there
  // is no object with the specified ID, null is passed to the callback method.
  private void queryById(DatabaseReference parentRef, String id, SingleQueryCallback callback, Class<? extends AbsModel> cls) {
    DatabaseReference itemRef = parentRef.child(id);
    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        AbsModel result = dataSnapshot.getValue(cls);
        callback.yourResult(result);
      }
      @Override public void onCancelled(DatabaseError databaseError) {}
    });
  }


  /*----------------------------------------------------------------------------------------------*
   * Get lists of object
   *----------------------------------------------------------------------------------------------*/

  // TODO: 22/03/17 Index data for ordering: https://firebase.google.com/docs/database/security/indexing-data 
  
  
  // Return all data as a list in a single call to the passed callback
  public void getDealsOrderByChild1(String child, ListQueryCallback callback) {
    ArrayList<Deal> deals = new ArrayList<>();
    // If the child does not exist, then no ordering is applied
    Query query = mDealsRef.orderByChild(child);
    // Load initial data one object at a time (in specified sort order)
    MyChildAddedListener childAddedListener = new MyChildAddedListener() {
      @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        deals.add(dataSnapshot.getValue(Deal.class));
      }
    };
    query.addChildEventListener(childAddedListener);
    // Take the value event as an indicator that the loading of the initial data by the child event
    // listener is finished (value events are guaranteed to be triggered after child events [1]).
    // This approach of isolating the initial data load is described here [2].
    // [1] https://firebase.google.com/docs/database/admin/retrieve-data#section-event-guarantees
    // [2] http://stackoverflow.com/questions/27978078/how-to-separate-initial-data-load-from-incremental-children-with-firebase
    query.addListenerForSingleValueEvent(new MyDataChangedListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        query.removeEventListener(childAddedListener);
        callback.yourResult(deals);
      }
    });
  }

  // Return each data object in a separate call to the passed callback, and notify the caller when
  // all data has been loaded by calling the finished() method of the callback.
  public void getDealsOrderByChild2(String child, MultiSingleQueryCallback callback) {
    Query query = mDealsRef.orderByChild(child);
    MyChildAddedListener childAddedListener = new MyChildAddedListener() {
      @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        callback.yourResult(dataSnapshot.getValue(Deal.class));
      }
    };
    query.addChildEventListener(childAddedListener);
    query.addListenerForSingleValueEvent(new MyDataChangedListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        query.removeEventListener(childAddedListener);
        callback.finished();
      }
    });
  }

  // Note: the order methods can be applied in conjunction with value events, so the approach to
  // separate initial data from incremental data in getDealsOrderByChild 1 + 2 is not necessary
  // to make queries on the initial data (the separating approach is only necessary for discarding
  // the initial data and handle only the incremental data in the child event listener)
  public void getDealsOrderByChild3(String child, ListQueryCallback callback) {
    ArrayList<Deal> deals = new ArrayList<>();
    // If the child does not exist, then no ordering is applied
    Query query = mDealsRef.orderByChild(child);

    query.addListenerForSingleValueEvent(new MyDataChangedListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot dealSnapshot : dataSnapshot.getChildren()) {
          deals.add(dealSnapshot.getValue(Deal.class));
        }
        callback.yourResult(deals);
      }
    });
  }

  // Note: returns the insertion order if using auto-generated keys (created by push())
  public void getDealsOrderByKey(ListQueryCallback callback) {
    ArrayList<Deal> deals = new ArrayList<>();
    Query query = mDealsRef.orderByKey();
    query.addListenerForSingleValueEvent(new MyDataChangedListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot dealSnapshot : dataSnapshot.getChildren()) {
          deals.add(dealSnapshot.getValue(Deal.class));
        }
        callback.yourResult(deals);
      }
    });
  }

  // Note: this ordering makes only sense if the values of the children are single values
  // (e.g. numbers, strings). If the values are objects, then the result is sorted
  // lexicographically by key in ascending order [1].
  // [1] https://firebase.google.com/docs/database/admin/retrieve-data#orderbyvalue
  public void getDealsOrderByValue(ListQueryCallback callback) {
    ArrayList<Deal> deals = new ArrayList<>();
    Query query = mDealsRef.orderByValue();
    query.addListenerForSingleValueEvent(new MyDataChangedListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot dealSnapshot : dataSnapshot.getChildren()) {
          deals.add(dealSnapshot.getValue(Deal.class));
        }
        callback.yourResult(deals);
      }
    });
  }


  //public ArrayList<Deal> getDealsOfCustomer(String customerId) {
  //  return null;
  //}
  //
  //public ArrayList<AbsUser> getCustomersOfDeal(String dealId) {
  //  return null;
  //}
  //
  //public ArrayList<Deal> getDealsOfBusiness(String businessId) {
  //  return null;
  //}

  public Query getDealsQuery() {
    return mDealsRef.limitToFirst(5).startAt(10);
  }

  /*----------------------------------------------------------------------------------------------*
   * Callback interfaces
   *----------------------------------------------------------------------------------------------*/

  public interface ListQueryCallback {
    void yourResult(List<? extends AbsModel> models);
  }

  public interface SingleQueryCallback {
    void yourResult(AbsModel model);
  }

  public interface MultiSingleQueryCallback extends SingleQueryCallback {
    void finished();
  }


  public abstract class MyChildAddedListener implements ChildEventListener {
    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
    @Override public void onCancelled(DatabaseError databaseError) {}
  }
  public abstract class MyDataChangedListener implements ValueEventListener {
    @Override public void onCancelled(DatabaseError databaseError) {}
  }
}
