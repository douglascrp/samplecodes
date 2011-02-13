package com.samplecodes;

import com.samplecodes.dao.CompanyDao;
import com.samplecodes.model.Company;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;


public class Main {

    static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(final String args[]) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"com/samplecodes/application-context.xml"});

        CompanyDao companyDao = (CompanyDao) context.getBean("companyDao");

        Company company = new Company();
        company.setName("hossein");
        //company.setAge(44);

        companyDao.save(company);
        logger.info("Company " + company + " saved");
    }
}
