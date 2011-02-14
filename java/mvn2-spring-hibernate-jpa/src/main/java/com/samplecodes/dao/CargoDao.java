package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Cargo;
import org.springframework.stereotype.Repository;

@Repository
public class CargoDao extends BasicDaoImpl<Cargo, Long> {

    @Override
    protected Class<Cargo> getEntityClass() {
        return Cargo.class;
    }
}
