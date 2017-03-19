package org.latefire.deals.models;

/**
 * Created by dw on 19/03/17.
 */

public class PrivateUserDealRelation {

    private String id;
    private String privateUserId;
    private String dealId;

    public PrivateUserDealRelation() {
    }

    public PrivateUserDealRelation(String id, String privateUserId, String dealId) {
        this.id = id;
        this.privateUserId = privateUserId;
        this.dealId = dealId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrivateUserId() {
        return privateUserId;
    }

    public void setPrivateUserId(String privateUserId) {
        this.privateUserId = privateUserId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }
}
