package kovalenko.vika.dao;

import org.hibernate.Session;

public interface IDAO<T> {
    T getById(Long id, Session session);
    T save(final T entity);
    T update(final T entity);
    T delete(Long id, Session session);
}
