package com.samplecodes;

import com.samplecodes.dao.CompanyDao;
import com.samplecodes.dao.EmployeeDao;
import com.samplecodes.model.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Date;
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
//    public CommonService() {
//        users = new ArrayList<User>();
//        cargo = new ArrayList<Cargo>();
//        locations = new ArrayList<Location>();
//
//        if(true /* TODO: database is empty */){
//            this.locations.add(new Location("Tehran"));
//            this.locations.add(new Location("Qom"));
//            this.locations.add(new Location("Los Angelos"));
//            this.locations.add(new Location("مشهد"));
//            this.locations.add(new Location("کابل"));
//            this.locations.add(new Location("امامزاده هاشم"));
//
//            getLocation("Tehran").adjacent.add(getLocation("امامزاده هاشم"));
//            getLocation("امامزاده هاشم").adjacent.add(getLocation("Tehran"));
//            getLocation("Qom").adjacent.add(getLocation("امامزاده هاشم"));
//            getLocation("امامزاده هاشم").adjacent.add(getLocation("Qom"));
//            getLocation("Tehran").adjacent.add(getLocation("مشهد"));
//            getLocation("مشهد").adjacent.add(getLocation("Tehran"));
//            getLocation("مشهد").adjacent.add(getLocation("کابل"));
//            getLocation("کابل").adjacent.add(getLocation("مشهد"));
//
//            users.add(new Admin("admin", "12345", this));
//            users.add(new Driver("mammad", "12345", this, this.locations.get(0)));
//            users.add(new Driver("gholam", "12345", this, this.locations.get(0)));
//            users.add(new Customer("mohsen", "12345", this));
//
//            // TODO: save arralists to database
//        }
//
//        try {
//            ((Customer)getUser("mohsen")).addOrder(new Cargo("نخود", new Date(), new Date(), getLocation("Tehran"), getLocation("Qom"), 100000));
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//    }
}
