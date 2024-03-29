package com.samplecodes.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Shipment {

    public static final int ORDERED = 1, ASSIGNED_DRIVER = 2, ON_THE_WAY = 3, DELIVERED = 4;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	private Cargo cargo;

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	private Driver driver;

    @OneToMany(mappedBy = "location", cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private List<Event> eventList;

    private Date deliveryDate;
    private int state;

    public Shipment() {
    }

    public Shipment(Cargo cargo) {
        this.cargo = cargo;
        this.state = ORDERED;
        this.eventList = new ArrayList<Event>();
    }

    public Long getId() {
        return id;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void add(Event event) {
       eventList.add(event);
    }

	public long getApproximateWeight(){
		return cargo.getWeight() / cargo.getShipmentList().size();
	}
	
	public String getType(){
		return cargo.getType();
	}

	public Location getOrigin(){
		return cargo.getOrigin();
	}
	
	public Location getDestination(){
		return cargo.getDestination();
	}
	
	public Date getDueDate() {
		return cargo.getDueDate();
	}
	
	public Driver getDriver(){
		return driver;
	}
	
	public Date getDeliveryDate(){
		return deliveryDate;
	}
	
	public void setDriver(Driver driver){
		state = ASSIGNED_DRIVER;
		this.driver = driver;
	}
	
	public void setState(int state){
		this.state = state;
	}
	
	public void setPickUpState(){
		if(state == DELIVERED || state == ON_THE_WAY){
			throw new RuntimeException("shipment has already been picked up");
		}
		setState(ON_THE_WAY);
	}
	
	public int getState(){
		return state;
	}
	
	public boolean hasDriver(){
		return state != ORDERED;
	}
	
	public Cargo getCargo(){
		return this.cargo;
	}

}
