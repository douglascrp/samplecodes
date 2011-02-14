package com.samplecodes.model;

import javax.persistence.*;
import java.util.List;


@Entity
@DiscriminatorValue("Customer")
public class Customer extends User {

    @OneToMany(mappedBy="customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @OrderBy("orderDate")
    private List<Cargo> orders;

    public Customer() {
        super();
    }

	public Customer(String username, String password) {
		super(username, password, User.CUSTOMER);
	}

    public List<Cargo> getOrders() {
        return orders;
    }

    public void addOrder(Cargo order) {
        orders.add(order);
    }

}