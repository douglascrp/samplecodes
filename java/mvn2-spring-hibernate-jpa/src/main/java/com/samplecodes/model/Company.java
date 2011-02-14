package com.samplecodes.model;


import javax.persistence.*;
import java.util.Set;

@NamedQueries({
@NamedQuery(name="company.name.like",
            query="select r from Company r where r.name like ?"),
@NamedQuery(name="company.name",
            query="select r from Company r where r.name = ?")
})
@Entity
public class Company {

    public final static String QUERY_NAME = "company.name";

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy="company", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Set<Employee> employees;

   public Set<Employee> getEmployee() {
       return employees;
  }

    public Long getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
