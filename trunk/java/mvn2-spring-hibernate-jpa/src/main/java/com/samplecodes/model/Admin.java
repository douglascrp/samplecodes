package com.samplecodes.model;

import com.samplecodes.base.Constants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;


@Entity
@DiscriminatorValue("Admin")
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String username, String password) {
		super(username, password, Privilege.ADMIN);
	}

}
