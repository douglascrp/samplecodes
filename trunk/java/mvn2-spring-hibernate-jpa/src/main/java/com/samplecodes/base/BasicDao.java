package com.samplecodes.base;

import java.util.List;

public interface BasicDao<E, I> {

    List<E> list();

    E findById(I id);

    E findById(Class<E> c, I id);

    void save(E entity);

    List<?> findByNamedQuery(final String queryName);

    List<?> findByNamedQuery(final String queryName, final Object... values);

    void update(E entity);
}
