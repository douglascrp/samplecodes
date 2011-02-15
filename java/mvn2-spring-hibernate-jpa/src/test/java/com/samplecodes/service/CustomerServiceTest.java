package com.samplecodes.service;

import com.samplecodes.model.Cargo;
import com.samplecodes.model.Customer;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"/com/samplecodes/application-context.xml"})
public class CustomerServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    CustomerService customerService;

    @Test
    public void testAddOrder() throws Exception {
        Customer customer = new Customer("hossein", "1234");
        Cargo cargo = new Cargo();
        customerService.addOrder(customer, cargo);
        assertEquals(customer.getOrders().size(), 1);

    }

    @Test
    public void testDeleteCargo() throws Exception {
        Customer customer = new Customer("hossein", "1234");
        Cargo cargo = new Cargo();
        cargo = customerService.addOrder(customer, cargo);
        customerService.deleteCargo(customer, cargo);
        assertEquals(customer.getOrders().size(), 0);
    }
}
