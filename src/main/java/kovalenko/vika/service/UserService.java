package kovalenko.vika.service;

import kovalenko.vika.dto.UserDTO;

public interface UserService {
    UserDTO validate(String username, String password);
    void register(UserDTO userDTO, String password);
}
