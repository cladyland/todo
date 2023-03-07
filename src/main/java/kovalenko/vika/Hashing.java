package kovalenko.vika;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {
    private static final String ALGORITHM = "MD5";

    public String getPasswordHash(String password) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hashBytes = messageDigest.digest(password.getBytes());
        BigInteger hashInt = new BigInteger(1, hashBytes);
        String hash = hashInt.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        return hash;
    }

    public boolean validatePassword(String password, String savedPasswordHash){
        String passwordHash = getPasswordHash(password);
        return passwordHash.equals(savedPasswordHash);
    }
}
