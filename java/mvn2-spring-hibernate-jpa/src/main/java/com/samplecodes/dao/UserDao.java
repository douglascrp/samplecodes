package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Admin;
import com.samplecodes.model.Privilege;
import com.samplecodes.model.User;
import com.samplecodes.model.UserId;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UserDao extends BasicDaoImpl<User, UserId> {

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    public List<Privilege> getUserPrivilege(String userName, String password) {
        List<Privilege> privileges = new ArrayList<Privilege>();
        for(User user : findByNamedQuery(User.USER_QUERY, userName, password)) {
           privileges.add(user.getPrivilege());
        }
        return privileges;
    }
}
