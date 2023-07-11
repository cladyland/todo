package kovalenko.vika.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashingTest {
    static Hashing hashing;
    String VALID_PASSWORD = "password";
    String VALID_PASSWORD_HASH = "9d021c245c9bbcf4b76a5a0fd8af50e7";

    @BeforeAll
    static void init() {
        hashing = new Hashing();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc12*AN", "SK_12upid"})
    void when_password_is_strong_return_true(String password) {
        assertTrue(hashing.isPasswordStrong(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcdefjh", "ABCD", "123456", "     ", "_________", "c12*AN="})
    void when_password_is_weak_return_false(String password) {
        assertFalse(hashing.isPasswordStrong(password));
    }

    @Test
    void validate_valid_password_return_true() {
        assertTrue(hashing.validatePassword(VALID_PASSWORD, VALID_PASSWORD_HASH));
    }

    @Test
    void validate_invalid_password_return_false() {
        assertFalse(hashing.validatePassword("password123", VALID_PASSWORD_HASH));
    }

    @RepeatedTest(5)
    void get_password_hash_return_the_same_hash() {
        assertEquals(VALID_PASSWORD_HASH, hashing.getPasswordHash("password"));
    }
}
