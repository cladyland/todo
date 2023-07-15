package kovalenko.vika.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class Hashing {
    private static final String ALGORITHM = "MD5";
    private static final String SALT = "0-*_*-0";
    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–%{}:;',?/*~$^+=<>_]).{8,32}$";
    private static final Pattern PATTERN = Pattern.compile(PASSWORD_REGEX);

    public boolean isPasswordStrong(String password) {
        return PATTERN.matcher(password).matches();
    }

    public boolean validatePassword(String password, String savedPasswordHash) {
        String passwordHash = getPasswordHash(password);
        return passwordHash.equals(savedPasswordHash);
    }

    public String getPasswordHash(String password) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }

        return createPasswordHash(messageDigest, password);
    }

    private String createPasswordHash(MessageDigest messageDigest, String password) {
        String passwordWithSalt = password + SALT;
        byte[] hashBytes = messageDigest.digest(passwordWithSalt.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        String hash = hashInt.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        return hash;
    }
}
