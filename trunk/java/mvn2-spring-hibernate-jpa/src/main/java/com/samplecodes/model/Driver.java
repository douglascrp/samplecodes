package com.samplecodes.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@DiscriminatorValue("Driver")
public class Driver extends User {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Location location;

    @OneToMany(mappedBy= DRIVER_FOREIGN_KEY, cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    @OrderBy(SHIPMENT_ORDER_BY_FIELD)
	private List<Shipment> shipmentList;

    public Driver() {
        super();
    }

    public Driver(String username, String password, Location location) {
        super(username, password, Privilege.DRIVER);
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
