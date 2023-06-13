package kovalenko.vika.mapper;

import kovalenko.vika.command.TaskCommand;
import kovalenko.vika.dto.CommentDTO;
import kovalenko.vika.dto.TagDTO;
import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.model.Tag;
import kovalenko.vika.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Mapper
public interface TaskMapper extends BasicMapper<Task, TaskDTO> {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Override
    Task mapToEntity(TaskDTO taskDTO);

    @Override
    default TaskDTO mapToDTO(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().getValue())
                .status(task.getStatus().getValue())
                .tags(getTaskTagDTO(task))
                .comments(getTaskCommentsDTO(task))
                .build();
    }

    Task mapToEntity(TaskCommand taskCommand);

    private List<TagDTO> getTaskTagDTO(Task task) {
        Set<Tag> tags = task.getTags();

        return isNull(tags) ? null : tags
                .stream()
                .map(TagMapper.INSTANCE::mapToDTO)
                .collect(Collectors.toList());
    }

    private List<CommentDTO> getTaskCommentsDTO(Task task) {
        if (isNull(task.getComments())) {
            return null;
        }
        return task.getComments()
                .stream()
                .map(CommentMapper.INSTANCE::mapToDTO)
                .sorted((comment1, comment2) ->
                        comment2.getCreateDate()
                                .compareTo(comment1.getCreateDate()))
                .collect(Collectors.toList());
    }
}
