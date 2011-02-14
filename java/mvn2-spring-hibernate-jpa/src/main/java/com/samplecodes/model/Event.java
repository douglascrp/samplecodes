package com.samplecodes.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Event{

    @Id
    @GeneratedValue
    private Long id;
    private Date date;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Location location;

    public Event() {
    }

    public Event(Date date, Location location) {
        this.date = date;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}