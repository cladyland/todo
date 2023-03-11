package kovalenko.vika.service.impl;

import jakarta.persistence.NoResultException;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.exception.ValidationException;
import kovalenko.vika.utils.Hashing;
import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.mapper.UserMapper;
import kovalenko.vika.model.User;
import kovalenko.vika.service.UserService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImp implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserDAO userDAO;
    private final UserMapper userMapper;
    private final Hashing hashing;

    public UserServiceImp(UserDAO userDAO, Hashing hashing) {
        this.userDAO = userDAO;
        this.userMapper = UserMapper.INSTANCE;
        this.hashing = hashing;

        LOG.info("'UserServiceImp' initialized");
    }

    @Override
    public UserDTO validate(String username, String password) {
        try (Session session = userDAO.getCurrentSession()) {
            session.getTransaction().begin();

            User user = findUser(username, session);
            String userPasswordHash = user.getPasswordHash();
            boolean passwordIsCorrect = hashing.validatePassword(password, userPasswordHash);

            UserDTO userDTO;
            if (passwordIsCorrect) {
                userDTO = userMapper.mapToDTO(user);
            } else {
                LOG.warn("Incorrect password entered for user '{}'", username);
                throw new ValidationException("Wrong password!");
            }

            session.getTransaction().commit();
            return userDTO;
        }
    }

    @Override
    public UserDTO register(UserCommand userCommand) {
        try (Session session = userDAO.getCurrentSession()) {
            session.getTransaction().begin();

            String passwordHash = hashing.getPasswordHash(userCommand.getPassword());
            User newUser = userMapper.mapToEntity(userCommand);
            newUser.setPasswordHash(passwordHash);
            userDAO.save(newUser);

            session.getTransaction().commit();
            LOG.info("User {} registered", newUser.getUsername());
            return userMapper.mapToDTO(newUser);
        }
    }

    @Override
    public Long getUserId(String username) {
        try (Session session = userDAO.getCurrentSession()) {
            session.getTransaction().begin();
            Long id = userDAO.getUserId(username);
            session.getTransaction().commit();
            return id;
        }
    }

    private User findUser(String username, Session session) {
        try {
            User user = userDAO.getUserByUsername(username, session);
            if (!user.getUsername().equals(username)){
                LOG.warn("The case of characters in the username {} does not match", username);
                throw new ValidationException("Wrong username!");
            }
            return user;
        } catch (NoResultException ex) {
            LOG.warn("User with username '{}' not found", username);
            throw new ValidationException("Wrong username!");
        }
    }
}
