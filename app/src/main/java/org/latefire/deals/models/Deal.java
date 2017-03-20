package org.latefire.deals.models;

import java.util.Date;

/**
 * Created by dw on 19/03/17.
 */

public class Deal {

  private String id;
  private String businessId;
  private String title;
  private String description;
  private String currency;
  private double regularPrice;
  private double dealPrice;
  private String beginValidity;
  private String endValidity;

  public Deal() {
  }

  public Deal(String id, String businessId, String title, String description, String currency,
      double regularPrice, double dealPrice, String beginValidity, String endValidity) {
    this.id = id;
    this.businessId = businessId;
    this.title = title;
    this.description = description;
    this.currency = currency;
    this.regularPrice = regularPrice;
    this.dealPrice = dealPrice;
    this.beginValidity = beginValidity;
    this.endValidity = endValidity;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

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

  public String getBeginValidity() {
    return beginValidity;
  }

  public void setBeginValidity(String beginValidity) {
    this.beginValidity = beginValidity;
  }

  public String getEndValidity() {
    return endValidity;
  }

  public void setEndValidity(String endValidity) {
    this.endValidity = endValidity;
  }
}
