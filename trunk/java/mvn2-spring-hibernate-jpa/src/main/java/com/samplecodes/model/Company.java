package com.samplecodes.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class Company {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

//    private Set<Employee> employees;

//    @OneToMany(mappedBy="troop")
//    public Set<Employee> getEmployee() {
//        return employees;
//    }

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
