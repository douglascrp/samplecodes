package com.samplecodes.model;

import javax.persistence.*;

import static com.samplecodes.model.Privilege.USER;

@NamedQueries({
@NamedQuery(name= User.USER_QUERY,
            query="select u from User u where u.userId.userName = ? and u.password = ?")
})

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name=User.TYPE,
    discriminatorType= DiscriminatorType.STRING
)
@DiscriminatorValue(User.CLASS_TYPE)
public class User {

    public static final String TYPE = "type";
    public static final String CLASS_TYPE = "User";
    public static final String USER_QUERY = "find_user";

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
