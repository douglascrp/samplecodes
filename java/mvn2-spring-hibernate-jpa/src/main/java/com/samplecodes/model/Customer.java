package com.samplecodes.model;

import com.samplecodes.base.Constants;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.CascadeType.*;


@Entity
@DiscriminatorValue("Customer")
public class Customer extends User {

    // IT is important to add annotation to the decalation if you add it to getOrders you get
    // Could not determine type for: java.util.List, at table: User, for columns: [org.hibernate.mapping.Column(orders)]
    // no idea why this is red @OneToMany(orphanRemoval=true
    @OneToMany(mappedBy= CUSTOMER_FOREIGN_KEY_FIELD, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @Cascade(DELETE_ORPHAN)
    @OrderBy(ORDERS_ORDER_BY_FIELD)
    private List<Cargo> orders;

    public Customer() {
    }

	public Customer(String username, String password) {
		super(username, password, Privilege.CUSTOMER);
	}

    public List<Cargo> getOrders() {
        if(orders == null) {
            orders = new ArrayList<Cargo>();
        }
        return orders;
    }

    public void addOrder(Cargo order) {
        order.setCustomer(this);
        getOrders().add(order);
    }

}
