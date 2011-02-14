package com.samplecodes.base;

import java.util.List;

public interface BasicDao<E, I> {

    E findById(I id);

    void save(E entity);

    E merge(E entity);

    void remove(E entity);

    List<E> list();

    E findById(Class<E> c, I id);

    List<?> findByNamedQuery(final String queryName);

    List<?> findByNamedQuery(final String queryName, final Object... values);
}
