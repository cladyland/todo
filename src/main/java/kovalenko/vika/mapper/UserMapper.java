package kovalenko.vika.mapper;

import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends BasicMapper<User, UserDTO> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Override
    User mapToEntity(UserDTO userDTO);

    @Override
    UserDTO mapToDTO(User user);

    User mapToEntity(UserCommand userCommand);
}