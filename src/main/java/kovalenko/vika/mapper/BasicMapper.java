package kovalenko.vika.mapper;

public interface BasicMapper<T, E> {
    T mapToEntity(E entity);

    E mapToDTO(T entity);
}
