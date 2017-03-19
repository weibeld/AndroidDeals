package org.latefire.deals.managers;

import org.latefire.deals.models.Business;
import org.latefire.deals.models.Deal;
import org.latefire.deals.models.Customer;
import org.latefire.deals.models.CustomerDealRelation;
import org.latefire.deals.models.User;

import java.util.ArrayList;

/**
 * Created by dw on 19/03/17.
 */

public class DatabaseManager {

    private static DatabaseManager instance;

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public void saveDeal(Deal deal) {

    }

    public void saveUser(User user) {
        if (user instanceof Customer) {

        }
        else if (user instanceof Business) {

        }
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
}
