package com.samplecodes;

import com.samplecodes.dao.PersonDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import static org.junit.Assert.*;
import org.junit.Test;

import com.samplecodes.model.Person;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

@ContextConfiguration(locations = {"application-context.xml" })
public class PersonTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    private PersonDao personDao;

    @Test
    public void testSave() {
        createAndSavePerson("David", 28);
        assertEquals(1, countRowsInTable("person"));

        Person david = getSinglePerson("David");
        assertEquals("Name not saved correctly", "David", david.getName());
        assertEquals("Age not saved correctly", 28, david.getAge());
    }

    @Test
    public void testGetById() {
        createAndSavePerson("David", 28);
        Person david = personDao.findById(0L);
        assertEquals(david.getName(), "David");
        assertEquals(david.getAge(), 28);
    }

    @Test
    public void testDelete() {
        createAndSavePerson("David", 28);
        Person david = personDao.findById(0L);
        personDao.delete(david);
        personDao.getEntityManager().flush();
        assertEquals("Deleting person failed.", 0, countRowsInTable("person"));
    }

    @Test
    public void testUpdate() {
        createAndSavePerson("David", 28);
        Person person = personDao.findById(0L);
        assertEquals("The Person didn't get saved.", 1, countRowsInTable("person"));

        person.setName("Jane");
        person.setAge(21);
        personDao.update(person);
        personDao.getEntityManager().flush();

        Person jane = getSinglePerson("David");
        assertEquals(1, countRowsInTable("person"));
        assertEquals("The name didn't get changed", "Jane", jane.getName());
        assertEquals("The Age didn't get changed", 21, jane.getAge());
    }

    private Person getSinglePerson(String name) {
        return simpleJdbcTemplate.queryForObject(
                "select * from person where name = ?", new PersonRowMapper(), name);
    }

    private void createAndSavePerson(String name, int age) {
        Person person = new Person();
        person.setName(name);
        person.setAge(age);

        logger.info("PersonDao " + personDao);
        personDao.save(person);

        // Must flush the person to the database before trying to find it
        personDao.getEntityManager().flush();
    }

    private static class PersonRowMapper implements ParameterizedRowMapper<Person> {
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person result = new Person();
            result.setName(rs.getString("name"));
            result.setAge(rs.getInt("age"));
            return result;
        }
    }
}