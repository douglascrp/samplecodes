package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Location;
import org.springframework.stereotype.Repository;

@Repository
public class LocationDao extends BasicDaoImpl<Location, String> {

    @Override
    protected Class<Location> getEntityClass() {
        return Location.class;
    }
}
