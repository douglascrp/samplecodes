package com.samplecodes.service;

import com.samplecodes.dao.CargoDao;
import com.samplecodes.model.Cargo;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@ContextConfiguration(locations = {"../application-context.xml"})
public class AdminServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    AdminService adminService;

    @Resource
    CargoDao cargoDao;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testAssignShipments() throws Exception {
        Cargo cargo = new Cargo();
        cargo = adminService.assignShipments(cargo, 5);
        assertNotNull(cargoDao.findById(cargo.getId()));
        assertEquals(cargoDao.findById(cargo.getId()).getShipmentList().size(), 5);
    }

    @Test
    public void testEditCargoDestination() throws Exception {

    }

    @Test
    public void testEditCargoType() throws Exception {

    }

    @Test
    public void testEditCargoDueDate() throws Exception {

    }

    @Test
    public void testEditCargoWeight() throws Exception {

    }

    @Test
    public void testListCargos() throws Exception {

    }

    @Test
    public void testDeleteCargo() throws Exception {

    }

    @Test
    public void testAssignDriver() throws Exception {

    }
}
