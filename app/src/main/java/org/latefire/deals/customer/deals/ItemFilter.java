package org.latefire.deals.customer.deals;

import org.latefire.deals.database.DealAcquired;

/**
 * Created by dw on 03/04/17.
 */
public interface ItemFilter {
  boolean isDisplayItem(DealAcquired dealAcquired);
}
