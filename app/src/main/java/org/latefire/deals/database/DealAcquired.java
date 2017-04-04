package org.latefire.deals.database;

import java.io.Serializable;

/**
 * Created by dw on 02/04/17.
 */

public class DealAcquired implements Serializable {

  private Long acquisitionDate;
  private Long redemptionDate;
  private Deal deal;

  public DealAcquired() {}

  public Long getAcquisitionDate() {
    return acquisitionDate;
  }

  public void setAcquisitionDate(long acquisitionDate) {
    this.acquisitionDate = acquisitionDate;
  }

  public Long getRedemptionDate() {
    return redemptionDate;
  }

  public void setRedemptionDate(long redemptionDate) {
    this.redemptionDate = redemptionDate;
  }

  public Deal getDeal() {
    return deal;
  }

  public void setDeal(Deal deal) {
    this.deal = deal;
  }
}
