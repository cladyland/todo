package kovalenko.vika.service;

import kovalenko.vika.dao.UserDAO;
import kovalenko.vika.dto.UserDTO;
import kovalenko.vika.model.User;
import org.hibernate.Session;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.util.Objects.isNull;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String passwordEncrypt(String password) throws NoSuchAlgorithmException {
        var messageDigest = MessageDigest.getInstance("MD5");
        byte[] hashBytes = messageDigest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        String hash = hashInt.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        return hash;
    }

    public UserDTO validate(String username, String password) throws NoSuchAlgorithmException {
        try(Session session = userDAO.getCurrentSession()) {
            session.getTransaction().begin();
            User user = userDAO.getUserByUsername(username, session);
            UserDTO userDTO = null;
            if (isNull(user)) {
                return null;
            }
            String passwordHash = passwordEncrypt(password);
            String userPasswordHash = user.getPasswordHash();

            if (passwordHash.equals(userPasswordHash)) {
                userDTO = UserDTO.builder()
                        .username(user.getUsername())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .build();
            }
            return userDTO;
        }
    }

    public void register(UserDTO userDTO, String password) throws NoSuchAlgorithmException {
        try(Session session = userDAO.getCurrentSession()) {
            session.getTransaction().begin();
            User newUser = User.builder()
                    .firstName(userDTO.getFirstName())
                    .lastName(userDTO.getLastName())
                    .username(userDTO.getUsername())
                    .passwordHash(passwordEncrypt(password))
                    .build();

            userDAO.save(newUser);
            session.getTransaction().commit();
        }
    }
}
