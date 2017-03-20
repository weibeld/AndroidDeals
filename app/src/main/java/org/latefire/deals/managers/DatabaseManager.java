package org.latefire.deals.managers;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.latefire.deals.models.Business;
import org.latefire.deals.models.Customer;
import org.latefire.deals.models.CustomerDealRelation;
import org.latefire.deals.models.Deal;
import org.latefire.deals.models.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by dw on 19/03/17.
 */

public class DatabaseManager {

  private static final String LOG_TAG = DatabaseManager.class.getSimpleName();
  private static DatabaseManager instance;

  private NewDealListener mListener;

  private DatabaseReference mDbRefCustomers;
  private DatabaseReference mDbRefBusinesses;
  private DatabaseReference mDbRefDeals;
  private DatabaseReference mDbRefCustomerDealRelations;

  private Map<String, Deal> mDeals;

  private DatabaseManager() {

    DatabaseReference rootDbRef = FirebaseDatabase.getInstance().getReference();

    mDbRefCustomers = rootDbRef.child("customers");
    mDbRefBusinesses = rootDbRef.child("businesses");

    mDeals = new LinkedHashMap<>();
    mDbRefDeals = rootDbRef.child("deals");
    mDbRefDeals.orderByChild("title").addChildEventListener(new ChildEventListener() {
      @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Deal deal = dataSnapshot.getValue(Deal.class);
        mDeals.put(dataSnapshot.getKey(), deal);
        if (mListener != null) mListener.newDeal(deal);
        Log.d(LOG_TAG, "Add deal: " + deal.getTitle());
      }

      @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
      }

      @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
      }

      @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {
      }

      @Override public void onCancelled(DatabaseError databaseError) {
      }
    });

    mDbRefCustomerDealRelations = rootDbRef.child("customerDealRelations");
  }

  public static synchronized DatabaseManager getInstance() {
    if (instance == null) instance = new DatabaseManager();
    return instance;
  }

  public void setNewDealListener(NewDealListener listener) {
    mListener = listener;
  }

  public String createDeal(Deal deal) {
    DatabaseReference child = mDbRefDeals.push();
    String id = child.getKey();
    deal.setId(id);
    child.setValue(deal);
    return id;
  }

  public String createCustomer(Customer customer) {
    DatabaseReference child = mDbRefCustomers.push();
    String id = child.getKey();
    customer.setId(id);
    child.setValue(customer);
    return id;
  }

  public String createBusiness(Business business) {
    DatabaseReference child = mDbRefBusinesses.push();
    String id = child.getKey();
    business.setId(id);
    child.setValue(business);
    return id;
  }

  public void createCustomerDealRelation(CustomerDealRelation relation) {

  }

  public Deal getDeal(String id) {
    return mDeals.get(id);
  }

  public ArrayList<Deal> getAllDeals() {
    ArrayList<Deal> deals = new ArrayList<>();
    mDbRefDeals.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot dealSnapshot : dataSnapshot.getChildren()) {
          deals.add(dealSnapshot.getValue(Deal.class));
        }
      }

      @Override public void onCancelled(DatabaseError databaseError) {
      }
    });
    Log.d(LOG_TAG, "getAllDeals: returning " + deals.size() + " deals");
    return deals;
  }

  public User getUser(String id) {
    return null;
  }

  public ArrayList<Deal> getDealsOfCustomer(String customerId) {
    return null;
  }

  public ArrayList<User> getCustomersOfDeal(String dealId) {
    return null;
  }

  public ArrayList<Deal> getDealsOfBusiness(String businessId) {
    return null;
  }

  public DatabaseReference getDealsReference() {
    return mDbRefDeals;
  }

  public interface NewDealListener {
    void newDeal(Deal deal);
  }
}
