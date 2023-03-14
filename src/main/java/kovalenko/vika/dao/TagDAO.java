package kovalenko.vika.dao;

import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.model.Tag;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;

public interface TagDAO {
    Tag save(final Tag entity);

    Tag update(final Tag entity);

    List<TagDTO> getDefaultTags(Session session);

    List<TagDTO> getUserTags(Long userId);

    Set<Tag> getTagsByIds(Set<Long> ids);

    Session getCurrentSession();
}
