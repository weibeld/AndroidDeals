package org.latefire.deals.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
   * Get single object
   *----------------------------------------------------------------------------------------------*/

  public void getDeal(String id, QueryCallbackSingle callback) {
    queryById(mDealsRef, id, callback, Deal.class);
  }

  public void getCustomer(String id, QueryCallbackSingle callback) {
    queryById(mCustomersRef, id, callback, Customer.class);
  }

  public void getBusiness(String id, QueryCallbackSingle callback) {
    queryById(mBusinessesRef, id, callback, Business.class);
  }

  public void getAcquisition(String id, QueryCallbackSingle callback) {
    queryById(mAcquisitionsRef, id, callback, Acquisition.class);
  }

  // Retrieve an object by its ID (key) and pass it to the QueryCallbackSingle's method. If there
  // is no object with the specified ID, null is passed to the callback method.
  private void queryById(DatabaseReference parentRef, String id, QueryCallbackSingle callback, Class<? extends AbsModel> cls) {
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

  public DatabaseReference getDealsReference() {
    return mDealsRef;
  }

  /*----------------------------------------------------------------------------------------------*
   * Callback interfaces
   *----------------------------------------------------------------------------------------------*/

  public interface QueryCallbackList {
    void yourResult(List<AbsModel> modelList);
  }

  public interface QueryCallbackSingle {
    void yourResult(AbsModel model);
  }
}
