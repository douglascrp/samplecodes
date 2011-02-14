package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Admin;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao extends BasicDaoImpl<Admin, String> {

    @Override
    protected Class<Admin> getEntityClass() {
        return Admin.class;
    }
}
