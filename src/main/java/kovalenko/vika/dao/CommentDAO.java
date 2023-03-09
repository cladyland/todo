package kovalenko.vika.dao;

import kovalenko.vika.model.Comment;
import org.hibernate.Session;

public interface CommentDAO {
    Comment getById(Long id, Session session);
    Comment save(final Comment entity);
    Comment update(final Comment entity);
    Comment delete(Long id, Session session);
    Session getCurrentSession();
}
