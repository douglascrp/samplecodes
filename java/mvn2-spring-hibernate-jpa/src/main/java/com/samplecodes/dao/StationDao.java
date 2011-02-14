package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Station;
import org.springframework.stereotype.Repository;

@Repository
public class StationDao extends BasicDaoImpl<Station, String> {

    @Override
    protected Class<Station> getEntityClass() {
        return Station.class;
    }
}
