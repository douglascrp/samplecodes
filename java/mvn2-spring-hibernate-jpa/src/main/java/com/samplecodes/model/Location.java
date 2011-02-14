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

    @ManyToOne
    private Location adjacent;
	
    @OneToMany(mappedBy = "adjacent")
    private List<Location> adjacentList;

	public List<Location> getNeighbours(){
		return new ArrayList<Location>(adjacentList);
	}
	
	public Location(String name) {
		this.name = name;
		this.adjacentList = new ArrayList<Location>();
		this.adjacentList.add(this);
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
