package org.latefire.deals.database;

/**
 * Created by dw on 19/03/17.
 */

abstract public class AbsUser extends AbsModel {

  private String email;
  private String phone;
  private String profilePhoto;

  AbsUser() {
  }

  AbsUser(String email, String phone) {
    this.email = email;
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getProfilePhoto() {
    return profilePhoto;
  }

  public void setProfilePhoto(String profilePhoto) {
    this.profilePhoto = profilePhoto;
  }
}
