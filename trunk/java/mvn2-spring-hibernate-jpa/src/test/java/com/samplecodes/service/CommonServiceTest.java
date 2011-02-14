package com.samplecodes.service;

import com.samplecodes.dao.CargoDao;
import com.samplecodes.model.Cargo;
import com.samplecodes.model.City;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = {"../application-context.xml"})
public class CommonServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    CommonService commonService;

    @Before
    public void setup() {
        commonService.saveOrUpdate(new City("SF"));
        commonService.saveOrUpdate(new City("San Jose"));
        commonService.saveOrUpdate(new City("Berkeley"));
        commonService.saveOrUpdate(new Cargo());
        commonService.saveOrUpdate(new Cargo());
    }

    @Test
    public void testListCities() throws Exception {
        assertEquals(3, commonService.listCities().size());
    }

    @Test
    public void testGetCity() throws Exception {
        assertEquals(commonService.getCity("SF").getName(), "SF");
    }

    @Test
    public void testListCargos() throws Exception {
        assertEquals(2, commonService.listCargos().size());
    }

    @Test
    public void testLogin() throws Exception {

    }
}
