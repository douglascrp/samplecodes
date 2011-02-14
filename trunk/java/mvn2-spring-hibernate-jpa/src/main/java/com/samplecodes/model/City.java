package com.samplecodes.model;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("City")
public class City extends Location {

    public City(String name) {
        super(name);
    }
}
