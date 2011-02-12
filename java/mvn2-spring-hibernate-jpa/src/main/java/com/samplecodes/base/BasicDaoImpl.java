package com.samplecodes.base;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public abstract class BasicDaoImpl<E, I> implements BasicDao<E, I> {

    EntityManager entityManager;

    protected abstract Class<E> getEntityClass();

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void save(E entity) {
        entityManager.persist(entity);
    }
    
    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void update(E entity) {
        entityManager.persist(entity);
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public void delete(E entity) {
       entityManager.remove(entity);
    }

    @Override
    public List<E> list() {
        return entityManager.createQuery("from " + getEntityClass().getSimpleName()).getResultList();
    }

    @Override
    public E findById(I id) {
        return entityManager.find(getEntityClass(), id);
    }

    @Override
    public E findById(Class<E> c, I id) {
        return entityManager.find(c, id);
    }

    @Override
    public List<E> findByNamedQuery(final String queryName) {
        return findByNamedQuery(queryName, new Object[]{});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<E> findByNamedQuery(final String queryName, final Object... values) /*throws DataAccessException*/ {
        Query queryObject = entityManager.createNamedQuery(queryName);
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                queryObject.setParameter(i + 1, values[i]);
            }
        }
        return queryObject.getResultList();

    }

}
