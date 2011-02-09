package net.usefulbits.dao;

import net.usefulbits.model.Person;

public interface BasicDao<E, I> {
    E findById(I id);
    
    void save(E entity);

    void delete(E entity);

    void update(E entity);
}
