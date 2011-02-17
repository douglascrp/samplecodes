package com.samplecodes.model;

import com.samplecodes.base.Constants;

import javax.persistence.*;

import static com.samplecodes.model.Privilege.USER;

@NamedQueries({
@NamedQuery(name= User.USER_QUERY_NAME,
            query= User.USER_QUERY)
})

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="type",
    discriminatorType= DiscriminatorType.STRING
)
@DiscriminatorValue("User")
public class User implements Constants {

    @EmbeddedId
    private UserId userId;

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }

    protected String password;


    public User() {
        userId = new UserId(null, USER);
    }

    public User(String username, String password, Privilege privilege) {
        userId = new UserId(username, privilege);
        this.password = password;
    }

    public String getUsername() {
        return userId.userName;
    }

    public Privilege getPrivilege() {
        return userId.privilege;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return userId.userName;
    }
}
