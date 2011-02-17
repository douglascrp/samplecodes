package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Customer;
import com.samplecodes.model.User;
import com.samplecodes.model.UserId;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDao extends BasicDaoImpl<Customer, UserId> {

    @Override
    protected Class<Customer> getEntityClass() {
        return Customer.class;
    }
}
