package net.usefulbits.dao;


import net.usefulbits.model.Person;

public class PersonDao extends BasicDaoImpl<Person, Long> {
    @Override
    protected Class<Person> getEntityClass() {
        return Person.class;
    }
}
