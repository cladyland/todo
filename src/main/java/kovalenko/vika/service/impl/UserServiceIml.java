package kovalenko.vika.service.impl;

import kovalenko.vika.utils.Hashing;
import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.mapper.UserMapper;
import kovalenko.vika.model.User;
import kovalenko.vika.service.UserService;
import org.hibernate.Session;

import static java.util.Objects.isNull;

public class UserServiceIml implements UserService {
    private final UserDAO userDAO;
    private final UserMapper userMapper;
    private final Hashing hashing;

    public UserServiceIml(UserDAO userDAO, Hashing hashing) {
        this.userDAO = userDAO;
        this.userMapper = UserMapper.INSTANCE;
        this.hashing = hashing;
    }

    @Override
    public UserDTO validate(String username, String password)  {
        try(Session session = userDAO.getCurrentSession()) {
            session.getTransaction().begin();

            User user = userDAO.getUserByUsername(username, session);
            UserDTO userDTO = null;
            if (isNull(user)) {
                return null;
            }
            String userPasswordHash = user.getPasswordHash();
            boolean correctPassword = hashing.validatePassword(password, userPasswordHash);

            if (correctPassword) {
                userDTO = userMapper.mapToDTO(user);
            }
            session.getTransaction().commit();
            return userDTO;
        }
    }

    @Override
    public UserDTO register(UserCommand userCommand) {
        try(Session session = userDAO.getCurrentSession()) {
            session.getTransaction().begin();

            String passwordHash = hashing.getPasswordHash(userCommand.getPassword());
            User newUser = userMapper.mapToEntity(userCommand);
            newUser.setPasswordHash(passwordHash);
            userDAO.save(newUser);

            session.getTransaction().commit();
            return userMapper.mapToDTO(newUser);
        }
    }

    @Override
    public Long getUserId(String username) {
        try(Session session = userDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Long id = userDAO.getUserId(username);
            session.getTransaction().commit();
            return id;
        }
    }
}
