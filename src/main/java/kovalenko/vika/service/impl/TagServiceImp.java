package kovalenko.vika.service.impl;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.mapper.TagMapper;
import kovalenko.vika.model.Tag;
import kovalenko.vika.service.TagService;
import kovalenko.vika.utils.AppUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class TagServiceImp implements TagService {
    private final TagDAO tagDAO;
    private final TagMapper tagMapper;

    public TagServiceImp(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
        tagMapper = TagMapper.INSTANCE;

        log.debug("'TagServiceImp' initialized");
    }

    @Override
    public List<TagDTO> getDefaultTags() {
        try (Session session = tagDAO.getCurrentSession()) {
            session.getTransaction().begin();

            List<TagDTO> defaultTags = tagDAO.getDefaultTags(session);

            session.getTransaction().commit();
            return defaultTags;
        }
    }

    @Override
    public List<TagDTO> getUserTags(Long userId) {
        try (Session session = tagDAO.getCurrentSession()) {
            session.getTransaction().begin();

            List<TagDTO> userTags = tagDAO.getUserTags(userId);

            session.getTransaction().commit();
            return userTags;
        }
    }

    @Override
    public TagDTO createTag(TagCommand tagCommand) {
        AppUtil.checkIfTitleIsBlank(tagCommand.getTitle());

        try (Session session = tagDAO.getCurrentSession()) {
            session.getTransaction().begin();

            Tag tag = tagDAO.save(tagMapper.mapToEntity(tagCommand));

            session.getTransaction().commit();
            return tagMapper.mapToDTO(tag);
        }
    }

    @Override
    public Set<TagDTO> getTagsByIds(Set<Long> ids) {
        try (Session session = tagDAO.getCurrentSession()) {
            session.getTransaction().begin();

            Set<Tag> tags = tagDAO.getTagsByIds(ids);

            session.getTransaction().commit();
            return tags.stream().map(tagMapper::mapToDTO).collect(Collectors.toSet());
        }
    }
}
