package kovalenko.vika.dao;

import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.model.Tag;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class TagDAO extends AbstractDAO<Tag>{

    public TagDAO(SessionFactory sessionFactory) {
        super(Tag.class, sessionFactory);
    }

    public List<TagDTO> getDefaultTags() {
        String queryStr = "select new kovalenko.vika.dto.TagDTO(t.title, t.color) from Tag t where t.isDefault = true";
        Query<TagDTO> query = getCurrentSession().createQuery(queryStr, TagDTO.class);
        return query.getResultList();
    }

}
