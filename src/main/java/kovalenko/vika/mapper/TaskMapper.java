package kovalenko.vika.mapper;

import kovalenko.vika.dto.TaskDTO;
import kovalenko.vika.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TaskMapper extends BasicMapper<Task, TaskDTO> {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Override
    Task mapToEntity(TaskDTO taskDTO);

    @Override
    TaskDTO mapToDTO(Task task);
}
