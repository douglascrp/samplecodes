package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Driver;
import com.samplecodes.model.User;
import com.samplecodes.model.UserId;
import org.springframework.stereotype.Repository;

@Repository
public class DriverDao extends BasicDaoImpl<Driver, UserId> {

    @Override
    protected Class<Driver> getEntityClass() {
        return Driver.class;
    }
}
