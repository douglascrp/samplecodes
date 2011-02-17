package com.samplecodes.model;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserId implements Serializable {
  String username;
  int privilege;

    public UserId() {
    }

    public UserId(String username, int privilege) {
        this.username = username;
        this.privilege = privilege;
    }
}