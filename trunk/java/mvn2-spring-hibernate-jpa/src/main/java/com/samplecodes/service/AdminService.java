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

    public Admin merge(Admin admin) {
       return adminDao.merge(admin);
    }

    @Override
    public Admin getUser(String userName) {
        return adminDao.findById(new UserId(userName, Privilege.ADMIN));
    }

    public Cargo assignShipments(Cargo cargo, int pieces) {
        // TODO: This part creates a bunch of shipments that
        // are part of a cargo. Before this method is called
        // the cargo had no shipments. A cargo can be composed
        // of zero shipments for a long time and during that time
        // that cargo's "shipment" field is null.

        List<Shipment> shipmentList = new ArrayList<Shipment>();
        for (int i = 0; i < pieces; ++i) {
            Shipment shipment = new Shipment(cargo);
            shipmentList.add(shipment);
        }
        cargo.setShipmentList(shipmentList);
        return cargoDao.merge(cargo);
    }

    public Cargo editCargoDestination(Cargo cargo, Location destination){
        cargo.setDestination(destination);
        return cargoDao.merge(cargo);
    }

    public Cargo editCargoType(Cargo cargo, String type){
        cargo.setType(type);
        return cargoDao.merge(cargo);
    }

    public Cargo editCargoDueDate(Cargo cargo, Date date){
        cargo.setDueDate(date);
        return cargoDao.merge(cargo);
    }

    public Cargo editCargoWeight(Cargo cargo, long weight){
        cargo.setWeight(weight);
        return cargoDao.merge(cargo);
    }

    public List<Cargo> listCargos() {
        return cargoDao.list();
    }
    public Admin refershAdmin(String username, String password) {
        Admin admin = adminDao.findById(new UserId(username, Privilege.CUSTOMER));
        if(admin == null) {
            admin = new Admin(username, password);
        } else if(password != null) {
            admin.setPassword(password);
        }
        return adminDao.merge(admin);
    }
}
