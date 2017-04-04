package org.latefire.deals.business.deal.customers;

import java.io.Serializable;

/**
 * Created by dw on 04/04/17.
 */
class DealCustomerInfo implements Serializable {

  private Long acquisitionDate;
  private Long redemptionDate;

  public DealCustomerInfo() {
  }

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
}
