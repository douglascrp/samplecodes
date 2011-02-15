package com.samplecodes.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@DiscriminatorValue("Customer")
public class Customer extends User {

    @OneToMany(mappedBy="customer", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @OrderBy("orderDate")
    private List<Cargo> orders = new ArrayList<Cargo>();

    public Customer() {
    }

	public Customer(String username, String password) {
		super(username, password, User.CUSTOMER);
	}

    public List<Cargo> getOrders() {
        return orders;
    }

    public void addOrder(Cargo order) {
        order.setCustomer(this);
        getOrders().add(order);
    }

}
