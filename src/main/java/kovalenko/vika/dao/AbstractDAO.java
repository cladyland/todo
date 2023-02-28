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
        getCurrentSession().getTransaction().begin();
        getCurrentSession().saveOrUpdate(entity);
        getCurrentSession().getTransaction().commit();
        return entity;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
