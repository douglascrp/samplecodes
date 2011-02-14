package com.samplecodes;

import com.samplecodes.dao.CompanyDao;
import com.samplecodes.model.Company;
import org.junit.Test;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration(locations = {"application-context.xml"})
public class CompanyTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String DEFAULT_COMPANY_NAME = "SampleCodes";
    private static final String NEW_COMPANY_NAME = "In20Seconds";

    @Resource
    private CompanyDao companyDao;

    //@Before
    public void cleanup() {
        cleanup(DEFAULT_COMPANY_NAME);
        cleanup(NEW_COMPANY_NAME);
    }

    @Test
    public void testSave() {
        Company company = createAndSaveCompany(DEFAULT_COMPANY_NAME);
        assertTrue(countRowsInTable(Company.class.getSimpleName()) > 0);

//        company = getSingleCompany(DEFAULT_COMPANY_NAME);
//        assertEquals("Name not saved correctly", DEFAULT_COMPANY_NAME, company.getName());
    }

    @Test
    public void testGetById() {
        Company company = createAndSaveCompany(DEFAULT_COMPANY_NAME);
        company = companyDao.findById(company.getId());
        assertEquals(company.getName(), DEFAULT_COMPANY_NAME);
    }

    //@Test
    public void testDelete() {
        int before = countRowsInTable(Company.class.getSimpleName());
        Company company = createAndSaveCompany(DEFAULT_COMPANY_NAME);
        company = companyDao.findById(company.getId());
        companyDao.remove(company);
        companyDao.getEntityManager().flush();
        assertEquals("Deleting company failed.", before - 1, countRowsInTable(Company.class.getSimpleName()));
    }

    @Test
    public void testUpdate() {
        Company company = createAndSaveCompany(DEFAULT_COMPANY_NAME);
        company = companyDao.findById(company.getId());
        assertTrue("The Company didn't get saved.", countRowsInTable(Company.class.getSimpleName()) > 0);

        company.setName(NEW_COMPANY_NAME);
        companyDao.merge(company);
        companyDao.getEntityManager().flush();

//        Company newCompany = getSingleCompany(NEW_COMPANY_NAME);
//        assertTrue(countRowsInTable(Company.class.getSimpleName()) > 0);
//        assertEquals("The name didn't get changed", NEW_COMPANY_NAME, newCompany.getName());
    }

    private Company getSingleCompany(String name) {
        return simpleJdbcTemplate.queryForObject(
                "select * from " + Company.class.getSimpleName() + " where name = ?", new RowMapper(), name);
    }

    private Company createAndSaveCompany(String name) {
        Company company = new Company();
        company.setName(name);

        companyDao.persist(company);

        // Must flush the company to the database before trying to find it
        companyDao.getEntityManager().flush();
        return company;
    }

    private void cleanup(String name) {
        for (Company company : companyDao.findByNamedQuery(Company.QUERY_NAME, name)) {
            companyDao.remove(company);
        }
    }

    private static class RowMapper implements ParameterizedRowMapper<Company> {
        public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
            Company result = new Company();
            result.setName(rs.getString("name"));
            return result;
        }
    }
}