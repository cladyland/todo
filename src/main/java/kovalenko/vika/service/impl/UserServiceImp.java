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
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class UserServiceImp implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImp.class);
    private static final String WRONG_CHARACTERS = "The {} '{}' does not match the pattern, registration is not possible";
    private static final String ONLY_LETTERS = "can only contains latin letters";
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

        try(Session session = userDAO.getCurrentSession();) {
            session.getTransaction().begin();

            checkData(userCommand);

            String passwordHash = hashing.getPasswordHash(userCommand.getPassword());
            User newUser = userMapper.mapToEntity(userCommand);
            newUser.setPasswordHash(passwordHash);

            checkIfUsernameBusy(userCommand.getUsername());
            userDAO.save(newUser);

            session.getTransaction().commit();

            LOG.info("User '{}' registered", newUser.getUsername());
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
                LOG.warn("The case of characters in the username '{}' does not match", username);
                throw new ValidationException("Wrong username!");
            }
            return user;
        } catch (NoResultException ex) {
            LOG.warn("User with username '{}' not found", username);
            throw new ValidationException("Wrong username!");
        }
    }

    private void checkData(UserCommand command) {
        String username = command.getUsername();
        if (!isWordCharacter(username)) {
            LOG.warn(WRONG_CHARACTERS, "username", username);
            throw new RegisterException("Username can contains numbers and latin letters");
        }

        String firstName = command.getFirstName();
        if (!isWordOnlyOfLetters(firstName)) {
            LOG.warn(WRONG_CHARACTERS, "first name", firstName);
            throw new RegisterException("First name " + ONLY_LETTERS);
        }

        String lastName = command.getLastName();
        if (!isWordOnlyOfLetters(lastName)) {
            LOG.warn(WRONG_CHARACTERS, "last name", lastName);
            throw new RegisterException("Last name " + ONLY_LETTERS);
        }

        //TODO add password checking
    }

    private void checkIfUsernameBusy(String username){
        try {
            Long id = userDAO.getUserId(username);
            if (id != null) {
                LOG.warn("Username '{}' is already busy", username);
                throw new RegisterException("Username is already busy");
            }
        } catch (NoResultException ex){
            LOG.info("Username '{}' is free", username);
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