package kovalenko.vika.service;

import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;

public interface UserService {
    UserDTO validate(String username, String password);
    UserDTO register(UserCommand userCommand);
    Long getUserId(String username);
}