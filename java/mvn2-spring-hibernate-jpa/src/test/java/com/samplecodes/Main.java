package com.samplecodes;

import com.samplecodes.dao.CompanyDao;
import com.samplecodes.dao.EmployeeDao;
import com.samplecodes.model.Company;
import com.samplecodes.model.Employee;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;


public class Main {

    static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(final String args[]) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"com/samplecodes/application-context.xml"});

        CompanyDao companyDao = (CompanyDao) context.getBean("companyDao");
        EmployeeDao employeeDao = (EmployeeDao) context.getBean("employeeDao");

        Company company = new Company();
        company.setName("in20seconds");

        Employee employee = new Employee();
        employee.setName("hossein");
        employee.setAge(22);
        employee.setCompany(company);

        employeeDao.save(employee);
        logger.info("Company " + company + " saved");
    }
}
