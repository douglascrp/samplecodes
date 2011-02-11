package com.samplecodes;

import com.samplecodes.dao.PersonDao;
import com.samplecodes.model.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.logging.Logger;


public class Main {

    static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(final String args[]) throws Exception {

        ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"com/samplecodes/application-context.xml"});

        PersonDao personDao = (PersonDao) context.getBean("personDao");

        Person person = new Person();
        person.setName("hossein");
        person.setAge(44);

        personDao.save(person);
        logger.info("Person " + person + " saved");
    }
}
