package com.samplecodes.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="type",
    discriminatorType= DiscriminatorType.STRING
)
@DiscriminatorValue("User")
public class User {

    public static final int ADMIN = 1, CUSTOMER = 2, DRIVER = 3;

    @EmbeddedId
    private UserId userId;

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    protected String password;


    public User() {
        userId = new UserId(null, 0);
    }

    public User(String username, String password, int privilege) {
        userId = new UserId(username, privilege);
        this.password = password;
    }

    public String getUsername() {
        return userId.username;
    }

    public int getPrivilege() {
        return userId.privilege;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return userId.username;
    }
}
