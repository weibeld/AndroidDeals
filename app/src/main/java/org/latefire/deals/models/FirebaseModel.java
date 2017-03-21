package org.latefire.deals.models;

/**
 * Created by dw on 21/03/17.
 */

public abstract class FirebaseModel {

  protected String id;

  public FirebaseModel() {
  }

  protected FirebaseModel(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
