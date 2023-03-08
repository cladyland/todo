package kovalenko.vika.dao;

import kovalenko.vika.model.Comment;
import org.hibernate.SessionFactory;

public class CommentDAO extends AbstractDAO<Comment> {

    public CommentDAO(SessionFactory sessionFactory) {
        super(Comment.class, sessionFactory);
    }
}
