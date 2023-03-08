package kovalenko.vika.mapper;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.dto.CommentDTO;
import kovalenko.vika.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper extends BasicMapper<Comment, CommentDTO> {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Override
    Comment mapToEntity(CommentDTO entity);

    @Override
    CommentDTO mapToDTO(Comment entity);

    Comment mapToEntity(CommentCommand commentCommand);
}
