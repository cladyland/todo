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
    public T getById(Long id, Session session) {
        return session.get(clazz, id);
    }

    @Override
    public T save(T entity) {
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public T update(final T entity) {
        return getCurrentSession().merge(entity);
    }

    @Override
    public T delete(Long id, Session session) {
        T element = getById(id, session);
        session.remove(element);
        return element;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
