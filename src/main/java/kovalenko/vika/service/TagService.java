package kovalenko.vika.service;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dto.TagDTO;

import java.util.List;

public interface TagService {
    List<TagDTO> getDefaultTags();

    List<TagDTO> getUserTags(Long userId);

    void createTag(TagCommand tagCommand);
}
