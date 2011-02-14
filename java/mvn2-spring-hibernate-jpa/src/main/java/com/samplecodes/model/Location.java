package com.samplecodes.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="type",
    discriminatorType= DiscriminatorType.STRING
)
@DiscriminatorValue("Location")
public class Location {

	@Id
    protected String name;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Location adjacent;
	
    @OneToMany(mappedBy = "adjacent", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private List<Location> adjacentList;

	public List<Location> getNeighbours(){
		return new ArrayList<Location>(adjacentList);
	}
	
    public Location() {
    }

    public Location(String name) {
        this.name = name;
    }

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
