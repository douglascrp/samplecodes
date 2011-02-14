package com.samplecodes.dao;


import com.samplecodes.base.BasicDaoImpl;
import com.samplecodes.model.Event;
import org.springframework.stereotype.Repository;

@Repository
public class EventDao extends BasicDaoImpl<Event, Long> {

    @Override
    protected Class<Event> getEntityClass() {
        return Event.class;
    }
}
