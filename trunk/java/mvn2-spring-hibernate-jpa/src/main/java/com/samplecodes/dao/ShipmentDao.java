package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Shipment;
import org.springframework.stereotype.Repository;

@Repository
public class ShipmentDao extends BasicDaoImpl<Shipment, Long> {

    @Override
    protected Class<Shipment> getEntityClass() {
        return Shipment.class;
    }
}
