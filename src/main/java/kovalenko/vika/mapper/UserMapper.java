package kovalenko.vika.mapper;

import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends BasicMapper<User, UserDTO> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User mapToEntity(UserDTO userDTO);

    UserDTO mapToDTO(User user);
}
