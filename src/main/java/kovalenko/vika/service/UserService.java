package kovalenko.vika.service;

import kovalenko.vika.dto.UserDTO;

public interface UserService {
    Long getUserId(String username);

    UserDTO validate(String username, String password);
}
