package com.samplecodes.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserId implements Serializable {
    String userName;
    Privilege privilege;

    public UserId() {
    }

    public UserId(String userName, Privilege privilege) {
        this.userName = userName;
        this.privilege = privilege;
    }
}