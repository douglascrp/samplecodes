package com.samplecodes.service;

import com.samplecodes.dao.CargoDao;
import com.samplecodes.dao.CustomerDao;
import com.samplecodes.model.Admin;
import com.samplecodes.model.Cargo;
import com.samplecodes.model.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CustomerService extends CommonService {

    @Resource
    CustomerDao customerDao;

    public Customer saveOrUpdate(Customer customer) {
       return customerDao.merge(customer);
    }

    public void addOrder(Customer customer, Cargo order) {
        customer.addOrder(order);
        customerDao.save(customer);

    }
}
