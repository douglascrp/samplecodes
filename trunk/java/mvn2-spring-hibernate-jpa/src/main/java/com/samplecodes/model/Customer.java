package com.samplecodes.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;


@Entity
@DiscriminatorValue("Customer")
public class Customer extends User {

    @OneToMany(mappedBy="customer")
    @OrderBy("orderDate")
    private List<Cargo> orders;

    public List<Cargo> getOrders() {
        return orders;
    }

	public Customer(String username, String password) {
		super(username, password, User.CUSTOMER);
	}

    public void addOrder(Cargo order) {
        orders.add(order);
    }

}
