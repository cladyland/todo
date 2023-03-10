package kovalenko.vika.service.impl;

import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.utils.Hashing;
import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.mapper.UserMapper;
import kovalenko.vika.model.User;
import kovalenko.vika.service.UserService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.isNull;

public class UserServiceImp implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserDAO userDAO;
    private final UserMapper userMapper;
    private final Hashing hashing;

    public UserServiceImp(UserDAO userDAO, Hashing hashing) {
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
                LOG.warn("User with username {} not found", username);
                return null;
            }
            String userPasswordHash = user.getPasswordHash();
            boolean passwordIsCorrect = hashing.validatePassword(password, userPasswordHash);

            if (passwordIsCorrect) {
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
            LOG.info("User {} registered", newUser.getUsername());
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
