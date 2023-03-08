package kovalenko.vika.mapper;

import kovalenko.vika.command.TagCommand;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.model.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TagMapper extends BasicMapper<Tag, TagDTO> {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    @Override
    Tag mapToEntity(TagDTO tagDTO);

    @Override
    TagDTO mapToDTO(Tag tag);
    Tag mapToEntity(TagCommand tagCommand);
}
