package kovalenko.vika.dao.impl;

import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.model.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagDAOImp implements TagDAO {
    private final SessionFactory sessionFactory;

    public TagDAOImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Tag getById(Long id, Session session) {
        return session.get(Tag.class, id);
    }

    @Override
    public Tag save(Tag entity) {
        getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public Tag update(final Tag entity) {
        return getCurrentSession().merge(entity);
    }

    @Override
    public Tag delete(Long id, Session session) {
        Tag element = getById(id, session);
        session.remove(element);
        return element;
    }

    @Override
    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public List<TagDTO> getDefaultTags(Session session) {
        String queryStr = "select new kovalenko.vika.dto.TagDTO(t.id, t.title, t.color) from Tag t where t.isDefault = true";
        Query<TagDTO> query = session.createQuery(queryStr, TagDTO.class);
        return query.getResultList();
    }

    @Override
    public List<TagDTO> getUserTags(Long userId) {
        String queryStr = "select new kovalenko.vika.dto.TagDTO(t.id, t.title, t.color) from Tag t where t.userId = :user";
        Query<TagDTO> query = getCurrentSession().createQuery(queryStr, TagDTO.class);
        query.setParameter("user", userId);
        return query.getResultList();
    }

    @Override
    public Set<Tag> getTagsByIds(Set<Long> ids) {
        String queryStr = "select t from Tag t where t.id in (:ids)";
        Query<Tag> query = getCurrentSession().createQuery(queryStr, Tag.class);
        query.setParameter("ids", ids);
        return new HashSet<>(query.getResultList());
    }

}
