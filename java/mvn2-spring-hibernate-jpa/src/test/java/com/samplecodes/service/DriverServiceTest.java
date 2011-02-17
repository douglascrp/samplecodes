package com.samplecodes.service;

import com.samplecodes.base.Constants;
import com.samplecodes.model.Cargo;
import com.samplecodes.model.Driver;
import com.samplecodes.model.Location;
import com.samplecodes.model.Shipment;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.util.Locale;

@ContextConfiguration(locations = {DriverServiceTest.APPLICATION_CONTEXT_SOURCE})
public class DriverServiceTest extends AbstractTransactionalJUnit4SpringContextTests implements Constants {

    @Resource
    DriverService driverService;

    @Test
    public void testListDrivers() throws Exception {

    }

    @Test
    public void testReportShipment() throws Exception {
        Location location = new Location("tehran");
        Driver driver = driverService.refreshDriver("hossein", "1234", location);
        Cargo cargo = new Cargo();
        Shipment shipment = new Shipment(cargo);
        shipment.setDriver(driver);
        driverService.reportShipment(shipment, driver.getLocation());
    }

    @Test
    public void testCanPickup() throws Exception {

    }

    @Test
    public void testPickupShipment() throws Exception {

    }

    @Test
    public void testReportLocation() throws Exception {

    }

    @Test
    public void testGetUnfinishedJobs() throws Exception {

    }

    @Test
    public void testAssignDriver() throws Exception {

    }
}
