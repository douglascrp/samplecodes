package com.samplecodes.model;


import javax.persistence.*;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int age;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    //@JoinColumn(name="COMP_ID")
    private Company company;

    public Long getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public int getAge() {
        return age;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
