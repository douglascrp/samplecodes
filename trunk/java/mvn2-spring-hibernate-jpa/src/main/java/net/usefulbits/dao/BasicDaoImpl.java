package net.usefulbits.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    public void save(E entity) {
        entityManager.persist(entity);
    }
    
    public E findById(I id) {
        return entityManager.find(getEntityClass(), id);
    }

    public void update(E entity) {
        entityManager.persist(entity);
    }

    public void delete(E entity) {
       entityManager.remove(entity);
    }
}
