package org.latefire.deals.models;

/**
 * Created by dw on 19/03/17.
 */

// TODO: 21/03/17 Delete this class
public class CustomerDealRelation {

  private String id;
  private String customerId;
  private String dealId;
  //private Date dateOfRedemption  // null if deal has not been redeemed

  public CustomerDealRelation() {
  }

  public CustomerDealRelation(String id, String customerId, String dealId) {
    this.id = id;
    this.customerId = customerId;
    this.dealId = dealId;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getDealId() {
    return dealId;
  }

  public void setDealId(String dealId) {
    this.dealId = dealId;
  }
}
