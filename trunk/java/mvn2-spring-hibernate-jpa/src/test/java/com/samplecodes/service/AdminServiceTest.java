package com.samplecodes.service;

import com.samplecodes.base.Constants;
import com.samplecodes.dao.CargoDao;
import com.samplecodes.model.Cargo;
import com.samplecodes.model.Location;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@ContextConfiguration(locations = {AdminServiceTest.APPLICATION_CONTEXT_SOURCE})
public class AdminServiceTest extends AbstractTransactionalJUnit4SpringContextTests implements Constants {

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
        Cargo cargo = new Cargo();
        Location destination = new Location("SF");
        cargo = adminService.editCargoDestination(cargo, destination);
        assertEquals(cargoDao.findById(cargo.getId()).getDestination().getName(), "SF");
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

}
