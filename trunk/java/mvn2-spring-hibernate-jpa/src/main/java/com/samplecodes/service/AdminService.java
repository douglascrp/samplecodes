package com.samplecodes.service;

import com.samplecodes.dao.AdminDao;
import com.samplecodes.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdminService extends CommonService {

    @Resource
    AdminDao adminDao;

    public Admin saveOrUpdate(Admin admin) {
       return adminDao.merge(admin);
    }

    public void assignShipments(Cargo cargo, int pieces) {
        // TODO: This part creates a bunch of shipments that
        // are part of a cargo. Before this method is called
        // the cargo had no shipments. A cargo can be composed
        // of zero shipments for a long time and during that time
        // that cargo's "shipment" field is null.

        List<Shipment> shipmentList = new ArrayList<Shipment>();
        for (int i = 0; i < pieces; ++i) {
            shipmentList.add(new Shipment(cargo));
        }
        cargo.setShipmentList(shipmentList);
        cargoDao.merge(cargo);

    }

    public void editCargoDestination(Cargo cargo, Location destination){
        cargo.setDestination(destination);
        cargoDao.merge(cargo);
    }

    public void editCargoType(Cargo cargo, String type){
        cargo.setType(type);
        cargoDao.merge(cargo);
    }

    public void editCargoDueDate(Cargo cargo, Date date){
        cargo.setDueDate(date);
        cargoDao.merge(cargo);
    }

    public void editCargoWeight(Cargo cargo, long weight){
        cargo.setWeight(weight);
        cargoDao.merge(cargo);
    }


    public List<Cargo> listCargos() {
        return cargoDao.list();
    }


    public boolean deleteCargo(Cargo cargo) {
        if (cargo.getShipmentList() == null) {
            cargo.getCustomer().getOrders().remove(cargo);
            cargoDao.remove(cargo);
            // TODO: remove respective row from database
            // and merge all references to it
            return true;
        } else {
            return false;
        }
    }

    public void assignDriver(Shipment shipment, Driver driver) {
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

}
