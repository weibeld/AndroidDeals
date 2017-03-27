package org.latefire.deals.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.GregorianCalendar;
import org.latefire.deals.utils.Constant;
import org.latefire.deals.utils.debug.ShowLog;

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

  // Values for user types
  private final String CUSTOMER = "customer";
  private final String BUSINESS = "business";

  // Shorthand database references
  private DatabaseReference mCustomersRef;
  private DatabaseReference mBusinessesRef;
  private DatabaseReference mDealsRef;
  private DatabaseReference mUsersRef;


  private DatabaseManager() {
    DatabaseReference rootDbRef = FirebaseDatabase.getInstance().getReference();
    mCustomersRef = rootDbRef.child("customers");
    mBusinessesRef = rootDbRef.child("businesses");
    mDealsRef = rootDbRef.child("deals");
    mUsersRef = rootDbRef.child("users");
  }

  public static synchronized DatabaseManager getInstance() {
    if (instance == null) instance = new DatabaseManager();
    return instance;
  }


  /*----------------------------------------------------------------------------------------------*
   * Create single object
   *----------------------------------------------------------------------------------------------*/

  public void createCustomer(Customer customer, String userId, SimpleCallback callback) {
    customer.setId(userId);
    mCustomersRef.child(userId).setValue(customer)
        .addOnCompleteListener(task1 -> mUsersRef.child(userId).setValue(CUSTOMER)
            .addOnCompleteListener(task2 -> callback.call()));
  }

  public void createBusiness(Business business, String userId, SimpleCallback callback) {
    business.setId(userId);
    mBusinessesRef.child(userId).setValue(business)
        .addOnCompleteListener(task1 -> mUsersRef.child(userId).setValue(BUSINESS)
            .addOnCompleteListener(task2 -> callback.call()));

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
   * Query user type
   *----------------------------------------------------------------------------------------------*/

  public void getUserType(String userId, IntegerCallback callback) {
    mUsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        String userType = dataSnapshot.getValue(String.class);
        if (userType == null) callback.result(null);
        else if (userType.equals(CUSTOMER)) callback.result(Constant.USER_TYPE_CUSTOMER);
        else if (userType.equals(BUSINESS)) callback.result(Constant.USER_TYPE_BUSINESS);
      }
      @Override public void onCancelled(DatabaseError databaseError) {}
    });
  }

  public void isUserSignedUp(String userId, BooleanCallback callback) {
    mUsersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        callback.result(dataSnapshot.getValue() != null);
      }
      @Override public void onCancelled(DatabaseError databaseError) {}
    });
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

  // Retrieve an object by its ID (key) and pass it to the SingleQueryCallback's method. If there
  // is no object with the specified ID, null is passed to the callback method.
  private void queryById(DatabaseReference parentRef, String id, SingleQueryCallback callback, Class<? extends AbsModel> cls) {
    DatabaseReference itemRef = parentRef.child(id);
    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        AbsModel result = dataSnapshot.getValue(cls);
        callback.yourResult(result);
      }
      @Override public void onCancelled(DatabaseError databaseError) {
        ShowLog.error(databaseError.getMessage());
      }
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
    return mCustomersRef.child(customerId);
  }

  public DatabaseReference getBusinessRef(String businessId) {
    return mBusinessesRef.child(businessId);
  }

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

  public interface IntegerCallback {
    void result(Integer result);
  }

  public interface BooleanCallback {
    void result(boolean result);
  }

  public interface SimpleCallback {
    void call();
  }
}
