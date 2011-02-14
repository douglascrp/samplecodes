package com.samplecodes.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Cargo {

    @Id
    @GeneratedValue
    private Long id;

    private String type;
    private long weight; // in grams
    private Date orderDate;
    private Date dueDate;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Customer customer;

    @OneToMany(mappedBy = "cargo")
	private List<Shipment> shipmentList;

    @ManyToOne
    @JoinColumn(name = "origin")
    private Location origin;

    @ManyToOne
    @JoinColumn(name = "destination")
    private Location destination;

    public Cargo(String type, Date orderDate, Date dueDate, Location origin,
            Location destination, long weight) throws InstantiationException {

        if(type.isEmpty()){
            throw new InstantiationException();
        }
        this.type = type;
        this.orderDate = orderDate;
        this.dueDate = dueDate;
        this.destination = destination;
        this.origin = origin;
        this.weight = weight;

        // TODO: create respective row in database
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setShipmentList(List<Shipment> shipmentList) {
        this.shipmentList = shipmentList;
    }

    public void setType(String type) {
        this.type = type;
    }

	public Customer getCustomer() {
		return customer;
	}
	
	public String getType() {
		return type;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public List<Shipment> getShipmentList() {
		return shipmentList;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public Location getOrigin() {
		return origin;
	}

	public Location getDestination() {
		return destination;
	}

	public long getWeight() {
		return weight;
	}
	
	public boolean delivered(){
		if(this.shipmentList == null){
			return false;
		}
		for(int i = 0; i < shipmentList.size(); ++i){
			if(shipmentList.get(i).getState() != Shipment.DELIVERED){
				return false;
			}
		}
		return true;
	}

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setOrigin(Location origin) {
        this.origin = origin;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }
}
