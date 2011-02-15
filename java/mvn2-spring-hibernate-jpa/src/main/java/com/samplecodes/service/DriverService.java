package com.samplecodes.service;


import com.samplecodes.dao.DriverDao;
import com.samplecodes.dao.ShipmentDao;
import com.samplecodes.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DriverService extends CommonService {

    @Resource
    DriverDao driverDao;
    @Resource
    ShipmentDao shipmentDao;

    public Driver merge(Driver driver) {
       return driverDao.merge(driver);
    }

    public Shipment merge(Shipment shipment) {
       return shipmentDao.merge(shipment);
    }

    public Driver getDriver(String name) {
        return driverDao.findById(name);
    }

    public List<Driver> listDrivers() {
        return driverDao.list();
    }

    public void reportShipment(Shipment shipment, Location location){
        shipment.add(new Event(new Date(), location));
        shipmentDao.persist(shipment);
    }

    public boolean canPickup(Driver driver, Shipment shipment) {
        return driver.getShipmentList().contains(shipment)
                && driver.getLocation() == shipment.getOrigin()
                && shipment.getDriver() == driver
                && shipment.getState() == Shipment.ASSIGNED_DRIVER;
    }

    public void pickupShipment(Driver driver, Shipment shipment) {
        if (canPickup(driver, shipment)) {
            shipment.setPickUpState();
        } else {
            throw new RuntimeException("Not allowed to pick up this shipment");
        }
    }

    public void reportLocation(Driver driver, Location location) {
        driver.setLocation(location);
        // TODO: merge respective field for currentLocation
        for (Shipment shipment : driver.getShipmentList()) {
            if (shipment.getState() == Shipment.ON_THE_WAY) {
                if (driver.getLocation() == shipment.getCargo().getDestination()) {
                    driver.setLocation(location);
                    shipment.setState(Shipment.DELIVERED);
                }
                reportShipment(shipment, location);
            }
        }
        driverDao.persist(driver);
        // System.err.println("Reported " + currentLocation.getName());
    }

    public int getUnfinishedJobs(Driver driver) {
        int returnValue = 0;
        for(Shipment shipment : driver.getShipmentList()){
            if(shipment.getState() != Shipment.DELIVERED)
                returnValue++;
        }
        return returnValue;
    }

    public void assignDriver(Driver driver, Shipment shipment) {
        if (shipment.hasDriver() == false) {
            driver.addShipment(shipment);
            shipment.assignDriver(driver);
            // TODO: as you can see here, one change is made
            // and that is: I added a new relation between a
            // driver and a shipment. Now, notice that this means
            // two changes in our fields. One is in driver.shipments
            // and the other is in cargo.driver
            //
            // These two fields are changed in the two methods above
            // and they can be handled deeper, or they can be handled
            // here.
        }
    }

//	public ArrayList<String> listAdjacentLocations() {
//		ArrayList<String> returnValue = new Arral<String>();
//
//		for (int i = 0; i < systemManager.locations.size(); ++i) {
//			returnValue.add(systemManager.locations.get(i).getName());
//		}
//
//		return returnValue;
//	}

}
