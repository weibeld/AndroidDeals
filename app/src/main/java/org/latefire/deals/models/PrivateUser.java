package org.latefire.deals.models;

/**
 * Created by dw on 19/03/17.
 */

public class PrivateUser extends User {

    private String firstName;
    private String lastName;

    public PrivateUser() {
    }

    public PrivateUser(String id, String email, String phone, String firstName, String lastName) {
        super(id, email, phone);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
