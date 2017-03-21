package org.latefire.deals.controler;

import java.util.ArrayList;
import java.util.List;
import org.latefire.deals.managers.DatabaseManager;
import org.latefire.deals.models.Deal;

/**
 * Created by phongnguyen on 3/20/17.
 */

public class BaseControler {
  private static BaseControler instance;
  private DatabaseManager databaseManager;

  public static synchronized BaseControler getInstance() {
    if (instance == null) {
      instance = new BaseControler();
    }
    return instance;
  }

  public void init(DatabaseManager databaseManager) {
    this.databaseManager = databaseManager;
  }

  public List<Deal> getAllDeals() {
    return null;  // Not needed
  }

  public void createDeal(Deal deal){
    databaseManager.createDeal(deal);
  }

  public ArrayList<Deal> getDummyDeal() {
    ArrayList<Deal> deals = new ArrayList<>();
    for(int i=0;i<20;i++){
      Deal deal = new Deal();
      deal.setTitle("This is deal title");
      deals.add(deal);
    }
    return deals;
  }
}
