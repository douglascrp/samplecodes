package com.samplecodes.model;

import javax.persistence.*;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="type",
    discriminatorType= DiscriminatorType.STRING
)
@DiscriminatorValue("User")
public class User {

    public static final int ADMIN = 1, CUSTOMER = 2, DRIVER = 3;

    @Id
    protected String username;
    protected int privilege;
    protected String password;


    public User() {
    }

    public User(String username, String password, int privilege) {
        this.username = username;
        this.password = password;
        this.privilege = privilege;
    }

    public String getUsername() {
        return username;
    }

    public int getPrivilege() {
        return privilege;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return username;
    }
}
