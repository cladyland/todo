package kovalenko.vika.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractDAO<T> implements IDAO<T> {
    private final Class<T> clazz;
    private final SessionFactory sessionFactory;

    public AbstractDAO(Class<T> clazz, SessionFactory sessionFactory) {
        this.clazz = clazz;
        this.sessionFactory = sessionFactory;
    }

    @Override
    public T getById(Long id) {
        return null;
    }

    @Override
    public T save(T entity) {
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
