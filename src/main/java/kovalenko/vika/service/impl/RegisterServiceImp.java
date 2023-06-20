package kovalenko.vika.service.impl;

import jakarta.persistence.NoResultException;
import kovalenko.vika.command.UserCommand;
import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.exception.RegisterException;
import kovalenko.vika.model.User;
import kovalenko.vika.service.RegisterService;
import kovalenko.vika.utils.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.regex.Pattern;

import static kovalenko.vika.utils.constants.AttributeConstant.FIRST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.LAST_NAME;
import static kovalenko.vika.utils.constants.AttributeConstant.PASSWORD;
import static kovalenko.vika.utils.constants.AttributeConstant.USERNAME;

@Slf4j
public class RegisterServiceImp extends AbstractUserService implements RegisterService {
    private static final String WRONG_CHARACTERS = "The {} '{}' does not match the pattern, registration is not possible";
    private static final String ONLY_LETTERS = "can only contains latin letters";

    public RegisterServiceImp(UserDAO userDAO, Hashing hashing) {
        super(userDAO, hashing);
        log.debug("'RegisterServiceImp' initialized");
    }

    @Override
    public UserDTO register(UserCommand userCommand) {
        try (Session session = userDAO.getCurrentSession()) {
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

    private void checkData(UserCommand command) {
        var errors = new HashMap<String, String>();

        String username = command.getUsername();
        if (!isWordCharacter(username)) {
            log.warn(WRONG_CHARACTERS, USERNAME, username);
            errors.put(USERNAME, "can contains numbers and latin letters");
        }

        String firstName = command.getFirstName();
        if (isNotOnlyLatinLetters(firstName)) {
            log.warn(WRONG_CHARACTERS, FIRST_NAME, firstName);
            errors.put(FIRST_NAME, ONLY_LETTERS);
        }

        String lastName = command.getLastName();
        if (isNotOnlyLatinLetters(lastName)) {
            log.warn(WRONG_CHARACTERS, LAST_NAME, lastName);
            errors.put(LAST_NAME, ONLY_LETTERS);
        }

        checkPassword(command.getPassword(), errors);
        if (!errors.isEmpty()) {
            throw new RegisterException(errors);
        }
    }

    private boolean isWordCharacter(String word) {
        String regex = "\\w*";
        return Pattern.matches(regex, word);
    }

    private boolean isNotOnlyLatinLetters(String word) {
        String regex = "[a-zA-Z]+";
        return !Pattern.matches(regex, word);
    }

    private void checkPassword(String password, HashMap<String, String> errors) {
        int minPasswordLength = 8;

        if (password.length() < minPasswordLength) {
            errors.put(PASSWORD, "must consist of at least 8 characters");
        }
        if (!hashing.isPasswordStrong(password)) {
            log.warn("Password is too weak");
            errors.put(PASSWORD, "should contains at least 1 number, 1 lowercase letter, 1 uppercase letter and 1 symbol");
        }
    }

    private void checkIfUsernameBusy(String username) {
        try {
            Long id = userDAO.getUserId(username);
            if (id != null) {
                String message = String.format("Username %s is already busy", username);
                log.warn(message);
                throw new RegisterException(message);
            }
        } catch (NoResultException ex) {
            log.debug("Username '{}' is free", username);
        }
    }
}
