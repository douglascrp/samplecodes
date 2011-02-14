package com.samplecodes;

import com.samplecodes.dao.EmployeeDao;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.annotation.Resource;

@ContextConfiguration(locations = {"application-context.xml" })
public class EmployeeTest extends AbstractTransactionalJUnit4SpringContextTests {

    private static final String DEFAULT_EMPLOYEE_NAME = "Hossein";
    private static final String NEW_EMPLOYEE_NAME = "Hessam";

    @Resource
    private EmployeeDao employeeDao;

}