package com.samplecodes.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@DiscriminatorValue("Driver")
public class Driver extends User {

    @OneToOne
    private Location location;

    @OneToMany(mappedBy="driver")
    @OrderBy("deliveryDate")
	private List<Shipment> shipmentList;

    public Driver() {
        super();
    }

    public Driver(String username, String password, Location location) {
        super(username, password, User.DRIVER);
        this.location = location;
    }

	public Location getLocation() {
		return location;
	}

	public void addShipment(Shipment shipment) {
		shipmentList.add(shipment);
	}


    public void setLocation(Location location) {
        this.location = location;
    }

    public void setShipmentList(ArrayList<Shipment> shipmentList) {
        this.shipmentList = shipmentList;
    }

    public List<Shipment> getShipmentList() {
        return shipmentList;
    }

}
