package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDao extends BasicDaoImpl<Employee, Long> {
    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }
}
