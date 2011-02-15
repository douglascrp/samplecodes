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

    public synchronized Cargo addOrder(Customer customer, Cargo order) {
        customer.addOrder(order);
        customer = customerDao.merge(customer);
        return customer.getOrders().get(customer.getOrders().size() - 1);
    }

    public boolean deleteCargo(Customer customer, Cargo cargo) {
        if (cargo.getShipmentList() == null || cargo.getShipmentList().size() == 0) {
            customer.getOrders().remove(cargo);
            customerDao.merge(customer);
            //cargoDao.remove(cargo);
            // TODO: remove respective row from database
            // and merge all references to it
            return true;
        } else {
            return false;
        }
    }

    public Customer refershCustomer(String username, String password) {
        Customer customer = customerDao.findById(username);
        if(customer == null) {
            customer = new Customer(username, password);
        } else if(password != null) {
            customer.setPassword(password);
        }
        return customerDao.merge(customer);
    }
}
