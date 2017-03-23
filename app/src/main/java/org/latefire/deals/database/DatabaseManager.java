package org.latefire.deals.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by dw on 19/03/17.
 */

public class DatabaseManager {

  private static final String LOG_TAG = DatabaseManager.class.getSimpleName();
  private static DatabaseManager instance;

  // Key names for the denormalised (redundant) data
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
   * Queries to be used with FirebaseRecyclerAdapter
   *----------------------------------------------------------------------------------------------*/

  // TODO: 22/03/17 Index data for ordering: https://firebase.google.com/docs/database/security/indexing-data
  public Query getDealsOrderByDealPrice() {
    return mDealsRef.orderByChild("dealPrice").limitToFirst(5);
  }

  public Query getDealsOrderByRegularPrice() {
    return mDealsRef.orderByChild("regularPrice").limitToFirst(5);
  }

  public Query getDealsOrderByTitle() {
    return mDealsRef.orderByChild("title").limitToFirst(5);
  }

  public Query getDealIdsOfBusiness(String businessId) {
    return mBusinessesRef.child(businessId).child(BUSINESS_DEALS);
  }

  public Query getAcquisitionIdsOfCustomer(String customerId) {
    return mCustomersRef.child(customerId).child(CUSTOMER_ACQUISITIONS);
  }

  public Query getAcquisitionIdsOfDeal(String dealId) {
    return mCustomersRef.child(dealId).child(DEAL_ACQUISITIONS);
  }

  // If not using the denormalised data
  //public Query getAcquisitionsOfDeal(String dealId) {
  //  return mAcquisitionsRef.orderByChild("dealId").equalTo(dealId);
  //}
  //
  //public Query getAcquisitionsOfCustomer(String customerId) {
  //  return mAcquisitionsRef.orderByChild("customerId").equalTo(customerId);
  //}
  //
  //public Query getDealsOfBusiness(String businessId) {
  //  return mDealsRef.orderByChild("businessId").equalTo(businessId);
  //}


  /*----------------------------------------------------------------------------------------------*
   * Get DatabaseReferences to specific locations in the database
   *----------------------------------------------------------------------------------------------*/
  public DatabaseReference getDealRef(String dealId) {
    return mDealsRef.child(dealId);
  }

  public DatabaseReference getAcquisitionRef(String acqId) {
    return mAcquisitionsRef.child(acqId);
  }



  /*----------------------------------------------------------------------------------------------*
   * Callback interfaces
   *----------------------------------------------------------------------------------------------*/

  public interface SingleQueryCallback {
    void yourResult(AbsModel model);
  }
}
