package net.usefulbits.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Person {
    private long id;
    private String name;
    private int age;
    
    public void setId(long id) {
        this.id = id;
    }
    
    @Id
    public long getId() {
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
}
