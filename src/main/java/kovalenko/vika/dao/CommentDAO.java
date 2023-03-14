package kovalenko.vika.dao;

import kovalenko.vika.model.Comment;
import org.hibernate.Session;

public interface CommentDAO {
    Comment save(final Comment entity);

    Session getCurrentSession();
}
