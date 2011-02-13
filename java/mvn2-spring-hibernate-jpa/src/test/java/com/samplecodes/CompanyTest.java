package com.samplecodes;

import com.samplecodes.dao.CompanyDao;
import com.samplecodes.model.Company;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import static org.junit.Assert.*;
import org.junit.Test;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

@ContextConfiguration(locations = {"application-context.xml" })
public class CompanyTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String DEFAULT_COMPANY_NAME = "SampleCodes";
    private static final String NEW_COMPANY_NAME = "In20Seconds";
    @Resource
    private CompanyDao companyDao;

    @Test
    public void testSave() {
        Company company = createAndSaveCompany(DEFAULT_COMPANY_NAME);
        assertEquals(1, countRowsInTable(Company.class.getSimpleName()));

        company = getSingleCompany(DEFAULT_COMPANY_NAME);
        assertEquals("Name not saved correctly", DEFAULT_COMPANY_NAME, company.getName());
        //assertEquals("Age not saved correctly", 28, company.getAge());
    }

    @Test
    public void testGetById() {
        Company company = createAndSaveCompany(DEFAULT_COMPANY_NAME);
        company = companyDao.findById(company.getId());
        assertEquals(company.getName(), DEFAULT_COMPANY_NAME);
        //assertEquals(company.getAge(), 28);
    }

    @Test
    public void testDelete() {
        Company company = createAndSaveCompany(DEFAULT_COMPANY_NAME);
        company = companyDao.findById(company.getId());
        companyDao.delete(company);
        companyDao.getEntityManager().flush();
        assertEquals("Deleting company failed.", 0, countRowsInTable(Company.class.getSimpleName()));
    }

    @Test
    public void testUpdate() {
        Company company = createAndSaveCompany(DEFAULT_COMPANY_NAME);
        company = companyDao.findById(company.getId());
        assertEquals("The Company didn't get saved.", 1, countRowsInTable(Company.class.getSimpleName()));

        company.setName(NEW_COMPANY_NAME);
        //company.setAge(21);
        companyDao.update(company);
        companyDao.getEntityManager().flush();

        Company newCompany = getSingleCompany(NEW_COMPANY_NAME);
        assertEquals(1, countRowsInTable(Company.class.getSimpleName()));
        assertEquals("The name didn't get changed", NEW_COMPANY_NAME, newCompany.getName());
        //assertEquals("The Age didn't get changed", 21, newCompany.getAge());
    }

    private Company getSingleCompany(String name) {
        return simpleJdbcTemplate.queryForObject(
                "select * from " + Company.class.getSimpleName() + " where name = ?", new RowMapper(), name);
    }

    private Company createAndSaveCompany(String name) {
        Company company = new Company();
        company.setName(name);

        logger.info("CompanyDao " + companyDao);
        companyDao.save(company);

        // Must flush the company to the database before trying to find it
        companyDao.getEntityManager().flush();
        return company;
    }

    private static class RowMapper implements ParameterizedRowMapper<Company> {
        public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
            Company result = new Company();
            result.setName(rs.getString("name"));
            //result.setAge(rs.getInt("age"));
            return result;
        }
    }
}