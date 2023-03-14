package kovalenko.vika.service;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dto.TagDTO;

import java.util.List;
import java.util.Set;

public interface TagService {
    List<TagDTO> getDefaultTags();

    List<TagDTO> getUserTags(Long userId);

    TagDTO createTag(TagCommand tagCommand);

    Set<TagDTO> getTagsByIds(Set<Long> convertIds);
}