package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Company;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDao extends BasicDaoImpl<Company, Long> {

    @Override
    protected Class<Company> getEntityClass() {
        return Company.class;
    }
}
