package com.samplecodes.dao;

import com.samplecodes.model.Privilege;
import com.samplecodes.service.AdminService;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(locations = {"/com/samplecodes/application-context.xml"})
public class UserDaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Resource
    UserDao userDao;
    @Resource
    AdminService adminService;

    @Test
    public void testGetUserPrivilege() throws Exception {
       adminService.refershAdmin("admin", "123");

      List<Privilege> privileges = userDao.getUserPrivilege("admin", "123");
        assertNotNull(privileges);
        assertTrue(privileges.size() > 0);
        assertEquals(privileges.get(0), Privilege.ADMIN);
    }
}
