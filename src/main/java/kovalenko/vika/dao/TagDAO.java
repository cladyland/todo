package kovalenko.vika.dao;

import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.model.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagDAO extends AbstractDAO<Tag>{

    public TagDAO(SessionFactory sessionFactory) {
        super(Tag.class, sessionFactory);
    }

    public List<TagDTO> getDefaultTags(Session session) {
        String queryStr = "select new kovalenko.vika.dto.TagDTO(t.id, t.title, t.color) from Tag t where t.isDefault = true";
        Query<TagDTO> query = session.createQuery(queryStr, TagDTO.class);
        return query.getResultList();
    }

    public List<TagDTO> getUserTags(Long userId){
        String queryStr = "select new kovalenko.vika.dto.TagDTO(t.id, t.title, t.color) from Tag t where t.userId = :user";
        Query<TagDTO> query = getCurrentSession().createQuery(queryStr, TagDTO.class);
        query.setParameter("user", userId);
        return query.getResultList();
    }

    public Set<Tag> getTagsByIds(Set<Long> ids) {
        String queryStr = "select t from Tag t where t.id in (:ids)";
        Query<Tag> query = getCurrentSession().createQuery(queryStr, Tag.class);
        query.setParameter("ids", ids);
        return new HashSet<>(query.getResultList());
    }

}
