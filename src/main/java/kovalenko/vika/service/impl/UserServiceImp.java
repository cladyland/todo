package kovalenko.vika.service.impl;

import jakarta.persistence.NoResultException;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.exception.ValidationException;
import kovalenko.vika.utils.Hashing;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.model.User;
import kovalenko.vika.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

@Slf4j
public class UserServiceImp extends AbstractUserService implements UserService {
    private static final String WRONG_USERNAME = "Wrong username!";

    public UserServiceImp(UserDAO userDAO, Hashing hashing) {
        super(userDAO, hashing);
        log.debug("'UserServiceImp' initialized");
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
                log.warn("Incorrect password entered for user '{}'", username);
                throw new ValidationException("Wrong password!");
            }

            session.getTransaction().commit();
            return userDTO;
        }
    }

    private User findUser(String username, Session session) {
        try {
            User user = userDAO.getUserByUsername(username, session);
            if (!user.getUsername().equals(username)) {
                log.warn("The case of characters in the username '{}' does not match", username);
                throw new ValidationException(WRONG_USERNAME);
            }
            return user;
        } catch (NoResultException ex) {
            log.warn("User with username '{}' not found", username);
            throw new ValidationException(WRONG_USERNAME);
        }
    }
}
