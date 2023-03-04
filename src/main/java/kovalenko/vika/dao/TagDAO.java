package kovalenko.vika.dao;

import kovalenko.vika.model.Tag;
import org.hibernate.SessionFactory;

public class TagDAO extends AbstractDAO<Tag>{

    public TagDAO(SessionFactory sessionFactory) {
        super(Tag.class, sessionFactory);
    }
}
