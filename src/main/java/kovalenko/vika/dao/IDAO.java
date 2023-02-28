package kovalenko.vika.dao;

public interface IDAO<T> {
    T getById(Long id);
    T save(final T entity);
}
