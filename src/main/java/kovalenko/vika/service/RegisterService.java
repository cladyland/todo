package kovalenko.vika.service;

import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;

public interface RegisterService {
    UserDTO register(UserCommand userCommand);
}
