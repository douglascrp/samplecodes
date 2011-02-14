package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Customer;
import com.samplecodes.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao extends BasicDaoImpl<Customer, String> {

    @Override
    protected Class<Customer> getEntityClass() {
        return Customer.class;
    }
}
