package kovalenko.vika.service.impl;

import jakarta.persistence.NoResultException;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.exception.RegisterException;
import kovalenko.vika.exception.ValidationException;
import kovalenko.vika.utils.Hashing;
import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.mapper.UserMapper;
import kovalenko.vika.model.User;
import kovalenko.vika.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.regex.Pattern;

@Slf4j
public class UserServiceImp implements UserService {
    private static final String WRONG_CHARACTERS = "The {} '{}' does not match the pattern, registration is not possible";
    private static final String ONLY_LETTERS = "can only contains latin letters";
    private final UserDAO userDAO;
    private final UserMapper userMapper;
    private final Hashing hashing;

    public UserServiceImp(UserDAO userDAO, Hashing hashing) {
        this.userDAO = userDAO;
        this.userMapper = UserMapper.INSTANCE;
        this.hashing = hashing;

        log.debug("'UserServiceImp' initialized");
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

    @Override
    public UserDTO register(UserCommand userCommand) {

        try (Session session = userDAO.getCurrentSession();) {
            session.getTransaction().begin();

            checkData(userCommand);

            String passwordHash = hashing.getPasswordHash(userCommand.getPassword());
            User newUser = userMapper.mapToEntity(userCommand);
            newUser.setPasswordHash(passwordHash);

            checkIfUsernameBusy(userCommand.getUsername());
            userDAO.save(newUser);

            session.getTransaction().commit();

            log.info("User '{}' registered", newUser.getUsername());
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
            if (!user.getUsername().equals(username)) {
                log.warn("The case of characters in the username '{}' does not match", username);
                throw new ValidationException("Wrong username!");
            }
            return user;
        } catch (NoResultException ex) {
            log.warn("User with username '{}' not found", username);
            throw new ValidationException("Wrong username!");
        }
    }

    private void checkData(UserCommand command) {
        String username = command.getUsername();
        if (!isWordCharacter(username)) {
            log.warn(WRONG_CHARACTERS, "username", username);
            throw new RegisterException("Username can contains numbers and latin letters");
        }

        String firstName = command.getFirstName();
        if (!isWordOnlyOfLetters(firstName)) {
            log.warn(WRONG_CHARACTERS, "first name", firstName);
            throw new RegisterException("First name " + ONLY_LETTERS);
        }

        String lastName = command.getLastName();
        if (!isWordOnlyOfLetters(lastName)) {
            log.warn(WRONG_CHARACTERS, "last name", lastName);
            throw new RegisterException("Last name " + ONLY_LETTERS);
        }

        checkPassword(command.getPassword());
    }

    private void checkPassword(String password) {
        int minPasswordLength = 8;

        if (password.length() < minPasswordLength) {
            throw new RegisterException("Password must consist of at least 8 characters");
        }
        if (!hashing.isPasswordStrong(password)) {
            log.warn("Password is too weak");
            throw new RegisterException("Password should contains at least 1 number, 1 lowercase letter, " +
                    "1 uppercase letter and 1 symbol");
        }
    }

    private void checkIfUsernameBusy(String username) {
        try {
            Long id = userDAO.getUserId(username);
            if (id != null) {
                log.warn("Username '{}' is already busy", username);
                throw new RegisterException("Username is already busy");
            }
        } catch (NoResultException ex) {
            log.info("Username '{}' is free", username);
        }
    }

    private boolean isWordOnlyOfLetters(String word) {
        String regex = "[a-zA-Z]+";
        return Pattern.matches(regex, word);
    }

    private boolean isWordCharacter(String word) {
        String regex = "\\w*";
        return Pattern.matches(regex, word);
    }
}
