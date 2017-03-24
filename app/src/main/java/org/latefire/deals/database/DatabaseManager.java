package org.latefire.deals.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.GregorianCalendar;

/**
 * Created by dw on 19/03/17.
 */

public class DatabaseManager {

  private static final String LOG_TAG = DatabaseManager.class.getSimpleName();
  private static DatabaseManager instance;

  // Key names for denormalised (redundant) data
  private final String BUSINESS_DEALS = "deals";
  private final String CUSTOMER_DEALS = "deals";
  private final String DEAL_CUSTOMERS = "customers";
  private final String ACQUISITION_DATE = "acquisitionDate";
  private final String REDEMPTION_DATE = "redemptionDate";
  private final String VALIDITY_BEGIN_DATE = "validityBeginDate";
  private final String VALIDITY_END_DATE = "validityEndDate";

  // Shorthand database references
  private DatabaseReference mCustomersRef;
  private DatabaseReference mBusinessesRef;
  private DatabaseReference mDealsRef;


  private DatabaseManager() {
    DatabaseReference rootDbRef = FirebaseDatabase.getInstance().getReference();
    mCustomersRef = rootDbRef.child("customers");
    mBusinessesRef = rootDbRef.child("businesses");
    mDealsRef = rootDbRef.child("deals");
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
    getBusinessDealRef(deal.getBusinessId(), dealId).setValue(true);
    return dealId;
  }

  public void acquireDeal(String customerId, String dealId) {
    long timestamp = getCurrentTimestamp();

    // Add  the deal to the customer object
    DatabaseReference ref = getCustomerDealRef(customerId, dealId);
    // Acquisition date
    ref.child(ACQUISITION_DATE).setValue(timestamp);
    // Get begin and end validity of deal and add it to the customer object
    getDealRef(dealId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        Deal deal = dataSnapshot.getValue(Deal.class);
        ref.child(VALIDITY_BEGIN_DATE).setValue(deal.getBeginValidity());
        ref.child(VALIDITY_END_DATE).setValue(deal.getEndValidity());
      }
      @Override public void onCancelled(DatabaseError databaseError) {}
    });

    // Add the customer to the deal object
    getDealCustomerRef(dealId, customerId).child(ACQUISITION_DATE).setValue(timestamp);
  }

  public void redeemDeal(String customerId, String dealId) {
    long timestamp = getCurrentTimestamp();
    getCustomerDealRef(customerId, dealId).child(REDEMPTION_DATE).setValue(timestamp);
    getDealCustomerRef(dealId, customerId).child(REDEMPTION_DATE).setValue(timestamp);
  }

  private DatabaseReference getCustomerDealRef(String customerId, String dealId) {
    return mCustomersRef.child(customerId).child(CUSTOMER_DEALS).child(dealId);
  }

  private DatabaseReference getDealCustomerRef(String dealId, String customerId) {
    return mDealsRef.child(dealId).child(DEAL_CUSTOMERS).child(customerId);
  }

  private DatabaseReference getBusinessDealRef(String businessId, String dealId) {
    return mBusinessesRef.child(businessId).child(BUSINESS_DEALS).child(dealId);
  }

  private long getCurrentTimestamp() {
    return GregorianCalendar.getInstance().getTimeInMillis();
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

  //public void getAcquisition(String id, SingleQueryCallback callback) {
  //  queryById(mAcquisitionsRef, id, callback, Acquisition.class);
  //}

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

  /*----------------------------------------------------------------------------------------------*
   * Get DatabaseReferences to specific locations in the database
   *----------------------------------------------------------------------------------------------*/
  public DatabaseReference getDealRef(String dealId) {
    return mDealsRef.child(dealId);
  }

  public DatabaseReference getCustomerRef(String customerId) {
    return mDealsRef.child(customerId);
  }

  public DatabaseReference getBusinessRef(String businessId) {
    return mDealsRef.child(businessId);
  }

  //public DatabaseReference getAcquisitionRef(String acqId) {
  //  return mAcquisitionsRef.child(acqId);
  //}

  public DatabaseReference getDealsRef() {
    return mDealsRef;
  }

  public DatabaseReference getCustomersRef() {
    return mCustomersRef;
  }

  public DatabaseReference getBusinessesRef() {
    return mBusinessesRef;
  }


  //public DatabaseReference getModelParentRef(Class<AbsModel> modelClass) {
  //  return getParentRef(modelClass);
  //}
  //
  //public DatabaseReference getModelRef(Class<? extends AbsModel> modelClass, String id) {
  //  return getParentRef(modelClass).child(id);
  //}
  //
  //private DatabaseReference getParentRef(Class<? extends AbsModel> modelClass) {
  //  if (modelClass.isAssignableFrom(Deal.class))
  //    return mDealsRef;
  //  else if (modelClass.isAssignableFrom(Customer.class))
  //    return mCustomersRef;
  //  else if (modelClass.isAssignableFrom(Business.class))
  //    return mBusinessesRef;
  //  else if (modelClass.isAssignableFrom(Acquisition.class))
  //    return mAcquisitionsRef;
  //  else
  //    throw new IllegalArgumentException(modelClass.getSimpleName() + " is not a valid database model class");
  //}



  /*----------------------------------------------------------------------------------------------*
   * Callback interfaces
   *----------------------------------------------------------------------------------------------*/

  public interface SingleQueryCallback {
    void yourResult(AbsModel model);
  }
}
