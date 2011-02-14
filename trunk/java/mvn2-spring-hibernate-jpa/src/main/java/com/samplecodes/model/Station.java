package com.samplecodes.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Station")
public class Station extends Location{

    public Station(String name) {
        super(name);
    }
}
