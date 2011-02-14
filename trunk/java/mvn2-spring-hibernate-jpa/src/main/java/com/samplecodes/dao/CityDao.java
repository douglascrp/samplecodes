package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.City;
import org.springframework.stereotype.Repository;

@Repository
public class CityDao extends BasicDaoImpl<City, String> {

    @Override
    protected Class<City> getEntityClass() {
        return City.class;
    }
}
