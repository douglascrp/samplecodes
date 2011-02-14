package com.samplecodes;

import com.samplecodes.dao.CompanyDao;
import com.samplecodes.dao.EmployeeDao;
import com.samplecodes.model.*;
import com.samplecodes.service.AdminService;
import com.samplecodes.service.CustomerService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;


public class Main {

    static final Logger logger = Logger.getLogger(Main.class.getName());
    static final ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"com/samplecodes/application-context.xml"});

    public static void main(final String args[]) throws Exception {

        CustomerService customerService = (CustomerService) context.getBean("customerService");
        Customer customer = new Customer("hossein", "1235");
        Cargo cargo = new Cargo();
        customer = customerService.addOrder(customer, cargo);
        //customerService.deleteCargo(customer, cargo);
        logger.info("Done");
    }

    private static void AdminServiceTest() {
        AdminService adminService = (AdminService) context.getBean("adminService");
        Cargo cargo = new Cargo();
        cargo = adminService.assignShipments(cargo, 5);
    }

    private static Company CompanyTest() {
        CompanyDao companyDao = (CompanyDao) context.getBean("companyDao");
        EmployeeDao employeeDao = (EmployeeDao) context.getBean("employeeDao");

        Company company = new Company();
        company.setName("in20seconds");

        Employee employee = new Employee();
        employee.setName("hossein");
        employee.setAge(22);
        employee.setCompany(company);

        employeeDao.persist(employee);
        return company;
    }
}
