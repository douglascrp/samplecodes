package com.samplecodes.service;

import com.samplecodes.dao.CustomerDao;
import com.samplecodes.model.Cargo;
import com.samplecodes.model.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomerService extends CommonService {

    @Resource
    CustomerDao customerDao;

    public Customer merge(Customer customer) {
       return customerDao.merge(customer);
    }

    public Customer addOrder(Customer customer, Cargo order) {
        customer = customerDao.merge(customer);
        customer.addOrder(order);
        return customerDao.merge(customer);
    }

    public boolean deleteCargo(Customer customer, Cargo cargo) {
        if (cargo.getShipmentList() == null || cargo.getShipmentList().size() == 0) {
            //customer.getOrders().remove(cargo);
            cargoDao.remove(cargo);
            // TODO: remove respective row from database
            // and merge all references to it
            return true;
        } else {
            return false;
        }
    }}
