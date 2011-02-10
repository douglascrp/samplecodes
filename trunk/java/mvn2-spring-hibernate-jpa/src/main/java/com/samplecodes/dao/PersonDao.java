package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Person;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao extends BasicDaoImpl<Person, Long> {
    @Override
    protected Class<Person> getEntityClass() {
        return Person.class;
    }
}
