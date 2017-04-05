package org.latefire.deals.base;

import java.util.ArrayList;
import org.latefire.deals.database.DatabaseManager;
import org.latefire.deals.database.Deal;

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
