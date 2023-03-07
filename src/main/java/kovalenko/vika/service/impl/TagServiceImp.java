package kovalenko.vika.service.impl;

import kovalenko.vika.dao.TagDAO;
import kovalenko.vika.dao.TaskDAO;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.mapper.TagMapper;
import kovalenko.vika.model.Task;
import kovalenko.vika.service.TagService;
import org.hibernate.Session;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class TagServiceImp implements TagService {
    private final TagDAO tagDAO;
    private final TaskDAO taskDAO;
    private final TagMapper tagMapper;
    public TagServiceImp(TagDAO tagDAO, TaskDAO taskDAO) {
        this.tagDAO = tagDAO;
        this.taskDAO = taskDAO;
        tagMapper = TagMapper.INSTANCE;
    }

    @Override
    public List<TagDTO> getDefaultTags() {
        try (Session session = tagDAO.getCurrentSession()) {
            session.getTransaction().begin();
            List<TagDTO> defaultTags = tagDAO.getDefaultTags();
            session.getTransaction().commit();
            return defaultTags;
        }
    }

    @Override
    public List<TagDTO> getUserTags(Long userId) {
        try (Session session = tagDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Task defaultTask = taskDAO.getDefaultTask(userId);
            if (isNull(defaultTask)) {
                defaultTask = createDefaultTask(userId);
                taskDAO.save(defaultTask);
            }
            session.getTransaction().commit();
            return defaultTask
                    .getTags()
                    .stream()
                    .map(tagMapper::mapToDTO)
                    .collect(Collectors.toList());
        }
    }

    private Task createDefaultTask(Long userId) {
        return Task.builder()
                .userId(userId)
                .title("default")
                .build();
    }
}
