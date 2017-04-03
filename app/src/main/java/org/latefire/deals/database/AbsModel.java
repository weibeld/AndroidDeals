package org.latefire.deals.database;

import java.io.Serializable;

/**
 * Created by dw on 21/03/17.
 */

public abstract class AbsModel implements Serializable {

  protected String id;

  AbsModel() {
  }

  AbsModel(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  // Package local: allow setting of ID by database manager, but not any class outside this package
  void setId(String id) {
    this.id = id;
  }

}
