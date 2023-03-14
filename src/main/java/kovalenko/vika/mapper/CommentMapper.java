package kovalenko.vika.mapper;

import kovalenko.vika.command.CommentCommand;
import kovalenko.vika.dto.CommentDTO;
import kovalenko.vika.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper
public interface CommentMapper extends BasicMapper<Comment, CommentDTO> {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Override
    Comment mapToEntity(CommentDTO entity);

    @Override
    default CommentDTO mapToDTO(Comment entity) {
        return CommentDTO.builder()
                .username(entity.getUser().getUsername())
                .contents(entity.getContents())
                .createDate(formattedDateTime(entity.getCreateDate()))
                .build();
    }

    Comment mapToEntity(CommentCommand commentCommand);

    private String formattedDateTime(LocalDateTime dateTime) {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(dateTime);
    }
}