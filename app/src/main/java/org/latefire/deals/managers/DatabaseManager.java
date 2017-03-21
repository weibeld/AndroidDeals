package org.latefire.deals.managers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;
import org.latefire.deals.models.Business;
import org.latefire.deals.models.Customer;
import org.latefire.deals.models.Deal;
import org.latefire.deals.models.FirebaseModel;

/**
 * Created by dw on 19/03/17.
 */

public class DatabaseManager {

  private static final String LOG_TAG = DatabaseManager.class.getSimpleName();
  private static DatabaseManager instance;

  private DatabaseReference mCustomersRef;
  private DatabaseReference mBusinessesRef;
  private DatabaseReference mDealsRef;
  //private DatabaseReference mDbRefCustomerDealRelations;

  private DatabaseManager() {
    DatabaseReference rootDbRef = FirebaseDatabase.getInstance().getReference();
    mCustomersRef = rootDbRef.child("customers");
    mBusinessesRef = rootDbRef.child("businesses");
    mDealsRef = rootDbRef.child("deals");
    //mDbRefCustomerDealRelations = rootDbRef.child("customerDealRelations");
  }

  public static synchronized DatabaseManager getInstance() {
    if (instance == null) instance = new DatabaseManager();
    return instance;
  }

  // TODO: 21/03/17 handle the case that no object with the passed ID exists
  public void getDeal(String id, QueryCallbackSingle callback) {
    queryById(mDealsRef, id, callback, Deal.class);
  }

  public void getCustomer(String id, QueryCallbackSingle callback) {
    queryById(mCustomersRef, id, callback, Customer.class);
  }

  public void getBusiness(String id, QueryCallbackSingle callback) {
    queryById(mBusinessesRef, id, callback, Business.class);
  }

  private void queryById(DatabaseReference parentRef, String id, QueryCallbackSingle callback, Class<? extends FirebaseModel> cls) {
    DatabaseReference itemRef = parentRef.child(id);
    itemRef.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        FirebaseModel result = dataSnapshot.getValue(cls);
        callback.yourResult(result);
      }
      @Override public void onCancelled(DatabaseError databaseError) {}
    });
  }

  public String createDeal(Deal deal) {
    return pushAndReturnId(mDealsRef, deal);
  }

  public String createCustomer(Customer customer) {
    return pushAndReturnId(mCustomersRef, customer);
  }

  public String createBusiness(Business business) {
    return pushAndReturnId(mBusinessesRef, business);
  }

  private String pushAndReturnId(DatabaseReference ref, FirebaseModel model) {
    DatabaseReference child = ref.push();
    String id = child.getKey();
    model.setId(id);
    child.setValue(model);
    return id;
  }


  //public void createCustomerDealRelation(CustomerDealRelation relation) {
  //}
  //
  //public ArrayList<Deal> getDealsOfCustomer(String customerId) {
  //  return null;
  //}
  //
  //public ArrayList<User> getCustomersOfDeal(String dealId) {
  //  return null;
  //}
  //
  //public ArrayList<Deal> getDealsOfBusiness(String businessId) {
  //  return null;
  //}

  public DatabaseReference getDealsReference() {
    return mDealsRef;
  }


  public interface QueryCallbackList {
    void yourResult(List<FirebaseModel> modelList);
  }

  public interface QueryCallbackSingle {
    void yourResult(FirebaseModel model);
  }
}
