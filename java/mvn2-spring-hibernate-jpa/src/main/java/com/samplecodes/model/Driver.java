package com.samplecodes.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@DiscriminatorValue("Driver")
public class Driver extends User {

    @OneToOne
    private Location currentLocation;

    @OneToMany(mappedBy="driver")
    @OrderBy("deliveryDate")
	private List<Shipment> shipmentList;

	public Location getLocation() {
		return currentLocation;
	}

	public void addShipment(Shipment shipment) {
		shipmentList.add(shipment);
	}

	public Driver(String username, String password, Location currentLocation) {
        super(username, password, User.DRIVER);
		shipmentList = new ArrayList<Shipment>();
		this.currentLocation = currentLocation;
	}

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setShipmentList(ArrayList<Shipment> shipmentList) {
        this.shipmentList = shipmentList;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public List<Shipment> getShipmentList() {
        return shipmentList;
    }

}
