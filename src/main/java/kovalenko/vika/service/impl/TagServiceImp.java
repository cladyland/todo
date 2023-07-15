package kovalenko.vika.service.impl;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.mapper.TagMapper;
import kovalenko.vika.service.TagService;
import kovalenko.vika.utils.AppMiddleware;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.List;

@Slf4j
public class TagServiceImp implements TagService {
    private final TagDAO tagDAO;
    private final TagMapper tagMapper;

    public TagServiceImp(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
        this.tagMapper = TagMapper.INSTANCE;

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
    public void createTag(TagCommand tagCommand) {
        AppMiddleware.checkIfTitleIsBlank(tagCommand.getTitle());

        try (Session session = tagDAO.getCurrentSession()) {
            session.getTransaction().begin();

            tagDAO.save(tagMapper.mapToEntity(tagCommand));

            session.getTransaction().commit();
            log.debug("Tag '{}' for user '{}' saved", tagCommand.getTitle(), tagCommand.getUserId());
        }
    }
}
