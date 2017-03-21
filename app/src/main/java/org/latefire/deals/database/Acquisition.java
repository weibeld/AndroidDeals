package org.latefire.deals.database;

// TODO: 21/03/17 Change type of dates to java.util.Date
/**
 * Created by dw on 19/03/17.
 */
public class Acquisition extends AbsModel {

  private String customerId;
  private String dealId;
  private String acquisitionDate;
  private String redemptionDate;

  public Acquisition() {
  }

  public Acquisition(String customerId, String dealId, String acquisitionDate, String redemptionDate) {
    this.customerId = customerId;
    this.dealId = dealId;
    this.acquisitionDate = acquisitionDate;
    this.redemptionDate = redemptionDate;

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

  public String getAcquisitionDate() {
    return acquisitionDate;
  }

  public void setAcquisitionDate(String acquisitionDate) {
    this.acquisitionDate = acquisitionDate;
  }

  public String getRedemptionDate() {
    return redemptionDate;
  }

  public void setRedemptionDate(String redemptionDate) {
    this.redemptionDate = redemptionDate;
  }

}
