package org.latefire.deals.database;

// TODO: 21/03/17 Change type of dates to java.util.Date

/**
 * Created by dw on 19/03/17.
 */
public class Deal extends AbsModel {

  private String businessId;
  private String title;
  private String description;
  private String currency;
  private double regularPrice;
  private double dealPrice;
  private String location;
  private long beginValidity;
  private long endValidity;
  private String photo;
  private String locationName;
  private double latitude;
  private double longitude;

  public Deal() {
  }

  //public Deal(String businessId, String title, String description, String currency,
  //    double regularPrice, double dealPrice, String beginValidity, String endValidity,
  //    String location) {
  //  this.businessId = businessId;
  //  this.title = title;
  //  this.description = description;
  //  this.currency = currency;
  //  this.regularPrice = regularPrice;
  //  this.dealPrice = dealPrice;
  //  this.beginValidity = beginValidity;
  //  this.endValidity = endValidity;
  //  this.location = location;
  //}

  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public double getRegularPrice() {
    return regularPrice;
  }

  public void setRegularPrice(double regularPrice) {
    this.regularPrice = regularPrice;
  }

  public double getDealPrice() {
    return dealPrice;
  }

  public void setDealPrice(double dealPrice) {
    this.dealPrice = dealPrice;
  }

  public long getBeginValidity() {
    return beginValidity;
  }

  public void setBeginValidity(long beginValidity) {
    this.beginValidity = beginValidity;
  }

  public long getEndValidity() {
    return endValidity;
  }

  public void setEndValidity(long endValidity) {
    this.endValidity = endValidity;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
