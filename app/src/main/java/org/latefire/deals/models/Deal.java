package org.latefire.deals.models;

import java.util.Date;

/**
 * Created by dw on 19/03/17.
 */

public class Deal {

    private String id;
    private String businessUserId;
    private String title;
    private String description;
    private String currency;
    private double regularPrice;
    private double dealPrice;
    private Date beginValidity;
    private Date endValidity;

    public Deal() {
    }

    public Deal(String id, String businessUserId, String title, String description, String currency, double regularPrice, double dealPrice, Date beginValidity, Date endValidity) {
        this.id = id;
        this.businessUserId = businessUserId;
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

    public String getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(String businessUserId) {
        this.businessUserId = businessUserId;
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

    public Date getBeginValidity() {
        return beginValidity;
    }

    public void setBeginValidity(Date beginValidity) {
        this.beginValidity = beginValidity;
    }

    public Date getEndValidity() {
        return endValidity;
    }

    public void setEndValidity(Date endValidity) {
        this.endValidity = endValidity;
    }
}
