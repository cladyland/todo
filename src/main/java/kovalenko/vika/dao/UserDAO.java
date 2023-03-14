package kovalenko.vika.dao;

import kovalenko.vika.model.User;
import org.hibernate.Session;

public interface UserDAO {
    User save(final User entity);

    User update(final User entity);

    User getUserByUsername(String name, Session session);

    Long getUserId(String username);

    Session getCurrentSession();
}
