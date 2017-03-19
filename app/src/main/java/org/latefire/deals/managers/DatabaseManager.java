package org.latefire.deals.managers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.latefire.deals.models.Business;
import org.latefire.deals.models.Customer;
import org.latefire.deals.models.CustomerDealRelation;
import org.latefire.deals.models.Deal;
import org.latefire.deals.models.User;

import java.util.ArrayList;

/**
 * Created by dw on 19/03/17.
 */

public class DatabaseManager {

    private static DatabaseManager instance;

    private DatabaseReference mDbRefCustomers;
    private DatabaseReference mDbRefBusinesses;
    private DatabaseReference mDbRefDeals;
    private DatabaseReference mDbRefCustomerDealRelations;

    private DatabaseManager() {
        DatabaseReference rootDbRef = FirebaseDatabase.getInstance().getReference();
        mDbRefCustomers = rootDbRef.child("customers");
        mDbRefBusinesses = rootDbRef.child("businesses");
        mDbRefDeals = rootDbRef.child("deals");
        mDbRefCustomerDealRelations = rootDbRef.child("customerDealRelations");
    }


    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public void saveDeal(Deal deal) {

    }

    public String saveCustomer(Customer customer) {
        return pushAndReturnKey(mDbRefCustomers, customer);
    }

    public void saveBusiness(Business business) {
        mDbRefBusinesses.push().setValue(business);
    }

    public void saveCustomerDealRelation(CustomerDealRelation relation) {

    }

    public Deal getDeal(String id) {
        return null;
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

    private String pushAndReturnKey(DatabaseReference ref, Object object) {
        DatabaseReference newEntry = ref.push();
        newEntry.setValue(object);
        return newEntry.getKey();
    }
}
